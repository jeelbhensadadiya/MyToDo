package com.jeelpatel.mytodo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeelpatel.mytodo.databinding.ItemTodosBinding
import com.jeelpatel.mytodo.data.remote.dto.TodoDto

class TodoAdapter(val context: Context) :
    ListAdapter<TodoDto, TodoAdapter.TodoViewHolder>(TodoAdapter.DiffCallBack()) {

    class TodoViewHolder(val binding: ItemTodosBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<TodoDto>() {
        override fun areItemsTheSame(oldItem: TodoDto, newItem: TodoDto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoDto, newItem: TodoDto): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val binding = ItemTodosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        val todo = getItem(position)

        with(holder.binding) {
            taskTitleTv.text = todo.title
            taskCheckBox.isChecked = todo.completed
        }
    }
}