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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentMainBinding
import com.jeelpatel.mytodo.ui.adapter.TaskAdapter
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val taskViewModel: TaskViewModel by viewModels()
    private var currentUserID = 0

    private lateinit var taskAdapter: TaskAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        currentUserID = sessionManager.getUserId()

        if (!sessionManager.isLoggedIn()) {
            findNavController().navigate(R.id.action_mainFragment_to_signUpFragment)
        }

        taskAdapter = TaskAdapter(
            requireContext(),
            onStatusChange = { taskId, isCompleted ->
                taskViewModel.updateTaskStatus(taskId, isCompleted)
            },
            onDeleted = { taskId ->
                taskViewModel.deleteTask(taskId)
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
        taskViewModel.getAllTask(currentUserID)
        binding.buttonGroup.check(R.id.allTaskFilterBtn)

        binding.onlineTodosBtn.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRemoteTodoFragment())
        }

        binding.buttonGroup.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.allTaskFilterBtn -> taskViewModel.getAllTask(currentUserID)
                R.id.overDueTaskFilterBtn -> taskViewModel.overDueTask(currentUserID)
                R.id.completedTaskFilterBtn -> taskViewModel.completedTask(currentUserID)
                R.id.pendingTaskFilterBtn -> taskViewModel.pendingTask(currentUserID)
            }
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
                launch {
                    taskViewModel.task.collectLatest {
                        taskAdapter.submitList(it)
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