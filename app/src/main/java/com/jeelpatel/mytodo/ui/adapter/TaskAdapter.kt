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
import com.jeelpatel.mytodo.databinding.ItemTasksBinding
import com.jeelpatel.mytodo.domain.model.TaskModel

class TaskAdapter(
    val context: Context,
    val onStatusChange: (taskId: Int, isCompleted: Boolean) -> Unit,
    val onDeleted: (taskId: Int) -> Unit,
    val onTaskClick: (task: TaskModel) -> Unit
) : ListAdapter<TaskModel, TaskAdapter.TaskViewHolder>(DiffCallBack()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).taskId.toLong()
    }

    class TaskViewHolder(val binding: ItemTasksBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallBack : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
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
                MaterialAlertDialogBuilder(holder.itemView.context).setTitle("Delete")
                    .setMessage("Are you sure to delete Task?").setCancelable(false)
                    .setPositiveButton("Delete") { dialog, _ ->
                        onDeleted(task.taskId)
                        dialog.dismiss()
                    }.setNeutralButton("Cancel", null).show()
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

//                val intent = Intent(holder.itemView.context, TaskActivity::class.java)
//                intent.putExtra("TITLE", task.title)
//                intent.putExtra("DESC", task.description)
//                intent.putExtra("DUE_DATE", task.dueDate)
//                intent.putExtra("PRIORITY", task.priority)
//                intent.putExtra("IS_COMPLETED", task.isCompleted)
//                context.startActivity(intent)
            }
        }
    }
}