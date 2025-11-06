package com.jeelpatel.mytodo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ItemDeletedTasksBinding
import com.jeelpatel.mytodo.domain.model.TaskModel

class RecycleBinTaskAdapter(
    val context: Context,
    val onRestore: (taskId: Int) -> Unit,
    val onTaskClick: (task: TaskModel) -> Unit
) :
    ListAdapter<TaskModel, RecycleBinTaskAdapter.TaskViewHolder>(DiffCallBack()) {
    class TaskViewHolder(val binding: ItemDeletedTasksBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleBinTaskAdapter.TaskViewHolder {
        val binding =
            ItemDeletedTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecycleBinTaskAdapter.TaskViewHolder, position: Int) {
        val task = getItem(position)

        with(holder.binding) {
            taskCheckBox.setOnCheckedChangeListener(null)
            deleteBtn.setOnClickListener(null)

            taskTitleTv.text = task.title
            taskDescriptionTv.text = task.description
            taskCheckBox.isChecked = task.isCompleted


            when (task.priority) {
                1 -> taskPriorityCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.p1)
                )

                2 -> taskPriorityCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.p2)
                )

                3 -> taskPriorityCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.p3)
                )
            }

            deleteBtn.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Restore")
                    .setCancelable(false)
                    .setPositiveButton("Restore") { dialog, _ ->
                        onRestore(task.taskId)
                        dialog.dismiss()
                    }
                    .setNeutralButton("Cancel", null)
                    .show()
            }

            root.setOnClickListener {
                onTaskClick(task)
            }
        }
    }
}