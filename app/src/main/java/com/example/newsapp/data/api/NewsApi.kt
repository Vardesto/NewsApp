package com.example.newsapp.data.api

import com.example.newsapp.data.models.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    companion object{
        const val API_KEY = "185f57aebc8d4fa7b1d814a87ba3ffb9"
    }

    @GET("everything")
    suspend fun getNewsEverything(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("q") keyWords: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: Int
    ): Response<ArticleResponse>

    @GET("top-headlines")
    suspend fun getNewsTop(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("page") page: Int
    ): Response<ArticleResponse>
}