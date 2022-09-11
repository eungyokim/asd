package com.example.asd

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.util.Log
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
import java.util.jar.Manifest


class Studymode : AppCompatActivity() {
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
}

