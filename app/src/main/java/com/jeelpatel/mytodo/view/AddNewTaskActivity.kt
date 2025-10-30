package com.jeelpatel.mytodo.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.databinding.ActivityAddNewTaskBinding
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class AddNewTaskActivity : AppCompatActivity() {

    private val binding: ActivityAddNewTaskBinding by lazy {
        ActivityAddNewTaskBinding.inflate(layoutInflater)
    }
    private val taskViewModel: TaskViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    var currentUserID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // for current user Status
        sessionManager = SessionManager(this)
        currentUserID = sessionManager.getUserId()


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