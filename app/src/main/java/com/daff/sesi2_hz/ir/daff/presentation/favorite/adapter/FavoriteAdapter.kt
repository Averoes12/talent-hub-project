package com.daff.sesi2_hz.ir.daff.presentation.favorite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daff.sesi2_hz.ir.daff.R
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteAdapter(
  private val itemClick: (Article) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

  private var favorite = ArrayList<Article>()
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
    val binding = ItemNewsBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )

    return FavoriteViewHolder(binding)
  }

  override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
    holder.bind(favorite[position])
  }

  override fun getItemCount(): Int = favorite.size

  fun setData(listArticle: List<Article>) {
    favorite.clear()
    favorite.addAll(listArticle)
    notifyDataSetChanged()
  }

  inner class FavoriteViewHolder(
    private val binding: ItemNewsBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(article: Article) {
      binding.root.setOnClickListener { itemClick(article) }
      val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
      val date = inputDateFormat.parse(article.publishedAt!!)

      Picasso.get()
        .load(article.urlToImage)
        .error(R.drawable.ic_broken_image)
        .into(binding.imageNews)
      binding.newsTitle.text = article.title
      binding.newsPublisher.text = article.author
      binding.newsDate.text = outputDateFormat.format(date)
    }
  }
}
