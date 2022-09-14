package com.example.asd

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.CallLog
import android.provider.Settings
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.example.asd.getCode.get_Code
import com.google.gson.GsonBuilder
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
    private lateinit var studyRecord: TextView

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


        studyRecord = findViewById(R.id.record)
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

    private fun studying() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        fun offDND(){
            if (checkNotificationPolicyAccess(notificationManager)){
                notificationManager.offDOD()
                toast("Do Not Disturb turned off")
            }
        }
        timerTask = kotlin.concurrent.timer(period = 1000) {
            time++
            val sec = time % 60
            val min = time % 3600 /60
            val hour = time / 3600
            runOnUiThread {
                val nfcCheck = detectDetached()
                Log.e("nfcCheck", "$nfcCheck")
                if (nfcCheck == false) {
                    cancel()
                    finish()
                    offDND()

                }
                studyRecord.text = "%1$02d:%2$02d:%3$02d".format(hour, min, sec)
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
        val result = nfcAdapter?.ignore(nfcTag, 10, NfcAdapter.OnTagRemovedListener {  }, Handler())
        if (result == null) {
            return false
        }
        return result
    }

    /*뒤로 가기 버튼 막기*/
    override fun onBackPressed() {
//        super.onBackPressed()
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

}

