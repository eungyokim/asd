package com.example.asd

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import java.util.*

class TodayAdapter(val context: Context,
                   var itemList: MutableList<Todo>,
                   val viewModel: TodoViewModel,
                   val setList: () -> Unit
) : RecyclerView.Adapter<TodayAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayAdapter.TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_todo,parent,false)

        return TodoViewHolder(view)

    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        // position에 해당하는 Todo객체를 얻음
        val todo = itemList[getItemCount() - 1 - position];

        holder.itemView.findViewById<TextView>(R.id.todo_text).text = todo.text

        //todo의 날짜와 시간이 표시됨.
        holder.todoTime.apply {
            holder.itemView.findViewById<TextView>(R.id.todo_time).text = "${todo.date}"
            holder.itemView.findViewById<TextView>(R.id.todo_time).visibility = View.VISIBLE

            if(todo.index!!.toInt() % 5 == 0){
                holder.itemView.findViewById<TextView>(R.id.todo_color).setBackgroundResource(R.drawable.todo_list_itemv1)
            }else if(todo.index!!.toInt() % 5 == 1){
                holder.itemView.findViewById<TextView>(R.id.todo_color).setBackgroundResource(R.drawable.todo_list_itemv2)
            }else if(todo.index!!.toInt() % 5 == 2){
                holder.itemView.findViewById<TextView>(R.id.todo_color).setBackgroundResource(R.drawable.todo_list_itemv3)
            }else if(todo.index!!.toInt() % 5 == 3){
                holder.itemView.findViewById<TextView>(R.id.todo_color).setBackgroundResource(R.drawable.todo_list_itemv4)
            }else if(todo.index!!.toInt() % 5 == 4){
                holder.itemView.findViewById<TextView>(R.id.todo_color).setBackgroundResource(R.drawable.todo_list_itemv5)
            }
        }

        //아이템 내의 x버튼을 누를 경우 삭제여부 확인.
        holder.todoDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") {str, dialogInterface ->
                    val todo = itemList[position]
                    viewModel.delete(todo)
                    setList()
                }
                .setNegativeButton("취소",null)
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val todoTime = itemView.findViewById<TextView>(R.id.todo_time)
        val todoDelete = itemView.findViewById<ImageView>(R.id.todo_delete)
    }
}