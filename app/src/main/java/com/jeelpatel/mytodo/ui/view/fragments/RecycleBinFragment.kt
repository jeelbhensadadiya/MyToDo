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
import com.jeelpatel.mytodo.databinding.FragmentRecycleBinBinding
import com.jeelpatel.mytodo.ui.adapter.RecycleBinTaskAdapter
import com.jeelpatel.mytodo.ui.viewModel.TaskUiState
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecycleBinFragment : Fragment() {

    private var _binding: FragmentRecycleBinBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var recyclerBinAdapter: RecycleBinTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleBinBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // default fetch
        viewModel.getAllDeletedTask()


        // adapter
        recyclerBinAdapter = RecycleBinTaskAdapter(
            requireContext(),
            onRestore = { taskId ->
                viewModel.restoreTask(taskId)
            },
            onTaskClick = { task ->
                val action =
                    RecycleBinFragmentDirections.actionRecycleBinFragmentToTaskViewFragment(
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
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskRecyclerView.adapter = recyclerBinAdapter

        // collect flow data
        dataCollector()

    }

    private fun dataCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiStates.collectLatest { uiStates ->
                        when (uiStates) {
                            is TaskUiState.Error -> {
                                UiHelper.showToast(requireContext(), uiStates.message)
                            }

                            is TaskUiState.Ideal -> {}
                            is TaskUiState.Loading -> {}
                            is TaskUiState.Success -> {
                                recyclerBinAdapter.submitList(uiStates.tasks)
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