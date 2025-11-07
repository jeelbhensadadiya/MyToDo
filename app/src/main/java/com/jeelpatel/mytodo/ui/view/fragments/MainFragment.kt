package com.jeelpatel.mytodo.ui.view.fragments

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
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentMainBinding
import com.jeelpatel.mytodo.ui.adapter.TaskAdapter
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.FilterTaskViewModel
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.RecycleTaskViewModel
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.UpdateTaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val filterViewModel: FilterTaskViewModel by viewModels()
    private val updateViewModel: UpdateTaskViewModel by viewModels()
    private val recyclerViewModel: RecycleTaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private var currentUserID = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserID = sessionManager.getUserId()

        if (!sessionManager.isLoggedIn()) {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSignUpFragment())
        }

        taskAdapter = TaskAdapter(
            requireContext(),
            onStatusChange = { taskId, isCompleted ->
                updateViewModel.updateTaskStatus(taskId, isCompleted)
            },
            onDeleted = { taskId ->
                recyclerViewModel.deleteTask(taskId)
            },
            onTaskClick = { task ->
                val action = MainFragmentDirections.actionMainFragmentToTaskViewFragment(
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

        // default Get all task
        filterViewModel.getAllTask(currentUserID)
        binding.buttonGroup.check(R.id.allTaskFilterBtn)

        binding.onlineTodosBtn.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRemoteTodoFragment())
        }

        binding.buttonGroup.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.allTaskFilterBtn -> filterViewModel.getAllTask(currentUserID)
                R.id.overDueTaskFilterBtn -> filterViewModel.overDueTask(currentUserID)
                R.id.completedTaskFilterBtn -> filterViewModel.completedTask(currentUserID)
                R.id.pendingTaskFilterBtn -> filterViewModel.pendingTask(currentUserID)
            }
        }

        dataCollectors()

    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    updateViewModel.message.collect {
                        UiHelper.showToast(requireContext(), it)
                    }
                }

                launch {
                    recyclerViewModel.message.collect {
                        UiHelper.showToast(requireContext(), it)
                    }
                }

                launch {
                    filterViewModel.message.collect {
                        UiHelper.showToast(requireContext(), it)
                    }
                }

                launch {
                    filterViewModel.task.collectLatest {
                        taskAdapter.submitList(it)
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