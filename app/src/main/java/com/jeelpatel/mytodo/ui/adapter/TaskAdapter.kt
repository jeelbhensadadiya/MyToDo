package com.jeelpatel.mytodo.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeelpatel.mytodo.databinding.ItemTasksBinding
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.ui.view.TaskActivity

class TaskAdapter(
    val context: Context,
    val onStatusChange: (taskId: Int, isCompleted: Boolean) -> Unit
) :
    ListAdapter<TaskModel, TaskAdapter.TaskViewHolder>(DiffCallBack()) {
    class TaskViewHolder(val binding: ItemTasksBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val binding = ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val task = getItem(position)

        with(holder.binding) {
            taskCheckBox.setOnCheckedChangeListener(null)

            taskTitleTv.text = task.title
            taskDescriptionTv.text = task.description
            taskCheckBox.isChecked = task.isCompleted
            taskPriorityTv.text = task.priority.toString()

            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onStatusChange(task.taskId, true)
                } else {
                    onStatusChange(task.taskId, false)
                }
            }

            root.setOnClickListener {
                val intent = Intent(context.applicationContext, TaskActivity::class.java)
                intent.putExtra("TITLE", task.title)
                intent.putExtra("DESC", task.description)
                intent.putExtra("DUE_DATE", task.dueDate)
                intent.putExtra("PRIORITY", task.priority)
                intent.putExtra("IS_COMPLETED", task.isCompleted)
                context.startActivity(intent)
            }
        }
    }
}