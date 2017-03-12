package storage

import LingqTest
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.junit.jupiter.api.Test

/**
 * Created by Av on 3/12/2017.
 */
internal class MongoTest {

    @Test
    fun connect() {


        val articles = Mongo.loadArticles()
        articles.size shouldBeGreaterOrEqualTo 1
        Mongo.updateArticle(articles.first().id)
        articles.forEach(::println)
    }

    fun insert() {
        val art = LingqTest.article
        Mongo.saveArticles(art)
    }

}