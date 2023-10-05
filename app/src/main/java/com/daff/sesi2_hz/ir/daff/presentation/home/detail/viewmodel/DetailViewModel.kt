package com.daff.sesi2_hz.ir.daff.presentation.home.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.data.repo.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DetailViewModel @Inject
constructor(): ViewModel(), CoroutineScope {

  @Inject
  lateinit var repository: Repository

  private val job = Job()
  override val coroutineContext: CoroutineContext
    get() = job + Dispatchers.Main

  private var _saveArticleState = MutableLiveData<String>()
  val saveArticleState: LiveData<String>
    get() = _saveArticleState

  private var _unFavoriteState = MutableLiveData<String>()
  val unFavoriteState: LiveData<String>
    get() = _unFavoriteState

  fun saveArticle(article: Article) {
    launch {
      val result = withContext(Dispatchers.IO) {
        article.isFavorite = true
        repository.saveArticle(article)
      }
      _saveArticleState.value = result
    }
  }

  fun setUnFavorite(id: Long) {
    launch {
      val result = withContext(Dispatchers.IO) {
        repository.setUnFavorite(id, false)
      }
      _unFavoriteState.value = result
    }
  }

}