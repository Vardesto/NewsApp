package com.example.newsapp.data.repository

import com.example.newsapp.data.api.NewsApi
import com.example.newsapp.data.models.ArticleResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi) {

    suspend fun getNewsTop(country: String, category: String = "", page: Int = 1): Response<ArticleResponse> {
        return newsApi.getNewsTop(country = country, category = category, page = page)
    }

    suspend fun getNewsEverything(keyWords: String, sortBy: String = "publishedAt", page: Int = 1): Response<ArticleResponse>{
        return newsApi.getNewsEverything(keyWords = keyWords, sortBy = sortBy, page = page)
    }

}