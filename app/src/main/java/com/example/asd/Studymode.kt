package com.example.asd

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil.setContentView
import java.util.jar.Manifest


class Studymode : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.studymode)

    }
}

