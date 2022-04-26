package com.example.newsapp.viewmodels.newslist

import com.example.newsapp.data.models.ArticleResponse

sealed class DownloadState{
    data class Success(val response: ArticleResponse): DownloadState()
    data class SuccessAdd(val response: ArticleResponse): DownloadState()
    object Loading: DownloadState()
    object Nothing: DownloadState()
    data class Error(val message: String): DownloadState()
}
