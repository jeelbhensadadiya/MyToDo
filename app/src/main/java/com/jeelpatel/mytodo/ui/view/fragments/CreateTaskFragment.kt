package com.jeelpatel.mytodo.ui.view.fragments

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jeelpatel.mytodo.databinding.FragmentCreateTaskBinding
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel: TaskViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    var currentUserID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // for current user Status
        sessionManager = SessionManager(requireContext())
        currentUserID = sessionManager.getUserId()

        dataCollector()


        binding.taskDueDateEt.setOnClickListener {
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
    }

    private fun dataCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    taskViewModel.message.collect {
                        toast(it)
                    }
                }

                launch {
                    taskViewModel.isTaskCreated.collect { isCreated ->
                        if (isCreated) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
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

            timePicker.show(parentFragmentManager, "M3_TIME_PICKER")
        }

        datePicker.show(parentFragmentManager, "M3_DATE_PICKER")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}