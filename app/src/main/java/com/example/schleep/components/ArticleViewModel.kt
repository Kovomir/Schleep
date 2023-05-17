package com.example.schleep.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ArticleViewModel(articles: List<Article>) {
    var articles: List<Article> = articles
    var selectedArticle: Article? by mutableStateOf(articles[0])

    fun selectArticle(article: Article) {
        selectedArticle = article
    }
}