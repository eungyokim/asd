package com.example.asd

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.TodoViewModel
import com.example.asd.todos.ViewModelProviderFactory
import java.time.LocalDate
import java.time.YearMonth
import kotlin.collections.ArrayList

class CalendarAdapter(private val dayList: ArrayList<String>, private val value: ArrayList<Boolean>): RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.calendar_cell_daytext)
        val color: View = itemView.findViewById(R.id.calendar_cell_color)
    }

    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell, parent, false)
        return ItemViewHolder(view)
    }

    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val selectedDate = LocalDate.now()
        var yearMonth = YearMonth.from(selectedDate)
        var lastDay = yearMonth.lengthOfMonth()
        if (value[position].toString() == "true"){
            holder.color.setBackgroundResource(R.drawable.calendar_cell_color_blue)
        }
        // 오늘 날짜, (**/**)의 형식
        // ex) '8/21'
        //     '10/21' 등등
        holder.dayText.text = dayList[holder.adapterPosition]

        // 제대로 클릭을 했는지 확인함.
        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
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