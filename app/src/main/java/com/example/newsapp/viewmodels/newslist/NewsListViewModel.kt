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

    //TEST
    private val country: String = "us"

    fun getNewsTop(){
        viewModelScope.launch {
            _downloadState.value = DownloadState.Loading
            val response = newsRepository.getNewsTop(country)
            if (response.isSuccessful){
                if (response.body() != null){
                    _downloadState.value = DownloadState.Success(response.body()!!)
                } else {
                    _downloadState.value = DownloadState.Nothing
                }
            } else {
                _downloadState.value = DownloadState.Error(response.message())
            }
        }
    }
}