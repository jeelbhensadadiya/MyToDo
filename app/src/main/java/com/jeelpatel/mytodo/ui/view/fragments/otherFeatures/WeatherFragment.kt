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
import com.bumptech.glide.Glide
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentWeatherBinding
import com.jeelpatel.mytodo.domain.model.WeatherResponseModel
import com.jeelpatel.mytodo.ui.viewModel.WeatherUiState
import com.jeelpatel.mytodo.ui.viewModel.weatherViewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WeatherFragment : Fragment() {


    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getCurrentWeatherBtn.setOnClickListener {
            viewModel.getCurrentWeather("Ahmedabad")
        }

        dataCollector()
    }

    private fun dataCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherUiState.collectLatest { weatherUiState ->
                    when (weatherUiState) {
                        is WeatherUiState.Ideal -> {

                        }

                        is WeatherUiState.Loading -> {

                        }

                        is WeatherUiState.Error -> {
                            Toast.makeText(
                                requireContext(), weatherUiState.message, Toast.LENGTH_SHORT
                            ).show()
                        }

                        is WeatherUiState.Success -> {
                            printWeatherValuesOnScreen(weatherUiState.weatherResponseModel)
                        }
                    }
                }
            }
        }
    }

    private fun printWeatherValuesOnScreen(weatherResponse: WeatherResponseModel) {
        binding.tvCityName.text = weatherResponse.location.name
        binding.tvLocalTime.text = weatherResponse.location.localtime
        binding.tvLocalTime.text = weatherResponse.location.localtime


        binding.tvTemperature.text = weatherResponse.current.temp_c.toString() + " °C"
        binding.tvConditionText.text = weatherResponse.current.condition.text

        binding.tvWind.text = weatherResponse.current.wind_kph.toString() + " km/h"
        binding.tvHumidity.text = weatherResponse.current.humidity.toString() + " %"
        binding.tvPressure.text = weatherResponse.current.pressure_mb.toString() + " mb"
        binding.tvUV.text = weatherResponse.current.uv.toString()
        binding.tvVisibility.text = weatherResponse.current.vis_km.toString() + " km"
        binding.tvCloud.text = weatherResponse.current.cloud.toString() + " %"

        Glide.with(binding.root)
            .load("https:${weatherResponse.current.condition.icon}")
            .placeholder(R.drawable.dummy_image)
            .into(binding.imgCondition)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}