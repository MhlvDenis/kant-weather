package ru.hse.newsreader.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.hse.newsreader.R
import ru.hse.newsreader.adapters.SourceAdapter
import ru.hse.newsreader.databinding.FragmentHomeBinding
import ru.hse.newsreader.other.Status
import ru.hse.newsreader.ui.viewmodels.HomeViewModel
import ru.hse.newsreader.ui.viewmodels.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var mainViewModel: MainViewModel
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private val sourceAdapter = SourceAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHomeBinding.bind(view)
        super.onViewCreated(binding.root, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        subscribeToObservers()

        repeat (10) {
            homeViewModel.loadNewSource(requireContext())
        }
    }

    private fun setupRecyclerView() = binding.rvAllSources.apply {
        adapter = sourceAdapter.apply {
            setItemClickListener {  source ->
                mainViewModel.postCurrentSource(source)
                Log.d("Click", "source is set")
            }
        }
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    repeat (5) {
                        homeViewModel.loadNewSource(requireContext())
                    }
                }
            }
        })
    }

    private fun subscribeToObservers() {
        homeViewModel.sourceItems.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    result.data?.let { sources ->
                        sourceAdapter.sources = sources
                    }
                    hideLoading()
                }
                Status.ERROR -> {
                    hideLoading()
                    Toast.makeText(requireActivity(), "Failed to load new item", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    showLoading()
                }
            }
            sourceAdapter.notifyDataSetChanged()
        }
    }

    private fun hideLoading() {
        binding.allSourcesProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.allSourcesProgressBar.visibility = View.VISIBLE
    }
}