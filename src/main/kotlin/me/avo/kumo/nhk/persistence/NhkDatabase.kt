package me.avo.kumo.nhk.persistence

import me.avo.kumo.nhk.data.*

interface NhkDatabase {

    fun saveArticles(articles: List<Article>)

    fun updateArticle(article: Article)

    fun filterImported(articles: List<Article>): List<Article>

}