package com.daff.sesi2_hz.ir.daff.data.model.article


import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    val articles: List<Article>?,
    val status: String?,
    val totalResults: Int?
)