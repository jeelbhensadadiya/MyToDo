package com.jeelpatel.mytodo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentTaskViewBinding
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TaskViewFragment : Fragment() {

    private var _binding: FragmentTaskViewBinding? = null
    private val binding get() = _binding!!
    private val args: TaskViewFragmentArgs by navArgs()

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formattedDate = SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.getDefault())
            .format(Date(args.dueDate))

        with(binding) {
            taskTitleTv.text = args.title
            taskDescriptionTv.text = args.desc
            taskStatusChip.text = if (args.isCompleted) "Completed" else "Pending"

            when (args.priority) {
                1 -> taskPriorityCard.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.p1)
                )

                2 -> taskPriorityCard.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.p2)
                )

                3 -> taskPriorityCard.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.p3)
                )
            }

            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                taskViewModel.updateTaskStatus(args.taskId, isChecked)
            }

            deleteTaskBtn.setOnClickListener {
                taskViewModel.deleteTask(args.taskId)
            }

            taskDueDateTv.text = "Due : $formattedDate"
        }

        dataCollectors()
    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    taskViewModel.message.collect {
                        toast(it)
                    }
                }
            }
        }
    }

    private fun toast(msg: String) {

        val bottomNav =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)

        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
            .setAnchorView(bottomNav)
            .setAction("Done") {}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}