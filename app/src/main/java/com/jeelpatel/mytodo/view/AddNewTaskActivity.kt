package com.jeelpatel.mytodo.view

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityAddNewTaskBinding
import com.jeelpatel.mytodo.model.TaskRepository
import com.jeelpatel.mytodo.model.UserRepository
import com.jeelpatel.mytodo.model.local.database.DatabaseBuilder
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.viewModel.TaskViewModel
import com.jeelpatel.mytodo.viewModel.TaskViewModelFactory
import com.jeelpatel.mytodo.viewModel.UserViewModel
import com.jeelpatel.mytodo.viewModel.UserViewModelFactory
import kotlinx.coroutines.launch

class AddNewTaskActivity : AppCompatActivity() {

    private val binding: ActivityAddNewTaskBinding by lazy {
        ActivityAddNewTaskBinding.inflate(layoutInflater)
    }
    lateinit var taskViewModel: TaskViewModel
    lateinit var sessionManager: SessionManager
    var currentUserID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // for current user Status
        sessionManager = SessionManager(this)
        currentUserID = sessionManager.getUserId()

        // Setup Room + MVVM
        val db = DatabaseBuilder.getInstance(this)
        val taskDao = db.taskDao()
        val taskRepository = TaskRepository(taskDao)
        val taskFactory = TaskViewModelFactory(taskRepository)
        taskViewModel = ViewModelProvider(this, taskFactory)[TaskViewModel::class.java]

        observeData()

        binding.createNewTaskBtn.setOnClickListener {
            val task = TaskEntity(
                title = binding.taskTitleEt.text.toString(),
                description = binding.taskDescriptionEt.text.toString(),
                priority = binding.prioritySlider.value.toInt(),
                dueDate = binding.taskDueDateEt.text.toString().toLong(),
                userOwnerId = currentUserID,
                isCompleted = false
            )
            taskViewModel.createNewTask(task)
        }
    }

    private fun observeData() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    taskViewModel.message.collect { msg ->
                        Toast.makeText(this@AddNewTaskActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }

                launch {
                    taskViewModel.isTaskCreated.collect { isCreated ->
                        if (isCreated) {
                            finish()
                        }
                    }
                }
            }
        }
    }
}