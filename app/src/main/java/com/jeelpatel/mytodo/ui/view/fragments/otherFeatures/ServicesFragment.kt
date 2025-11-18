package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jeelpatel.mytodo.databinding.FragmentServicesBinding
import com.jeelpatel.mytodo.service.ServiceExample


class ServicesFragment : Fragment() {


    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startServiceBtn.setOnClickListener {
            ContextCompat.startForegroundService(
                requireContext(),
                Intent(requireContext(), ServiceExample::class.java)
            )
        }

        binding.stopServiceBtn.setOnClickListener {
            requireContext().stopService(Intent(requireContext(), ServiceExample::class.java))
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}