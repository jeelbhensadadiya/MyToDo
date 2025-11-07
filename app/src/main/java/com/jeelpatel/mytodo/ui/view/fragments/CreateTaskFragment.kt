package com.jeelpatel.mytodo.ui.view.fragments

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.CreateTaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateTaskViewModel by viewModels()

    @Inject
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
        currentUserID = sessionManager.getUserId()


        // show date time picker
        binding.taskDueDateEt.setOnClickListener {
            showMaterialDateTimePicker()
        }


        // create task
        binding.createNewTaskBtn.setOnClickListener {
            viewModel.createNewTask(
                title = binding.taskTitleEt.text.toString(),
                description = binding.taskDescriptionEt.text.toString(),
                priority = binding.prioritySlider.value.toInt(),
                dueDate = binding.taskDueDateEt.text.toString(),
                isCompleted = false
            )
        }


        // collect flow data
        dataCollector()
    }

    private fun dataCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.message.collect {
                        // show errors
                        UiHelper.showSnackWithBottomNav(binding.root, msg = it)
                    }
                }


                launch {
                    viewModel.isTaskCreated.collect { isCreated ->
                        // navigate to back if task created successfully
                        if (isCreated) {
                            findNavController().popBackStack()
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

            timePicker.show(parentFragmentManager, "M3_TIME_PICKER")
        }

        datePicker.show(parentFragmentManager, "M3_DATE_PICKER")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null //  clear binding
    }
}