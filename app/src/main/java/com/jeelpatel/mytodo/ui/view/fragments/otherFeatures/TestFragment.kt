package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

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
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentTestBinding
import com.jeelpatel.mytodo.ui.viewModel.otherViewModel.ChannelViewModel
import kotlinx.coroutines.launch


class TestFragment : Fragment() {


    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChannelViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Collect channel events
        observeEvents()

        // Button Click
        binding.textView.setOnClickListener {
            viewModel.onButtonClicked()
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is ChannelViewModel.UiEvent.ShowToast -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        ChannelViewModel.UiEvent.NavigateToNextScreen -> {
                            findNavController().navigate(R.id.connectivityFragment)
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