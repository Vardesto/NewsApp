package com.example.newsapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.data.models.Article
import com.example.newsapp.databinding.ItemNewsListBinding

class NewsListAdapter (private val context: Context, private val updateFun: () -> Unit): RecyclerView.Adapter<NewsListAdapter.NewsListItemViewHolder>() {

    private var articles: List<Article> = emptyList()

    fun updateList(newList: List<Article>){
        val diffUtil = NewsListDiffUtils(articles, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        articles = newList
        result.dispatchUpdatesTo(this)
    }

    fun addArticles(newList: List<Article>) = updateList(articles + newList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListItemViewHolder {
        val binding = ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holderItem: NewsListItemViewHolder, position: Int) {
        holderItem.bind(articles[position])
        if (position == articles.lastIndex){
            updateFun()
        }
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
            binding.root.setOnLongClickListener {
                val uri = Uri.parse(article.url)
                val urlIntent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(urlIntent)
                true
            }
        }

    }

}