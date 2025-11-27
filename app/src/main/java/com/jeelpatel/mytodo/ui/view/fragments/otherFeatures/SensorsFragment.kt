package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.R
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeelpatel.mytodo.databinding.FragmentSensorsBinding

class SensorsFragment : Fragment() {


    private var _binding: FragmentSensorsBinding? = null
    private val binding get() = _binding!!


    private lateinit var sensorsManager: SensorManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSensorsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorsManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager


        val sensors = sensorsManager.getSensorList(Sensor.TYPE_ALL)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_1,
            sensors.map { "${it.name} \n ${it.vendor} \n ${it.power}" }
        )

        binding.sensorsListView.adapter = adapter

        binding.gyroscopeBtn.setOnClickListener {
            findNavController().navigate(SensorsFragmentDirections.actionSensorsFragmentToGyroscopeFragment())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding
    }

}