package com.example.asd

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.appsearch.AppSearchSchema
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import android.provider.Settings
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.example.asd.getCode.get_Code
import com.google.gson.GsonBuilder
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.jar.Manifest


class Studymode : AppCompatActivity() {

    private var time = 0
    private var timerTask: Timer? = null
    private lateinit var currntTime: TextView
    private lateinit var studyRecord: TextView
    private lateinit var studyTimeText: TextView
    private lateinit var clockText:TextView
    private lateinit var alert:TextView
    private lateinit var fiveSecond: TextView

    var nfcTag: Tag? = null
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
//    var gson= GsonBuilder().setLenient().create()
//    private val retrofit = Retrofit.Builder()
//        .baseUrl("https://asdapi.implude.kr/")
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .build()
//
//    private val startStudymode = retrofit.create(startStudymode::class.java)
//    private val stopStudymode = retrofit.create(stopStudymode::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.studymode)
        // DND part
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Turn on DND
        fun onDND(){
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.onDOD()
                toast("Do Not Disturb turned on.")
            }
        }

        currntTime = findViewById(R.id.currentTime)
        clockText = findViewById(R.id.clock)
        studyTimeText = findViewById(R.id.studyTime)
        studyRecord = findViewById(R.id.record)
        alert = findViewById(R.id.alert)
        fiveSecond = findViewById(R.id.fiveSecond)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val intent: Intent = getIntent()
        nfcTag = intent.getParcelableExtra("nfcTag")
        onDND()
        studying()

//        startStudymode.SendStudyTime().enqueue(object :
//            Callback<Message> {
//            override fun onResponse(
//                call: Call<Message>,
//                response: Response<Message>
//            ) {
//            }
//            override fun onFailure(call: Call<Message>, t: Throwable) {
//                Log.d("result",t.toString())
//            }
//        })
        //        stopStudymode.SendStudyTime().enqueue(object :
//            Callback<Message> {
//            override fun onResponse(
//                call: Call<Message>,
//                response: Response<Message>
//            ) {
//            }
//            override fun onFailure(call: Call<Message>, t: Throwable) {
//                Log.d("result",t.toString())
//            }
//        })
    }
    /*화면 풀스크린 띄우기*/
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }
    private fun hideNavigationBar() {
        val decorView: View = this.window.decorView
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread { decorView.systemUiVisibility = uiOptions }
            }
        }
        timer.scheduleAtFixedRate(task, 1, 2)
    }

    private fun studying() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        fun offDND(){
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.offDOD()
                toast("Do Not Disturb turned off")
            }
        }
        /*detechedCheck = 1이면 스피커에 폰이 붙어있음/ 0이면 반대 */
        var detechedCheck = 1
        /*5초 내로 nfc가 붙는 지 체크*/
        var fiveCount = 0

        var backUpTime = 0
        var backUpSec = 0
        var backUpMin = 0
        var backUpHour = 0
        timerTask = kotlin.concurrent.timer(period = 1000) {
            time++
            var sec = time % 60
            var min = time % 3600 /60
            var hour = time / 3600
            runOnUiThread {
                val nfcCheck = detectDetached()
                Log.e("nfcCheck", "$nfcCheck")
                if (nfcCheck == false) {
                    if (detechedCheck == 1) {
                        backUpTime = time
                        backUpSec = sec
                        backUpMin = min
                        backUpHour = hour
                        detechedCheck = 0
                        fiveCount += 1
                    }
                    else if (detechedCheck == 0 && fiveCount < 15) {
                        fiveCount += 1
                    }
                    else if(fiveCount >= 15) {
                        cancel()
                        finish()
                        offDND()
                    }
                }
                else if(nfcCheck == true && detechedCheck == 0) {
                    fiveCount = 0
                    detechedCheck = 1
                    time = backUpTime
                    sec = backUpSec
                    min = backUpMin
                    hour = backUpHour
                }
                if (detechedCheck == 0) {
                    currntTime.setTextColor(Color.BLACK)
                    clockText.setTextColor(Color.BLACK)
                    studyTimeText.setTextColor(Color.BLACK)
                    studyRecord.setTextColor(Color.BLACK)
                    alert.setTextColor(Color.WHITE)
                    alert.text = "공부시간을 이어가려면 15초 이내로 스피커에 폰을 다시 올려주세요."
                    fiveSecond.setTextColor(Color.RED)
                    fiveSecond.text = "${16 - fiveCount}"
                }
                else {
                    currntTime.setTextColor(Color.WHITE)
                    clockText.setTextColor(Color.WHITE)
                    studyTimeText.setTextColor(Color.WHITE)
                    studyRecord.setTextColor(Color.WHITE)
                    alert.setTextColor(Color.BLACK)
                    fiveSecond.setTextColor(Color.BLACK)
                    studyRecord.text = "%1$02d:%2$02d:%3$02d".format(hour, min, sec)
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }
    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    fun detectDetached(): Boolean{
        val result = nfcAdapter?.ignore(
            nfcTag,
            1000,
            NfcAdapter.OnTagRemovedListener  {},
            Handler())
        if (result == null) {
            return false
        }
        return result
    }
    //DND
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

    // Extension function to show toast message
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    /*홈, 뒤로가기 등의 버튼 막기*/
    override fun onBackPressed() {
//        super.onBackPressed()
    }
}

