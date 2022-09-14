package com.example.asd

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import com.example.asd.todos.ViewModelProviderFactory
import com.example.asd.databinding.CalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Calendar : AppCompatActivity() {

    // Todo List
    private lateinit var adapter: TodoAdapter
    lateinit var viewModel : TodoViewModel


    private lateinit var binding: CalendarBinding

    //년월 변수
    lateinit var selectedDate: LocalDate

    lateinit var dayText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        // 달력의 날짜를 누르지 않고 'Todo'추가 시 오늘의 날짜로 입력이 된다.
        dayText = "${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}"

        // Todo List
        //뷰모델 받아오기
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        //recycler view에 보여질 아이템 Room에서 받아오기
        viewModel.date.observe(this, androidx.lifecycle.Observer {
            Log.d("date",it.toString());
            val list = viewModel.getSelectedList(it.toString())

            adapter = TodoAdapter(this, list, viewModel);
            Log.d("date",list.toString());

            findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
            findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this)
        })


        // calendar
        //binding 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.calendar)

        //현재 날짜
        selectedDate = LocalDate.now()

        setMonthView()

        //이전달로 넘어가기
        binding.calendarCalendarDashLeft.setOnClickListener {
            //현재 월 - 1
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        //다음달로 넘어가기
        binding.calendarCalendarDashRight.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }

        // Add Todo List
        findViewById<Button>(R.id.add_button).setOnClickListener {
            if (findViewById<TextView>(R.id.recycleradd).text.toString() != "") {
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

                var year = dayText.split("/")[0]
                var month = dayText.split("/")[1]
                var date = dayText.split("/")[2]
                var time = "${year}-${month}-${date}"



                val todo = Todo(findViewById<TextView>(R.id.recycleradd).text.toString(), time, year.toInt(), month.toInt(), date.toInt(), color.toString())
                viewModel.insert(todo)
                setList("${year}/${month}/${date}")
                findViewById<TextView>(R.id.recycleradd).setText("")
            }
            else {
                setList("${LocalDate.now().year}/${LocalDate.now().monthValue}/${LocalDate.now().dayOfMonth}")
                findViewById<TextView>(R.id.recycleradd).setText("")
            }
        }
        findViewById<Button>(R.id.add_button).performClick()

        // Navigation Bar
        val navigationbar_home = findViewById<ImageButton>(R.id.navigation_bar_home)
        val navigationbar_info = findViewById<ImageButton>(R.id.navigation_bar_info)

        navigationbar_home.setOnClickListener {
            val intent = Intent(this@Calendar, Main::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }
        navigationbar_info.setOnClickListener {
            val intent = Intent(this@Calendar, setting::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }
        findViewById<ConstraintLayout>(R.id.SampleLayoutView1).setOnTouchListener(object: OnSwipeTouchListener(this@Calendar) {
            override fun onSwipeLeft() {
                val intent = Intent(this@Calendar, Main::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)
            }
        })
    }

    //날짜 화면에 보여주기
    private fun setMonthView() {
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        var yearMonth = YearMonth.from(selectedDate)
        var lastDay = yearMonth.lengthOfMonth()

        var _isIn = ArrayList<Boolean>()

        //해당 월의 첫째 날
        var firstDay = selectedDate.withDayOfMonth(1)

        //첫 번째날 요일(월요일 : 1, 일요일 : 7)
        var dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1..dayOfWeek){
            _isIn.add(false)
        }
        for (i in 1..lastDay){
            val list = viewModel.getSelectedList("${selectedDate.year}/${selectedDate.monthValue}/${i}")
            if(list.toString() == "[]") _isIn.add(false)
            else _isIn.add(true);
        }

            //년월 텍스트 셋팅
        binding.calendarCalendarYearnmonth.text = yearMonthFromDate(selectedDate)

        //날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray(selectedDate)

        //어댑터 초기화
        val adapter = CalendarAdapter(dayList, _isIn)


        //레이아웃 설정(열 7개)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)

        //레이아웃 적용
        binding.calendarRecyclerview.layoutManager = manager

        //어댑터 적용
        binding.calendarRecyclerview.adapter = adapter

        adapter.setItemClickListener(object: CalendarAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                var year = binding.calendarCalendarYearnmonth.text.split(" ")[0]
                year = year.replace("년", "")
                var month = binding.calendarCalendarYearnmonth.text.split(" ")[1]
                month = month.replace("월", "")
                var date = dayList[position]

                var clickDay: String = "${year}/${month}/${date}"

                dayText = clickDay

                viewModel.updateDate(clickDay)
            }
        })
    }

    private fun yearMonthFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("YYYY년 M월")

        //받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }

    //날짜 생성
    private fun dayInMonthArray(date: LocalDate): ArrayList<String> {
        val dayList = ArrayList<String>()

        var yearMonth = YearMonth.from(date)

        //해당 월 마지막 날짜
        var lastDay = yearMonth.lengthOfMonth()

        //해당 월의 첫째 날
        var firstDay = selectedDate.withDayOfMonth(1)

        //첫 번째날 요일(월요일 : 1, 일요일 : 7)
        var dayOfWeek = firstDay.dayOfWeek.value

        for(i in 1..41) {
            if(i <= dayOfWeek || i > (lastDay + dayOfWeek)){
                //만약 일요일이 첫째날이 되버리면 일주일의 공백이 생긴 상태로 나오는 걸 방지
                if (dayOfWeek == 7 && i < 8) {
                    continue
                }
                dayList.add("")
            }
            else {
                dayList.add((i - dayOfWeek).toString())
            }
        }

        return dayList
    }

    // 화면을 다시 돌리기 위해 viewModel 내에 있는 LiveData의 value를 변경시켜줌.
    // value가 변경됨에 따라 observer에 설정된 함수가 실행되고 UI가 변경됨.
    fun setList(data:String) {
        val list = viewModel.getSelectedList(data)
        adapter = TodoAdapter(this, list, viewModel);
        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this)
    }
}