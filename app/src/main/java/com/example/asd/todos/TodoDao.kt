package com.example.asd.todos

<<<<<<< Updated upstream
class TodoDao {
=======
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface TodoDao {
    //전체 얻기. 기본값. 등록순서로 보이기
    @Query("select * from Todo order by registerTime desc")
    fun getAll() : MutableList<Todo>

    @Query("select * from Todo where year = :tyear AND month = :tmonth AND tdate = :tdate order by registerTime desc")
    fun getToday(tyear: String, tmonth: String, tdate: String) : MutableList<Todo>

    @Query("select * from Todo where year = :year AND month = :month AND tdate = :date order by registerTime desc")
    fun getSelectedDay(year: String, month: String, date: String) : MutableList<Todo>

    @Insert
    fun insert(todo: Todo)

    @Delete
    fun delete(todo: Todo)
>>>>>>> Stashed changes
}