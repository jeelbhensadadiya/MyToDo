package com.jeelpatel.mytodo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeelpatel.mytodo.databinding.ItemTasksBinding
import com.jeelpatel.mytodo.model.local.entity.TaskEntity

class TaskAdapter : ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(DiffCallBack()) {
    class TaskViewHolder(val binding: ItemTasksBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean =
            oldItem == newItem
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val binding = ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val task = getItem(position)

        with(holder.binding) {
            taskTitleTv.text = task.title
            taskDescriptionTv.text = task.description
            taskCheckBox.isSelected = task.isCompleted
            taskPriorityTv.text = when (task.priority) {
                1 -> "🟢"
                2 -> "🟡"
                3 -> "🔴"
                else -> " "
            }
        }

    }

}