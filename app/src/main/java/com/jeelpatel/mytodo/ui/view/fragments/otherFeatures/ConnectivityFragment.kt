package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeelpatel.mytodo.databinding.FragmentConnectivityBinding

class ConnectivityFragment : Fragment() {


    private var _binding: FragmentConnectivityBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectivityBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wifiBtn.setOnClickListener {
            findNavController().navigate(ConnectivityFragmentDirections.actionConnectivityFragmentToWifiFragment())
        }

        binding.bluetoothBtn.setOnClickListener {
            findNavController().navigate(ConnectivityFragmentDirections.actionConnectivityFragmentToBluetoothFragment())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}