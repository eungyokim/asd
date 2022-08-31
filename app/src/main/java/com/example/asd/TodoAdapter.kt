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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asd.todos.Todo
import com.example.asd.todos.TodoViewModel
import java.util.*

class TodoAdapter(val context: Context,
                  var itemList: MutableList<Todo>,
                  val viewModel: TodoViewModel,
//                val setList: (data:String) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_todo,parent,false)

        return TodoViewHolder(view)

    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        // position에 해당하는 Todo객체를 얻음
        val todo = itemList[position];

        holder.itemView.findViewById<TextView>(R.id.todo_text).text = todo.text

        //todo의 날짜와 시간이 표시됨.
        holder.todoTime.apply {
            holder.itemView.findViewById<TextView>(R.id.todo_time).text = "${todo.date}"
            holder.itemView.findViewById<TextView>(R.id.todo_time).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.todo_color).setBackgroundColor(color)
        }

        //아이템 내의 x버튼을 누를 경우 삭제여부 확인.
        holder.todoDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") {str, dialogInterface ->
                    val todo = itemList[position]
                    viewModel.delete(todo)
                }
                .setNegativeButton("취소",null)
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    inner class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val todoInfo = itemView.findViewById<ConstraintLayout>(R.id.todo_info)
        val todoText = itemView.findViewById<TextView>(R.id.todo_text)
        val todoTime = itemView.findViewById<TextView>(R.id.todo_time)
        val todoDelete = itemView.findViewById<ImageView>(R.id.todo_delete)
    }

}