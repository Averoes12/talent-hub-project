package com.daff.sesi2_hz.ir.daff.data.db

import android.content.SharedPreferences
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.utils.AppEnvironment.ConstKey.PREF_EMAIL
import com.daff.sesi2_hz.ir.daff.utils.AppEnvironment.ConstKey.PREF_USERNAME
import com.daff.sesi2_hz.ir.daff.utils.SharedPreferencesFactory.clearPref

class LocalData(private val pref: SharedPreferences, private val db: AppDatabase) {

//  pref
  fun saveUserLogin(email: String, username: String): String{
    return try {
      pref.edit().putString(PREF_EMAIL, email).apply()
      pref.edit().putString(PREF_USERNAME, username).apply()
      "success"
    }catch (e: Exception){
      e.localizedMessage ?: ""
    }
  }

  fun getUserLogin(): String = pref.getString(PREF_EMAIL, "") ?: ""
  fun getUsername(): String = pref.getString(PREF_USERNAME, "") ?: ""

  fun logout(): String{
    return try {
      pref.clearPref()
      "success"
    }catch (e: Exception){
      e.localizedMessage ?: ""
    }
  }

  // Room
  fun saveArticle(article: Article): String {
    return try {
      db.articleDao()
        .insertArticle(article)
      return "success"
    } catch (e: Exception) {
      e.localizedMessage ?: "failed"
    }
  }

  fun saveArticles(articles: List<Article>): String {
    return try {
      db.articleDao()
        .insertArticles(articles)
      return "success"
    } catch (e: Exception) {
      e.localizedMessage ?: "failed"
    }
  }

  fun getArticles(isFavorite: Boolean): List<Article>? {
    return try {
      db.articleDao()
        .getArticles(isFavorite)
    } catch (e: Exception) {
      null
    }
  }

  fun deleteFavorite(id: Long, isFavorite: Boolean): String {
    return try {
      db.articleDao()
        .deleteFavorite(id, isFavorite)
      return "success"
    } catch (e: Exception) {
      e.localizedMessage ?: "failed"
    }
  }

}