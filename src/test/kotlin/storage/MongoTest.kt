package storage

import LingqTest
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

/**
 * Created by Av on 3/12/2017.
 */
internal class MongoTest {

    companion object {

        @AfterAll
        @JvmStatic
        fun tearDown() {
            Mongo {
                deleteMany(Document())
            }
        }
    }


    @Test
    fun connect() {
        insert()
        val articles = Mongo.loadArticles()
        articles.size shouldBeGreaterOrEqualTo 1
        Mongo.updateArticle(articles.first())
        articles.forEach(::println)
    }

    fun insert() {
        val art = LingqTest.article
        Mongo.saveArticles(art)
    }

}