package com.example.asd

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import com.example.asd.todos.ViewModelProviderFactory
import com.example.discoding.SendUserInfo
import com.example.discoding.get_message
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Main : AppCompatActivity() {

    lateinit var todayAdapter : TodayAdapter
    lateinit var viewModel : TodoViewModel
    lateinit var todoList: MutableLiveData<MutableList<Todo>>

    var permissions = arrayOf(
        android.Manifest.permission.READ_CALL_LOG,
        android.Manifest.permission.SEND_SMS,
        android.Manifest.permission.WRITE_CONTACTS,
        android.Manifest.permission.READ_CONTACTS,
    )

    //년월 변수
    lateinit var selectedDate: LocalDate

//    var gson= GsonBuilder().setLenient().create()
//    private val retrofit = Retrofit.Builder()
//        .baseUrl("http://selfstudy.kro.kr:5000/")
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .build()
//
//    private val LedService = retrofit.create(sendLedSeekBarValue::class.java)
//    private val SoundService = retrofit.create(sendSoundSeekBarValue::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndstart()
        startProcess()
    }

    private fun startProcess() {
        setContentView(R.layout.main)

//        findViewById<SeekBar>(R.id.LedSeekBar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                LedService.SendLedValue(findViewById<SeekBar>(R.id.LedSeekBar).progress).enqueue(object :
//                    Callback<get_message> {
//                    override fun onResponse(
//                        call: Call<get_message>,
//                        response: Response<get_message>
//                    ) {
//                    }
//                    override fun onFailure(call: Call<get_message>, t: Throwable) {
//                        Log.d("result",t.toString())
//                    }
//                })
//            }
//        })
//        findViewById<SeekBar>(R.id.SoundSeekBar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                SoundService.SendSoundValue(findViewById<SeekBar>(R.id.LedSeekBar).progress).enqueue(object :
//                    Callback<get_message> {
//                    override fun onResponse(
//                        call: Call<get_message>,
//                        response: Response<get_message>
//                    ) {
//                    }
//                    override fun onFailure(call: Call<get_message>, t: Throwable) {
//                        Log.d("result",t.toString())
//                    }
//                })
//            }
//        })

        // DND part
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Turn on DND
        fun onDND(){
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.onDOD()
                toast("Do Not Disturb turned on.")
                val intent = Intent(this@Main, Studymode::class.java)
                startActivity(intent)
            }
        }

        // Turn off DND
        fun offDND(){
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.offDOD()
                toast("Do Not Disturb turned off")
            }
        }


        selectedDate = LocalDate.now()
        findViewById<TextView>(R.id.main_todolist_dash_middle).text = yearMonthFromDate(selectedDate)

        val sharedPreference = getSharedPreferences("uuid", 0)
        val editor  : SharedPreferences.Editor = sharedPreference.edit()

        // uuid 내부저장 일치 여부 확인.
        if (sharedPreference.getString("uuid", null) == null){
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


        //뷰모델 받아오기
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

        //여기서부터 nfc 짜
        onDND()

    }
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


    //DND기능
    // Method to check notification policy access status
    private fun checkNotificationPolicyAccess(notificationManager:NotificationManager):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.isNotificationPolicyAccessGranted){
                //toast("Notification policy access granted.")
                return true
            }else{
                toast("You need to grant notification policy access.")
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

}


private fun yearMonthFromDate(date: LocalDate): String{
    var formatter = DateTimeFormatter.ofPattern("YYYY년 M월 d일")

    //받아온 날짜를 해당 포맷으로 변경
    return date.format(formatter)
}




