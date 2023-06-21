package ru.hse.newsreader.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.hse.newsreader.R
import ru.hse.newsreader.databinding.FragmentSourceBinding
import ru.hse.newsreader.ui.viewmodels.MainViewModel
import ru.hse.newsreader.ui.viewmodels.SourceViewModel

class SourceFragment : Fragment(R.layout.fragment_source) {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var sourceViewModel: SourceViewModel
    private lateinit var binding: FragmentSourceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSourceBinding.bind(view)
        super.onViewCreated(binding.root, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sourceViewModel = ViewModelProvider(requireActivity())[SourceViewModel::class.java]
        mainViewModel.currentSource.value?.let {
            sourceViewModel.source = it
        }
        mainViewModel.clearCurrentSource()

        applySource()
    }

    private fun applySource() {
        sourceViewModel.source?.let { source ->
            binding.apply {
                tvLocation.text = "Location: ${source.location}"
                tvWeather.text = "Weather: ${source.weather}"
                tvTemperature.text = "Temperature: ${source.temperature}"
                tvWind.text = "Wind speed: ${source.wind}"
                tvPressure.text = "Pressure: ${source.pressure}"
                tvSeaLevel.text = "Sea level: ${source.seaLevel}"
            }
        }
    }


}