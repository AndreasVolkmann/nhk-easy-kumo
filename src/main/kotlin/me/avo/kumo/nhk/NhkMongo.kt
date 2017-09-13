package me.avo.kumo.nhk

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import me.avo.kumo.util.Mongo
import org.bson.Document

object NhkMongo : Mongo {

    override val database = "nhk"
    override val collection = if (System.getProperty("ENV") == "TEST") "test" else "articles"

    fun saveArticles(vararg articles: Article) = saveArticles(articles.toList())

    fun saveArticles(articles: List<Article>) = this {
        val docs = articles.map(Article::toDocument)
        docs.forEach {
            val id = it["id"].toString()
            findOneAndReplace(eq("id", id), it, FindOneAndReplaceOptions().upsert(true))
        }
    }

    fun updateArticle(article: Article): Document? = this {
        findOneAndUpdate(eq("id", article.id), Document("\$set", Document("imported", true)), FindOneAndUpdateOptions().upsert(true))
    }

    fun loadArticles(): List<Article> = this {
        find().toList().map(Document::toArticle)
    }

    fun filterImported(articles: List<Article>) = this {
        articles.filter {
            val match = find(eq("id", it.id)).filter { it["imported"] as Boolean }
            match.count() == 0 // no articles with this id found
        }
    }

    fun filterHeadlines(headlines: List<Headline>) = this {
        headlines.filter {
            val match = find(eq("id", it.id)).filter { it["imported"] as Boolean }
            match.count() == 0
        }
    }

}