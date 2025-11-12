package com.jeelpatel.mytodo.ui.view.fragments.filters

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeelpatel.mytodo.databinding.FragmentPendingTasksBinding
import com.jeelpatel.mytodo.ui.adapter.TaskAdapter
import com.jeelpatel.mytodo.ui.viewModel.TaskUiState
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PendingTasksFragment : Fragment() {


    private var _binding: FragmentPendingTasksBinding? = null
    private val binding get() = _binding!!


    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPendingTasksBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // setup adapter
        taskAdapter = TaskAdapter(
            requireContext(),
            onStatusChange = { taskId, isCompleted ->
                viewModel.updateTaskStatus(taskId, isCompleted)
            },
            onDeleted = { taskId ->
                viewModel.deleteTask(taskId)
            },
            onTaskClick = { task ->
                val action =
                    PendingTasksFragmentDirections.actionPendingTasksFragmentToTaskViewFragment(
                        task.taskId,
                        task.title,
                        task.description ?: "No - Description",
                        task.dueDate,
                        task.priority,
                        task.isCompleted
                    )
                findNavController().navigate(action)
            }
        )
        taskAdapter.setHasStableIds(true)
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = taskAdapter


        // default call
        viewModel.pendingTask()


        // data flow collector
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

                            is TaskUiState.Success -> {
                                taskAdapter.submitList(uiStates.tasks)
                            }

                            is TaskUiState.Error -> {
                                UiHelper.showToast(requireContext(), uiStates.message)
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}