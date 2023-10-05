package com.daff.sesi2_hz.ir.daff.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daff.sesi2_hz.ir.daff.data.model.article.Article
import com.daff.sesi2_hz.ir.daff.utils.AppEnvironment.DB_APP

@Database(entities = [Article::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
  abstract fun articleDao(): ArticleDao

  companion object {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          DB_APP
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}