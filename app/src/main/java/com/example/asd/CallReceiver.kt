package com.example.asd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import android.provider.CallLog
import android.provider.Settings

class CallReceiver:BroadcastReceiver() {
    var searchText = ""
    var sortText = "asd"

    override fun onReceive(context: Context?, intent: Intent?) {
        val zenMode = Settings.Global.getInt(context?.contentResolver, "zen_mode")
        if(intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING){
            if (zenMode == 1){
                showToastMsg(context!!,"Incoming...")
                getPhoneNumbers(context!!, sortText, searchText)
            }

        }

    }
    fun showToastMsg(c:Context, msg: String){
        val toast = Toast.makeText(c, msg, Toast.LENGTH_LONG)
        toast.show()
    }
    fun getPhoneNumbers(c:Context, sort:String, name:String): List<String>{
        val list = mutableListOf<String>()

        val callLogUri = CallLog.Calls.CONTENT_URI
        val proj = arrayOf(
            CallLog.Calls.PHONE_ACCOUNT_ID,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_PHOTO_URI,
        )
        val cursor = c.contentResolver.query(callLogUri, proj, null,null,null)
        cursor?.moveToFirst()
        if(cursor?.isFirst == true){
            val id = cursor?.getString(0)
            val name = cursor?.getString(1)
            var number = cursor?.getString(2)
            val photo = cursor?.getString(3)

            val smsManager: SmsManager = SmsManager.getDefault()

            smsManager.sendTextMessage(
                number,
                null,
                "<자동 발신>사용자는 현재 공부중입니다. 나중에 다시 걸어주세요.",
                null,
                null
            )
        }

        return list
    }
}