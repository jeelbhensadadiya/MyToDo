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
import com.jeelpatel.mytodo.databinding.FragmentCreateTaskBinding
import com.jeelpatel.mytodo.ui.viewModel.CreateTaskUiState
import com.jeelpatel.mytodo.ui.viewModel.taskViewModel.CreateTaskViewModel
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateTaskViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // show date time picker
        binding.taskDueDateEt.setOnClickListener {
            UiHelper.showMaterialDateTimePicker(parentFragmentManager) {
                binding.taskDueDateEt.setText(it)
            }
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
                    viewModel.uiState.collectLatest { uiState ->
                        when (uiState) {
                            is CreateTaskUiState.Ideal -> {}
                            is CreateTaskUiState.Loading -> {}
                            is CreateTaskUiState.Success -> {
                                findNavController().popBackStack()
                            }

                            is CreateTaskUiState.Error -> {
                                UiHelper.showToast(requireContext(), uiState.message)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null //  clear binding
    }
}