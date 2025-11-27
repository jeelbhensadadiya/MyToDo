package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jeelpatel.mytodo.databinding.FragmentGyroscopeBinding

class GyroscopeFragment : Fragment() {

    private var _binding: FragmentGyroscopeBinding? = null
    private val binding get() = _binding!!
    private var mySensor: Sensor? = null
    private lateinit var sensorManager: SensorManager
    private val sensorListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            event ?: return

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            binding.rotationsTv.text = """
            X = $x
            Y = $y
            Z = $z
        """.trimIndent()

            // Visual rotation effect
            binding.gyroscopeBtn.rotation = z * 20     // amplify for better effect
            binding.gyroscopeBtn.rotationX = x * 20
            binding.gyroscopeBtn.rotationY = y * 20
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGyroscopeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        sensorManager.registerListener(
            sensorListener,
            mySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sensorManager.unregisterListener(sensorListener)
    }

}