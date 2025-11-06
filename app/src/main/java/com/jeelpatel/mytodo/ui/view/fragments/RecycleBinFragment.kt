package com.jeelpatel.mytodo.ui.view.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeelpatel.mytodo.databinding.FragmentRecycleBinBinding
import com.jeelpatel.mytodo.ui.adapter.RecycleBinTaskAdapter
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecycleBinFragment : Fragment() {

    private var _binding: FragmentRecycleBinBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var recyclerBinAdapter: RecycleBinTaskAdapter
    private lateinit var sessionManager: SessionManager
    private var currentUserID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecycleBinBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // for get current User ID and Status
        sessionManager = SessionManager(requireContext())
        currentUserID = sessionManager.getUserId()


        // default fetch
        taskViewModel.getAllDeletedTask(currentUserID)


        // adapter
        recyclerBinAdapter = RecycleBinTaskAdapter(
            requireContext(),
            onRestore = { taskId ->
                taskViewModel.restoreTask(taskId)
            },
            onTaskClick = { task ->
                val action = RecycleBinFragmentDirections.actionRecycleBinFragmentToTaskViewFragment(
                    task.title,
                    task.description ?: "No - Description",
                    task.dueDate,
                    task.priority,
                    task.isCompleted
                )
                findNavController().navigate(action)
            }
        )
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = recyclerBinAdapter

        // collect flow data
        dataCollector()

    }

    private fun dataCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    taskViewModel.task.collect {
                        recyclerBinAdapter.submitList(it)
                    }
                }

                launch {
                    taskViewModel.message.collect {
                        toast(it)
                    }
                }
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}