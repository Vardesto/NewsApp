package com.example.newsapp.app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsListFragment: Fragment(R.layout.fragment_news_list) {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private val newsListViewModel: NewsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewsListBinding.bind(view)

        val adapter = NewsListAdapter(requireContext()) { newsListViewModel.updateNews() }

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
                    is DownloadState.SuccessAdd -> {
                        adapter.addArticles(it.response.articles)
                    }
                }
            }
        }

        binding.refresh.setOnRefreshListener {
            newsListViewModel.updateNews()
            binding.refresh.isRefreshing = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_news_list, menu)
        val searchView = (menu.findItem(R.id.search).actionView) as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query != ""){
                    (activity as AppCompatActivity).supportActionBar?.title = query
                    newsListViewModel.getNewsEverything(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.country -> {
                val dialog = CountrySelectDialog(newsListViewModel)
                dialog.show(childFragmentManager, CountrySelectDialog.DIALOG_TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (newsListViewModel.returnToTopNews()){
                    (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
                }

            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

}