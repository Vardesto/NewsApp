package com.example.newsapp.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsListAdapter
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.viewmodels.newslist.DownloadState
import com.example.newsapp.viewmodels.newslist.NewsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsListFragment: Fragment(R.layout.fragment_news_list) {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var adapter: NewsListAdapter

    private val newsListViewModel: NewsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewsListBinding.bind(view)

        //val adapter = NewsListAdapter()
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

        newsListViewModel.getNewsTop()

        lifecycleScope.launch {
            newsListViewModel.downloadState.collectLatest {
                when(it){
                    is DownloadState.Success -> {
                        adapter.updateList(it.response.articles)
                        binding.progressBar.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE
                    }
                    is DownloadState.Nothing -> Log.e("Nothing", "Nothing")
                    is DownloadState.Error -> Log.e("Error", it.message)
                    is DownloadState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
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