package com.jeelpatel.mytodo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ItemTasksBinding
import com.jeelpatel.mytodo.domain.model.TaskModel

class TaskPagingAdapter(
    private val context: Context,
    private val onStatusChange: (Int, Boolean) -> Unit,
    private val onDeleted: (Int) -> Unit,
    private val onTaskClick: (TaskModel) -> Unit
) : PagingDataAdapter<TaskModel, TaskPagingAdapter.TaskViewHolder>(DiffCallBack()) {



    class DiffCallBack : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem == newItem
    }



    class TaskViewHolder(val binding: ItemTasksBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding = ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) ?: return


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
                MaterialAlertDialogBuilder(holder.itemView.context).setTitle(context.getString(R.string.delete))
                    .setMessage(context.getString(R.string.are_you_sure_to_delete_task))
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.delete)) { dialog, _ ->
                        onDeleted(task.taskId)
                        dialog.dismiss()
                    }.setNeutralButton(context.getString(R.string.cancel), null).show()
            }

            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onStatusChange(task.taskId, true)
                } else {
                    onStatusChange(task.taskId, false)
                }
            }

            root.setOnClickListener {
                onTaskClick(task)
            }
        }
    }
}