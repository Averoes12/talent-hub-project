package com.daff.sesi2_hz.ir.daff.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daff.sesi2_hz.ir.daff.R
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.databinding.ItemNewsBinding
import com.daff.sesi2_hz.ir.daff.databinding.ItemPagingBinding
import com.daff.sesi2_hz.ir.daff.presentation.home.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(val viewModel: HomeViewModel,val itemClick: (Article) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var news = ArrayList<Article>()

	override fun getItemViewType(position: Int): Int {
		return if(viewModel.isLastPage.value == false && position == news.size){
			R.layout.item_paging
		}else {
			R.layout.item_news
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			R.layout.item_news -> {
				val itemBinding = ItemNewsBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false
				)
				ViewHolder(itemBinding)
			}
			else -> {
				val binding = ItemPagingBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false
				)
				PaginateItem(binding)
			}
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is ViewHolder) {
			holder.bind(news[position])
		}
	}

	override fun getItemCount(): Int = news.size

	inner class ViewHolder(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SimpleDateFormat")
		fun bind(news: Article){

			news.url.let {
				binding.root.setOnClickListener { itemClick(news) }
			}
			val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
			val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
			val date = inputDateFormat.parse(news.publishedAt)
			
			Picasso.get()
				.load(news.urlToImage)
				.error(R.drawable.ic_broken_image)
				.into(binding.imageNews)
			
			binding.newsTitle.text = news.title
			binding.newsPublisher.text = news.author
			binding.newsDate.text = date?.let { outputDateFormat.format(it) }
			
		}
	}
	inner class PaginateItem(private val binding: ItemPagingBinding) : RecyclerView.ViewHolder(binding.root)

	@SuppressLint("NotifyDataSetChanged")
	fun setNewsList(newsList: List<Article>){
		news.clear()
		news.addAll(newsList)
		notifyDataSetChanged()
	}
}