package me.avo.kumo.nhk.persistence

import me.avo.kumo.nhk.data.Article

interface NhkDatabase {

    /**
     * Persists the [articles] to the database
     */
    fun saveArticles(articles: List<Article>)

    /**
     * Updates the [article] so that the imported flag is true
     */
    fun updateArticle(article: Article)

    /**
     * Checks the list of [articles] and filters out any [Article] that has already been imported or is ignored
     */
    fun filterImportedOrIgnored(articles: List<Article>): List<Article>

    /**
     * Add the [articleId] to the ignore list
     */
    fun ignoreArticle(articleId: String)

}