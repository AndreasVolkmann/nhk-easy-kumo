package me.avo.kumo.nhk.persistence

import me.avo.kumo.nhk.data.Article
import me.avo.kumo.nhk.persistence.NhkSqlDatabase.*
import me.avo.testKodein
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContain
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NhkSqlDatabaseTest : KodeinAware {

    override val kodein: Kodein = testKodein

    private val db = NhkSqlDatabase(
        direct.instance("url"),
        direct.instance("driver")
    )

    @BeforeAll fun beforeAll() = transaction {
        val tables = arrayOf(Ignore, Tags, Articles)
        SchemaUtils.drop(*tables)
        SchemaUtils.create(*tables)
    }

    @Test fun connect() {
        db
    }

    @Test fun insert() {
        val article = makeArticle()
        val articles = listOf(article)
        with(db) {
            saveArticles(articles)
            filterImportedOrIgnored(articles) shouldContain article
            updateArticle(article) // should succeed
            filterImportedOrIgnored(articles).shouldBeEmpty()
        }
    }

    @Test fun ignored() {
        val article = makeArticle()
        val articles = listOf(article)
        with(db) {
            saveArticles(articles)
            ignoreArticle(article.id)
            filterImportedOrIgnored(articles).shouldBeEmpty()
        }
    }

    private fun makeArticle(imported: Boolean = false) = Article(
        idCounter++.toString(),
        "",
        "test",
        DateTime(),
        "",
        null,
        null,
        File(""),
        null,
        File(""),
        listOf("test-tag"),
        imported
    )

    private var idCounter = 0

}