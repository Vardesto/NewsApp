package com.example.newsapp.data.models

data class ArticleResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)