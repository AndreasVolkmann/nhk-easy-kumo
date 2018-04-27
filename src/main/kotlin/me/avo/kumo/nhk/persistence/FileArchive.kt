package me.avo.kumo.nhk.persistence

import me.avo.kumo.nhk.data.Article
import me.avo.kumo.util.writeIfNotExists

class FileArchive(private val database: NhkDatabase) {

    fun archive(articles: List<Article>) {
        articles.forEach {
            it.image?.let(it.imageFile::writeIfNotExists)
        }
        database.saveArticles(articles)
    }

}

