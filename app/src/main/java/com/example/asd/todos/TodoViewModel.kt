package com.example.asd.todos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room

class TodoViewModel(context: Context) : ViewModel() {

    private val todoDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "todo")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val todoDao = todoDatabase.todoDao()

    var Date = java.util.Calendar.getInstance()

    private val todos = getTodayList()

    val mutableLiveData = MutableLiveData<MutableList<Todo>>(todos)

    var selectedData = MutableLiveData<MutableList<Todo>>()

    private val _date = MutableLiveData<String>()

    val date: LiveData<String>
        get() = _date

    fun getList(): MutableList<Todo> {
        return getAll()
    }

    fun getTodayList(): MutableList<Todo> {
        var tyear = Date.get(java.util.Calendar.YEAR)
        var tmonth = Date.get(java.util.Calendar.MONTH) + 1
        var tdate = Date.get(java.util.Calendar.DATE)
        return todoDao.getToday(tyear.toString(), tmonth.toString(), tdate.toString())
    }

    fun getSelectedList(data: String): MutableList<Todo> {
        return todoDao.getSelectedDay(
            data.split("/")[0],
            data.split("/")[1],
            data.split("/")[2]
        )
    }

    fun getAll(): MutableList<Todo> {
        return todoDao.getAll()
    }

    fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    fun updateDate(clickDay: String) {
        _date.value = clickDay
    }
}