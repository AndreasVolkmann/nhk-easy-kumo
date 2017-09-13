package me.avo.storage

import me.avo.kumo.nhk.NhkMongo
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

internal class NhkMongoTest {

    companion object {

        @AfterAll
        @JvmStatic
        fun tearDown() {
            NhkMongo {
                deleteMany(Document())
            }
        }
    }


    @Test
    fun connect() {
        insert()
        val articles = NhkMongo.loadArticles()
        articles.size shouldBeGreaterOrEqualTo 1
        NhkMongo.updateArticle(articles.first())
        articles.forEach(::println)
    }

    fun insert() {
        NhkMongo.saveArticles()
    }

}