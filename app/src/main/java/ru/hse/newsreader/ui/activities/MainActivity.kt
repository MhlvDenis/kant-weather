package ru.hse.newsreader.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.hse.newsreader.R
import ru.hse.newsreader.databinding.ActivityMainBinding
import ru.hse.newsreader.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.currentSource.observe(this) {
            if (it != null) {
                Log.d("Click", "in observer")
                binding.navHostFragment.getFragment<Fragment>().findNavController().navigate(
                    R.id.globalActionToSourceFragment
                )
            }
        }
    }
}