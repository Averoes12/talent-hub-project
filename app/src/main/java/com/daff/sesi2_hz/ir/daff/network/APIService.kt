package com.daff.sesi2_hz.ir.daff.network

import com.daff.sesi2_hz.ir.daff.BuildConfig
import com.daff.sesi2_hz.ir.daff.constant.Constant
import com.daff.sesi2_hz.ir.daff.data.model.article.ArticleResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface APIService {

  @GET("top-headlines")
  suspend fun getTopHeadlines(
    @Query("country") country: String? = "us",
    @Query("pageSize") pageSize: Int = 20,
    @Query("page") page: Int = 1
  ): ArticleResponse

  @GET("everything")
  suspend fun getEverything(
    @Query("q") query: String,
    @Query("pageSize") pageSize: Int = 20,
    @Query("page") page: Int = 1
  ): ArticleResponse

  companion object Factory {

    private fun request(): Retrofit {
      val mLoggingInterceptor = HttpLoggingInterceptor()
      mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
      val contentType = "application/json".toMediaType()
      val json = Json {
        ignoreUnknownKeys = true
      }

      val client = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
          .addInterceptor { chain ->
            chain.proceed(chain.let {
              it.request().newBuilder()
                .header("X-Api-Key", Constant.API_KEY)
                .method(it.request().method, it.request().body)
                .build()
            })
          }.readTimeout(30, TimeUnit.SECONDS)
          .connectTimeout(30, TimeUnit.SECONDS)
          .addInterceptor(mLoggingInterceptor)
          .build()
      } else {
        OkHttpClient.Builder()
          .addInterceptor { chain ->
            chain.proceed(chain.let {
              it.request().newBuilder()
                .header("X-Api-Key", Constant.API_KEY)
                .method(it.request().method, it.request().body)
                .build()
            })
          }.readTimeout(30, TimeUnit.SECONDS)
          .connectTimeout(30, TimeUnit.SECONDS)
          .build()
      }

      return Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(client)
        .build()

    }

    fun <T> createService(service: Class<T>): T {
      return request().create(service)
    }
  }


}