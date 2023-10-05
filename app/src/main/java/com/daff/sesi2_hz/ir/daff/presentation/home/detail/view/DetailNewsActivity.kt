package com.daff.sesi2_hz.ir.daff.presentation.home.detail.view

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daff.sesi2_hz.ir.daff.R
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.databinding.ActivityDetailNewsBinding
import com.daff.sesi2_hz.ir.daff.di.DIModule
import com.daff.sesi2_hz.ir.daff.di.DaggerDIComponent
import com.daff.sesi2_hz.ir.daff.presentation.home.detail.viewmodel.DetailViewModel
import com.daff.sesi2_hz.ir.daff.utils.extension.onClick
import com.daff.sesi2_hz.ir.daff.utils.extension.showToast
import javax.inject.Inject

class DetailNewsActivity : AppCompatActivity() {
	
	lateinit var binding: ActivityDetailNewsBinding

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var detailViewModel: DetailViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailNewsBinding.inflate(layoutInflater)
		setContentView(binding.root)

		DaggerDIComponent.builder()
			.dIModule(DIModule(this))
			.build().inject(this)

		detailViewModel = ViewModelProvider(
			this,
			viewModelFactory
		)[DetailViewModel::class.java]

		val news = intent.extras?.getParcelable("news") ?: Article()

		binding.dtlNews.apply {
			settings.apply {
				javaScriptEnabled = true
				loadWithOverviewMode = true
				loadsImagesAutomatically = true
			}

			webViewClient = WebViewClient()
		}

		if (news.url.isNullOrBlank()) {
			binding.containerError.isVisible = true
			binding.dtlNews.isVisible = false
			binding.tvErrorTitle.text = getString(R.string.news_title)
		} else {
			binding.containerError.isVisible = false
			binding.dtlNews.isVisible = true
			binding.dtlNews.loadUrl(news.url!!)
		}

		setFavoriteIcon(news.isFavorite)


		binding.btnFav.onClick {
			if (news.isFavorite == true) {
				detailViewModel.setUnFavorite(news.id)
			} else {
				detailViewModel.saveArticle(news)
			}
		}

		detailViewModel.saveArticleState.observe(this, Observer {
			showToast("Added to Favorite")
			setFavoriteIcon(true)
		})

		detailViewModel.unFavoriteState.observe(this, Observer {
			showToast("Remove from Favorite")
			setFavoriteIcon(false)
		})

	}

	private fun setFavoriteIcon(isFavorite: Boolean?) {
		if (isFavorite == true) {
			binding.btnFav.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite))
		} else {
			binding.btnFav.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unfavorite))
		}
	}
}