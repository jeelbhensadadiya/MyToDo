package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.databinding.FragmentWorkManagerBinding
import com.jeelpatel.mytodo.ui.viewModel.WorkInfoState
import com.jeelpatel.mytodo.ui.viewModel.workerViewModel.WorkManagerExampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkManagerFragment : Fragment() {


    private var _binding: FragmentWorkManagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkManagerExampleViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkManagerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.startWorkManagerBtn.setOnClickListener {
            viewModel.startWorkManager()
        }

        dataCollector()

    }

    private fun dataCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.workInfoState.collectLatest { workInfoState ->
                        when (workInfoState) {
                            WorkInfoState.IDEAL -> {
                                binding.showStateTv.text = "IDEAL"
                            }

                            WorkInfoState.ENQUEUED -> {
                                binding.showStateTv.text = "ENQUEUED"

                            }

                            WorkInfoState.RUNNING -> {
                                binding.showStateTv.text = "RUNNING"

                            }

                            WorkInfoState.SUCCEEDED -> {
                                binding.showStateTv.text = "SUCCEEDED"

                            }

                            WorkInfoState.CANCELLED -> {
                                binding.showStateTv.text = "CANCELLED"

                            }

                            WorkInfoState.FAILED -> {
                                binding.showStateTv.text = "FAILED"

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