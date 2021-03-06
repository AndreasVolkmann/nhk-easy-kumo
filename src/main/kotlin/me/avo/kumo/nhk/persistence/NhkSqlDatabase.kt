package me.avo.kumo.nhk.persistence

import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.data.ArticleException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class NhkSqlDatabase(url: String, driver: String) : NhkDatabase {

    object Articles : Table("articles") {
        val id = varchar("id", 20).primaryKey().uniqueIndex()
        val url = varchar("url", 254)
        val title = varchar("title", 254)
        val date = datetime("date")
        val content = text("content")
        val audioUrl = varchar("audio_url", 254).nullable()
        val imported = bool("imported")
    }

    object Tags : Table("article_tag") {
        val articleId = varchar("article_id", 20) references Articles.id
        val tag = varchar("tag", 50)

        init {
            uniqueIndex(articleId, tag)
        }
    }

    object Ignore : Table("ignore") {
        val articledId = varchar("article_id", 20) references Articles.id
    }

    init {
        initialize(url, driver)
    }

    private fun initialize(url: String, driver: String) {
        Database.connect(url, driver)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    override fun saveArticles(articles: List<Article>) = transaction {
        val alreadyExists = Articles
            .slice(Articles.id)
            .select { Articles.id inList articles.map(Article::id) }
            .map { it[Articles.id] }

        articles
            .filterNot { alreadyExists.contains(it.id) }
            .forEach(this@NhkSqlDatabase::insertArticle)
    }

    private fun insertArticle(article: Article) = try {
        Articles.insert {
            it[id] = article.id
            it[url] = article.url
            it[title] = article.title
            it[date] = article.date
            it[content] = article.content
            it[audioUrl] = article.audioUrl
            it[imported] = article.imported
        }
        article.tags.forEach { t -> insertTag(article.id, t) }
    } catch (ex: Exception) {
        throw ArticleException(article, ex)
    }

    private fun insertTag(articleId: String, tag: String) = Tags.insert {
        it[this.articleId] = articleId
        it[this.tag] = tag
    }

    override fun updateArticle(article: Article): Unit = transaction {
        Articles.update({ Articles.id eq article.id }) {
            it[imported] = true
        }
    }

    override fun filterImportedOrIgnored(articles: List<Article>): List<Article> {
        val ids = articles.map(Article::id)
        val alreadyImported = transaction {
            (Articles leftJoin Ignore)
                .slice(Articles.id)
                .select {
                    Articles.id inList ids and (Articles.imported eq true or Ignore.articledId.isNotNull())
                }
                .map { it[Articles.id] }
        }
        return articles.filter { it.id !in alreadyImported }
    }

    override fun ignoreArticle(articleId: String): Unit = transaction {
        Ignore.insert {
            it[this.articledId] = articleId
        }
    }

}