package com.daff.sesi2_hz.ir.daff.presentation.home.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daff.sesi2_hz.ir.daff.data.model.BaseResponse
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.data.repo.Repository
import com.daff.sesi2_hz.ir.daff.utils.extension.toArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class HomeViewModel @Inject
constructor() : ViewModel(), CoroutineScope {

	@Inject
	lateinit var repository: Repository

	private val job = Job()
	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Main

	private var _isLastPage = MutableLiveData<Boolean>()
	val isLastPage: LiveData<Boolean>
		get() = _isLastPage

	private var _topHeadlines = MutableLiveData<BaseResponse<List<Article>>>()
	val topHeadlines: LiveData<BaseResponse<List<Article>>>
		get() = _topHeadlines
	private var allTopHeadlines = ArrayList<Article>()

	private var _logoutState = MutableLiveData<String>()
	val logoutState: LiveData<String>
		get() = _logoutState

	private var _modeOffline = MutableLiveData<Boolean>()
	val modeOffline: LiveData<Boolean>
		get() = _modeOffline

	fun saveArticles(articles: List<Article>) {
		launch {
			val result = withContext(Dispatchers.IO){
				repository.saveArticles(articles)
			}
		}
	}

	private fun getTopHeadlinesLocal() {
		launch {
			val result = withContext(Dispatchers.IO) {
				repository.getArticles(false)
			}
			allTopHeadlines.addAll(result.toArrayList())
			_topHeadlines.value = BaseResponse.Success(allTopHeadlines)
			_modeOffline.value = true
		}
	}

	private fun getTopHeadlinesRemote(page: Int, pageSize: Int) {
		if (page == 1) {
			_topHeadlines.value = BaseResponse.Loading
			allTopHeadlines.clear()
		}

		launch {
			val result = withContext(Dispatchers.IO) {
				repository.getTopHeadlines(page, pageSize)
			}
			when (result) {
				is BaseResponse.Loading -> {}
				is BaseResponse.Success -> {
					allTopHeadlines.addAll(result.data.articles.toArrayList())
					_topHeadlines.value = BaseResponse.Success(allTopHeadlines)
					_isLastPage.value = result.data.articles?.size != pageSize

					saveArticles(allTopHeadlines)
				}

				is BaseResponse.Error -> {
					_topHeadlines.value = result
				}
			}

		}
	}

	fun getEverything(page: Int, pageSize: Int, query: String) {
		if (page == 1) {
			_topHeadlines.value = BaseResponse.Loading
			allTopHeadlines.clear()
		}

		launch {
			val result = withContext(Dispatchers.IO) {
				repository.getEverything(query, page, pageSize)
			}
			when (result) {
				is BaseResponse.Loading -> {}
				is BaseResponse.Success -> {
					allTopHeadlines.addAll(result.data.articles.toArrayList())
					_topHeadlines.value = BaseResponse.Success(allTopHeadlines)
					_isLastPage.value = result.data.articles?.size != pageSize
				}

				is BaseResponse.Error -> {}
			}

		}
	}

	fun getTopHeadlines(context: Context, page: Int, pageSize: Int) {
		if (isInternetAvailable(context)) {
			getTopHeadlinesRemote(page, pageSize)
		} else {
			getTopHeadlinesLocal()
		}
	}


	private fun isInternetAvailable(context: Context): Boolean {
		val connectivityManager =
			context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}


}