package com.example.newsapp.viewmodels.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {

    private val _downloadState: MutableStateFlow<DownloadState> = MutableStateFlow(DownloadState.Loading)
    val downloadState = _downloadState.asStateFlow()

    private var pageState: PageState = PageState.Top

    private var currentPage = 1

    private var currentKeyWords = ""

    //TEST
    var country: String = "us"
        private set

    fun changeCurrentCountry(value: String){
        if (value == country)
            return
        country = value
        getNewsTop()
    }

    fun getNewsTop(){
        pageState = PageState.Top
        currentPage = 1
        viewModelScope.launch {
            _downloadState.value = DownloadState.Loading
            val response = newsRepository.getNewsTop(country = country)
            if (response.isSuccessful){
                if (response.body() != null){
                    _downloadState.value = DownloadState.Success(response.body()!!)
                } else {
                    _downloadState.value = DownloadState.Nothing
                }
            } else {
                _downloadState.value = DownloadState.Error(response.body()!!.status)
            }
        }
    }

    fun getNewsEverything(keyWords: String){
        pageState = PageState.Everything
        currentPage = 1
        currentKeyWords = keyWords
        viewModelScope.launch {
            _downloadState.value = DownloadState.Loading
            val response = newsRepository.getNewsEverything(keyWords = keyWords)
            if (response.isSuccessful){
                if (response.body() != null){
                    _downloadState.value = DownloadState.Success(response.body()!!)
                } else {
                    _downloadState.value = DownloadState.Nothing
                }
            } else {
                _downloadState.value = DownloadState.Error(response.body()!!.status)
            }
        }
    }

    fun updateNews(){
        when(pageState){
            is PageState.Everything -> addNewsEverything()
            is PageState.Top -> addNewsTop()
        }
    }

    private fun addNewsTop(){
        currentPage++
        viewModelScope.launch {
            val response = newsRepository.getNewsTop(country = country, page = currentPage)
            if (response.isSuccessful){
                if (response.body() != null){
                    _downloadState.value = DownloadState.SuccessAdd(response.body()!!)
                } else {
                    _downloadState.value = DownloadState.Nothing
                }
            } else {
                _downloadState.value = DownloadState.Error(response.body()!!.status)
            }
        }
    }

    private fun addNewsEverything(){
        currentPage++
        viewModelScope.launch {
            val response = newsRepository.getNewsEverything(keyWords = currentKeyWords, page = currentPage)
            if (response.isSuccessful){
                if (response.body() != null){
                    _downloadState.value = DownloadState.SuccessAdd(response.body()!!)
                } else {
                    _downloadState.value = DownloadState.Nothing
                }
            } else {
                _downloadState.value = DownloadState.Error(response.errorBody().toString())
            }
        }
    }

    fun returnToTopNews(): Boolean{
        return when(pageState){
            is PageState.Top -> {
                false
            }
            is PageState.Everything -> {
                getNewsTop()
                true
            }
        }
    }

}