package com.example.newsapp.viewmodels.newslist

sealed class PageState {
    object Top : PageState()
    object Everything : PageState()
}