package com.example.asd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.telecom.TelecomManager
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.jar.Manifest


class CallReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val zenMode = Settings.Global.getInt(context?.contentResolver, "zen_mode")
        val smsManager: SmsManager = SmsManager.getDefault()
        val telephonyManager = context!!.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        Log.e("fdasf", intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString())

        // 방해금지모드가 켜져(공부모드를 실행중이다.) 있는 상황에서 통화가 수신되면 문자를 회신한다.
        if(intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE){
            if(zenMode !=  0){
                if( intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) != null){
                    smsManager.sendTextMessage(
                        intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER),
                        null,
                        "<자동발신 - ASD>현재 사용자는 학습하고 있습니다. 잠시만 기다려주세요.",
                        null,
                        null
                    )
                }
            }
        }
    }
}