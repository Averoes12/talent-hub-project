package com.daff.sesi2_hz.ir.daff.data.model

sealed class BaseResponse<out T> {
  data object Loading : BaseResponse<Nothing>()
  data class Success<out T>(val data: T) : BaseResponse<T>()
  data class Error(val code: Int, val message: String) : BaseResponse<Nothing>()
}
