package com.jeelpatel.mytodo.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    private val binding: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val title = intent.getStringExtra("TITLE")
        val desc = intent.getStringExtra("DESC")
        val dueDate = intent.getLongExtra("DUE_DATE", 0)
        val priority = intent.getIntExtra("PRIORITY", 0)
        val isCompleted = intent.getBooleanExtra("IS_COMPLETED", false)

        with(binding) {
            taskTitleTv.text = title
            taskDescriptionTv.text = desc
            taskStatusChip.text = if (isCompleted) "Completed" else "In Progress"
            taskPriorityChip.text = "Priority: $priority"
            taskDueDateTv.text = "Due : $dueDate"
        }
    }
}