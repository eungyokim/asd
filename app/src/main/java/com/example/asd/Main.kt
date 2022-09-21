package com.example.asd

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import com.example.asd.todos.ViewModelProviderFactory
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Main : AppCompatActivity(),  NfcAdapter.ReaderCallback{

    lateinit var todayAdapter : TodayAdapter
    lateinit var viewModel : TodoViewModel
    lateinit var todoList: MutableLiveData<MutableList<Todo>>

    var permissions = arrayOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.SEND_SMS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        )

    //년월 변수
    lateinit var selectedDate: LocalDate

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent

    // 자체 백엔드 서버 연동
    var gson= GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://asdapi.implude.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val LedService = retrofit.create(sendLedSeekBarValue::class.java)
    private val SoundService = retrofit.create(sendSoundSeekBarValue::class.java)
    private val LedHex = retrofit.create(sendLedHex::class.java)
    private val WhiteNoiseIndex = retrofit.create(sendSoundIndex::class.java)

    // DND part
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndstart()
        startProcess()
    }

    private fun startProcess() {
        setContentView(R.layout.main)
        findViewById<ImageButton>(R.id.bulb).backgroundTintList = ColorStateList.valueOf(getSharedPreferences("test", 0).getInt("LED_color", -14907928))
//        val nfcAdapter : NfcAdapter = NfcAdapter.getDefaultAdapter(applicationContext)
//        val isNfcOn : Boolean = nfcAdapter.isEnabled()
//
//        if( isNfcOn == false){
//            Toast.makeText(application, "공부모드를 시작하려면 NFC를 켜주세요.", Toast.LENGTH_SHORT).show()
//        }
        checkNotificationPolicyAccess(getSystemService(NOTIFICATION_SERVICE) as NotificationManager)

        val sharedPreference = getSharedPreferences("test", 0)
        val editor  : SharedPreferences.Editor = sharedPreference.edit()

        findViewById<SeekBar>(R.id.SoundSeekBar).setProgress(sharedPreference.getInt("white_noise", 0))
        findViewById<SeekBar>(R.id.LedSeekBar).setProgress(sharedPreference.getInt("LED", 0))

        var checkedItemPosition = sharedPreference.getInt("white_noise_index", 0)
        val array = arrayOf("장작타는소리", "빗소리", "도서관소리")


        selectedDate = LocalDate.now()
        findViewById<TextView>(R.id.main_todolist_dash_middle).text = yearMonthFromDate(selectedDate)

        findViewById<ImageButton>(R.id.main_help).setOnClickListener{
            val url = "https://eks0220.notion.site/eks0220/ASD-30f28bfa8e1d4cada4b30090c6fbbe08"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.bulb).setOnClickListener {
            ColorPickerDialog
                .Builder(this)        				// Pass Activity Instance
                .setTitle("LED 색깔 선택")
                .setNegativeButton("뒤로가기")
                .setPositiveButton("선택")// Default "Choose Color"
                .setColorShape(ColorShape.CIRCLE)   // Default ColorShape.CIRCLE
                .setDefaultColor(sharedPreference.getInt("LED_color", 1869288))    // Pass Default Color
                .setColorListener { color, colorHex ->
                    LedHex.SendLedHex(getSharedPreferences("uuid", 0).getString("uuid", "").toString(),colorHex).enqueue(object :
                        Callback<getMsg> {
                        override fun onResponse(
                            call: Call<getMsg>,
                            response: Response<getMsg>
                        ) {
                            editor.putInt("LED_color", color)
                            editor.putString("LED_Hex", colorHex)
                            editor.commit()
                            findViewById<ImageButton>(R.id.bulb).backgroundTintList = ColorStateList.valueOf(sharedPreference.getInt("LED_color", 0))
                            Log.e("Fdasfsdaf", response.body().toString())
                        }
                        override fun onFailure(call: Call<getMsg>, t: Throwable) {
                            Log.d("result",t.toString())
                        }
                    })
                }
                .show()
        }

        findViewById<ImageButton>(R.id.Sound).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("백색소음 선택하기")
                .setSingleChoiceItems(array, checkedItemPosition, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "which : $which")
                        checkedItemPosition = which
                    }
                })
                .setPositiveButton("선택하기", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.d("MyTag", "checkedItemPosition : $checkedItemPosition")
                        WhiteNoiseIndex.SendSoundIndex(getSharedPreferences("uuid", 0).getString("uuid", "").toString(), checkedItemPosition).enqueue(object :
                            Callback<getMsg> {
                            override fun onResponse(
                                call: Call<getMsg>,
                                response: Response<getMsg>
                            ) {
                                editor.putInt("white_noise_index", checkedItemPosition)
                                editor.commit()
                            }
                            override fun onFailure(call: Call<getMsg>, t: Throwable) {
                            }
                        })
                    }
                })
                .show()
        }


        // LED 밝기 제어
        findViewById<SeekBar>(R.id.LedSeekBar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                LedService.SendLedValue(getSharedPreferences("uuid", 0).getString("uuid", "").toString(), findViewById<SeekBar>(R.id.LedSeekBar).progress).enqueue(object :
                    Callback<getMsg> {
                    override fun onResponse(
                        call: Call<getMsg>,
                        response: Response<getMsg>
                    ) {
                        editor.putInt("LED", findViewById<SeekBar>(R.id.LedSeekBar).progress)
                        editor.commit()
                        Log.e("Fdasfsdaf", response.body().toString())
                    }
                    override fun onFailure(call: Call<getMsg>, t: Throwable) {
                        Log.d("result",t.toString())
                    }
                })
            }
        })

        // 음량 제어
        findViewById<SeekBar>(R.id.SoundSeekBar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                SoundService.SendSoundValue(getSharedPreferences("uuid", 0).getString("uuid", "").toString(), findViewById<SeekBar>(R.id.LedSeekBar).progress).enqueue(object :
                    Callback<getMsg> {
                    override fun onResponse(
                        call: Call<getMsg>,
                        response: Response<getMsg>
                    ) {
                        Log.e("Fdasfsdaf", response.body().toString())
                        editor.putInt("white_noise", findViewById<SeekBar>(R.id.SoundSeekBar).progress)
                        editor.commit()
                    }
                    override fun onFailure(call: Call<getMsg>, t: Throwable) {
                        Log.d("result",t.toString())
                    }
                })
            }
        })


        // uuid 내부저장 일치 여부 확인.
        if (getSharedPreferences("uuid", 0).getString("uuid", "") == ""){
            val intent = Intent(this@Main, setting::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }
        // Navigation bar control
        val navigationbar_book = findViewById<ImageButton>(R.id.navigation_bar_book)
        val navigationbar_info = findViewById<ImageButton>(R.id.navigation_bar_info)

        navigationbar_book.setOnClickListener {
            val intent = Intent(this@Main, Calendar::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
        }
        navigationbar_info.setOnClickListener {
            val intent = Intent(this@Main, setting::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)
        }


        //뷰모델 받아오기 - Todo
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        //recycler view에 보여질 아이템 Room에서 받아오기
        todoList = viewModel.mutableLiveData
        todoList.observe(this, Observer {
            todayAdapter.itemList = it
            todayAdapter.notifyDataSetChanged()
        })

        todayAdapter = TodayAdapter(this, mutableListOf<Todo>(), viewModel, ::setList)


        //recycler view에 adapter와 layout manager 넣기
        findViewById<RecyclerView>(R.id.recyclerView).adapter = todayAdapter
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this)

        findViewById<ConstraintLayout>(R.id.SampleLayoutView1).setOnTouchListener(object: OnSwipeTouchListener(this@Main) {
            override fun onSwipeLeft() {
                val intent = Intent(this@Main, setting::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)
            }
            override fun onSwipeRight() {
                val intent = Intent(this@Main, Calendar::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
            }
        })



    }
    // Version Check for Send sms and detect call
    private fun checkAndstart() {
        if ( isLower23() || isPermitted()){
            startProcess()
        }else{
            ActivityCompat.requestPermissions(this, permissions, 99)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isPermitted(): Boolean {
        for(perm in permissions){
            if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }
    private fun isLower23(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }


    //DND
    // Method to check notification policy access status
    private fun checkNotificationPolicyAccess(notificationManager:NotificationManager):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.isNotificationPolicyAccessGranted){
                return true
            }else{
                // If notification policy access not granted for this package
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }else{
            toast("Device does not support this feature.")
        }
        return false
    }
    fun NotificationManager.onDOD(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        }
    }


    // Extension function to turn off do not disturb
    fun NotificationManager.offDOD(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

    // Extension function to show toast message
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun setList() {
        todoList.value = viewModel.getTodayList()
    }

    override fun onResume() {
        super.onResume()
        checkIfHasNFCHardWare()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
        nfcAdapter?.disableReaderMode(this)
    }
    fun checkIfHasNFCHardWare() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, Main::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE
        )
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIntent(intent)
    }

    private fun resolveIntent(intent: Intent) {
        val action = intent?.action
        if(NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            val turnOnStudyMode = Intent(this, Studymode::class.java)
            turnOnStudyMode.putExtra("nfcTag", tag)
            startActivity(turnOnStudyMode)
        }
    }
    override fun onTagDiscovered(tag: Tag?) {
    }
}

private fun yearMonthFromDate(date: LocalDate): String{
    var formatter = DateTimeFormatter.ofPattern("YYYY년 M월 d일")

    //받아온 날짜를 해당 포맷으로 변경
    return date.format(formatter)
}
