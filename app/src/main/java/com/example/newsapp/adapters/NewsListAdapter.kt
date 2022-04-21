package com.example.newsapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.databinding.ItemNewsListBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NewsListAdapter @Inject constructor(@ApplicationContext private val context: Context): RecyclerView.Adapter<NewsListAdapter.NewsListItemViewHolder>() {

    private var articles: List<Article> = emptyList()

    fun updateList(newList: List<Article>){
        val diffUtil = NewsListDiffUtils(articles, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        articles = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListItemViewHolder {
        val binding = ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holderItem: NewsListItemViewHolder, position: Int) {
        holderItem.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsListItemViewHolder(private val binding: ItemNewsListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(article: Article){
            binding.author.text = article.author
            binding.description.text = article.description
            binding.title.text = article.title
            Glide.with(context)
                .load(article.urlToImage)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(binding.image)
        }

    }

}