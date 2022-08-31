package com.example.asd

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import com.example.asd.todos.ViewModelProviderFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Main : AppCompatActivity() {

    lateinit var todayAdapter : TodayAdapter
    lateinit var viewModel : TodoViewModel
    lateinit var todoList: MutableLiveData<MutableList<Todo>>

    //년월 변수
    lateinit var selectedDate: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

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



