package com.daff.sesi2_hz.ir.daff.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daff.sesi2_hz.ir.daff.data.model.article.Article

@Dao
interface ArticleDao {
  @Insert
  fun insertArticle(data: Article)
  @Insert
  fun insertArticles(data: List<Article>)
  @Query("UPDATE article SET isfavorite = :isFavorite WHERE id= :id")
  fun deleteFavorite(id: Long, isFavorite: Boolean)
  @Query("DELETE FROM article WHERE id == :id")
  fun deleteArticle(id:Long)
  @Query("SELECT * FROM article WHERE isfavorite == :isFavorite")
  fun getArticles(isFavorite: Boolean): List<Article>

}