package com.jeelpatel.mytodo.ui.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityAddNewTaskBinding
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

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
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // for current user Status
        sessionManager = SessionManager(this)
        currentUserID = sessionManager.getUserId()


        observeData()

        binding.taskDueDateEt.setOnClickListener() {
            showMaterialDateTimePicker()
        }

        binding.createNewTaskBtn.setOnClickListener {

            val title = binding.taskTitleEt

            if (title.text.toString().isEmpty()) {
                binding.taskTitleEt.error = "Title is Empty"
            } else {
                val formatter = SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.getDefault())
                val dateString = binding.taskDueDateEt.text.toString()

                val dueMillis = formatter.parse(dateString)?.time ?: 0L

                val task = TaskModel(
                    title = title.text.toString(),
                    description = binding.taskDescriptionEt.text.toString(),
                    priority = binding.prioritySlider.value.toInt(),
                    dueDate = dueMillis,
                    userOwnerId = currentUserID,
                    isCompleted = false
                )
                taskViewModel.createNewTask(task)
            }

        }

        binding.materialToolBar.setNavigationOnClickListener() {
            finish()
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

    private fun showMaterialDateTimePicker() {

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        datePicker.addOnPositiveButtonClickListener { date ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date

            // Time Picker
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)

                val formatter = SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.getDefault())
                binding.taskDueDateEt.setText(formatter.format(calendar.time))
            }

            timePicker.show(supportFragmentManager, "M3_TIME_PICKER")
        }

        datePicker.show(supportFragmentManager, "M3_DATE_PICKER")
    }

}