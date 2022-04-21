package com.example.newsapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.newsapp.data.models.Article

class NewsListDiffUtils(private val oldList: List<Article>, private val newList: List<Article>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return when{
            oldItem.author != newItem.author -> false
            oldItem.content != newItem.content -> false
            oldItem.description != newItem.description -> false
            oldItem.publishedAt != newItem.publishedAt -> false
            oldItem.source != newItem.source -> false
            oldItem.title != newItem.title -> false
            oldItem.url != newItem.url -> false
            oldItem.urlToImage != newItem.urlToImage -> false
            else -> true
        }
    }

}