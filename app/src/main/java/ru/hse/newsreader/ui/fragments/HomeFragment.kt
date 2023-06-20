package ru.hse.newsreader.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.hse.newsreader.R
import ru.hse.newsreader.adapters.SourceAdapter
import ru.hse.newsreader.databinding.FragmentHomeBinding
import ru.hse.newsreader.other.Status
import ru.hse.newsreader.ui.viewmodels.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private val sourceAdapter = SourceAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHomeBinding.bind(view)
        super.onViewCreated(binding.root, savedInstanceState)

        setupRecyclerView()
        subscribeToObservers()

        homeViewModel.loadNewSource(requireContext())
    }

    private fun setupRecyclerView() = binding.rvAllSources.apply {
        adapter = sourceAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >= 0) {
                    homeViewModel.loadNewSource(requireContext())
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