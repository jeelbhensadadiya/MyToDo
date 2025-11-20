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
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentTaskViewBinding
import com.jeelpatel.mytodo.ui.viewModel.TaskUiState
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskViewFragment : Fragment() {

    private var _binding: FragmentTaskViewBinding? = null
    private val binding get() = _binding!!
    private val args: TaskViewFragmentArgs by navArgs()
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            taskTitleTv.text = args.title
            taskDescriptionTv.text = args.desc
            taskStatusChip.text = if (args.isCompleted) {
                requireContext().getString(R.string.completed)
            } else {
                requireContext().getString(R.string.pending)
            }
            taskCheckBox.isChecked = args.isCompleted

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
                viewModel.updateTaskStatus(args.taskId, isChecked)
            }

            deleteTaskBtn.setOnClickListener {
                viewModel.deleteTask(args.taskId)
            }

            taskDueDateTv.text = getString(R.string.due, UiHelper.formatDate(args.dueDate))
        }

        dataCollectors()
    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiStates.collectLatest { uiStates ->
                        when (uiStates) {
                            is TaskUiState.Ideal -> {}
                            is TaskUiState.Loading -> {}
                            is TaskUiState.Success -> {}
                            is TaskUiState.Error -> {
                                UiHelper.showToast(requireContext(), uiStates.message)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}