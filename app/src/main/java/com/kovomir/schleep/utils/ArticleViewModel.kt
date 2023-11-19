package com.kovomir.schleep.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ArticleViewModel(var articles: List<Article>) {
    var selectedArticle: Article? by mutableStateOf(articles[0])

    fun selectArticle(article: Article) {
        selectedArticle = article
    }
}