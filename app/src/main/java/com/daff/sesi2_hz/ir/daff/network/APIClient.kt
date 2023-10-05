package com.daff.sesi2_hz.ir.daff.network

import com.daff.sesi2_hz.ir.daff.data.model.BaseResponse
import com.daff.sesi2_hz.ir.daff.data.model.article.ArticleResponse
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class APIClient(private val apiService: APIService) {

  suspend fun getTopHeadlines(page: Int, pageSize: Int): BaseResponse<ArticleResponse> {
    return try {
      val response = apiService.getTopHeadlines(page = page, pageSize = pageSize)
      BaseResponse.Success(response)
    } catch (e: Exception){
      getExceptionResponse(e)
    }
  }

  suspend fun getEverything(query: String, page: Int, pageSize: Int): BaseResponse<ArticleResponse>{
    return try {
      val response = apiService.getEverything(query = query, page = page, pageSize = pageSize)
      BaseResponse.Success(response)
    } catch (e: Exception){
      getExceptionResponse(e)
    }
  }

  private fun <T> getExceptionResponse(e: Exception): BaseResponse<T> {
    e.printStackTrace()
    when (e) {
      is HttpException -> {
        val code = e.code()
        var msg = e.message()
        val errorMessage: String?
        try {
          val jsonObj = e.response()?.errorBody()?.charStream()?.readText()
            ?.let { JSONObject(it) }
          errorMessage = jsonObj?.getString("message")
        } catch (exception: java.lang.Exception) {
          return when (exception) {
            is UnknownHostException -> BaseResponse.Error(
              code,
              "Connection server error: ${e.message}",
            )
            is SocketTimeoutException -> BaseResponse.Error(
              code,
              "Connection server error: ${e.message}",
            )
            else -> BaseResponse.Error(
              code,
              "Connection server error. errorcode : <b>$code</b>",
            )
          }
        }

        when (code) {
          504 -> {
            msg = errorMessage ?: "Error Response"
          }
          502, 404 -> {
            msg = errorMessage ?: "Error Connect or Resource Not Found"
          }
          400 -> {
            msg = errorMessage ?: "Bad Request"
          }
          401 -> {
            msg = errorMessage ?: "Not Authorized"
          }
          422 -> {
            msg = errorMessage ?: "Unprocessable Entity"
          }
        }
        return BaseResponse.Error(code, msg)
      }

      else -> return BaseResponse.Error(-1,  e.message ?: "Unknown error occurred")
    }
  }



}