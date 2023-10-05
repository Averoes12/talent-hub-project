package com.daff.sesi2_hz.ir.daff.data.repo

import com.daff.sesi2_hz.ir.daff.data.db.LocalData
import com.daff.sesi2_hz.ir.daff.data.model.BaseResponse
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.data.model.article.ArticleResponse
import com.daff.sesi2_hz.ir.daff.network.APIClient

class Repository (private val localData: LocalData, private val apiClient: APIClient){
	
//	local
	fun saveUserLogin(email: String, username: String): String = localData.saveUserLogin(email, username)
	fun getUserLogin(): String = localData.getUserLogin()
	fun getUsername(): String = localData.getUsername()
	fun logout(): String = localData.logout()

	fun saveArticle(article: Article) = localData.saveArticle(article)
	fun saveArticles(articles: List<Article>) = localData.saveArticles(articles)
	fun getArticles(isFavorite: Boolean) = localData.getArticles(isFavorite)
	fun setUnFavorite(id: Long, isFavorite: Boolean) = localData.deleteFavorite(id, isFavorite)

//	remote

	suspend fun getTopHeadlines(page: Int, pageSize: Int): BaseResponse<ArticleResponse> = apiClient.getTopHeadlines(page, pageSize)
	suspend fun getEverything(query: String, page: Int, pageSize: Int): BaseResponse<ArticleResponse> = apiClient.getEverything(query, page, pageSize)

	companion object {
		private var instance: Repository? = null
		fun getInstance(localData: LocalData, apiClient: APIClient): Repository {
			return instance ?: synchronized(this) {
				instance ?: Repository(localData, apiClient).also { instance = it }
			}
		}

	}

}