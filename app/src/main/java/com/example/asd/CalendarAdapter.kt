package com.example.asd

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import com.example.asd.todos.ViewModelProviderFactory
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.*
import java.util.Calendar
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class CalendarAdapter(private val dayList: ArrayList<String>):
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {
    lateinit var todayAdapter : TodoAdapter
    lateinit var viewModel : TodoViewModel
    lateinit var todoList: MutableLiveData<MutableList<Todo>>
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.calendar_cell_daytext)
    }


    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell, parent, false)
        return ItemViewHolder(view)
    }

    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // 오늘 날짜, (**/**)의 형식
        // ex) '8/21'
        //     '10/21' 등등
        holder.dayText.text = dayList[holder.adapterPosition]
<<<<<<< Updated upstream

        holder.itemView.setOnClickListener{
=======
        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
>>>>>>> Stashed changes
        }

    }

    override fun getItemCount(): Int {
        return dayList.size
    }
    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}