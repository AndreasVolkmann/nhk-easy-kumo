package me.avo.kumo

import com.github.salomonbrys.kodein.*
import me.avo.kumo.nhk.persistence.*
import me.avo.kumo.nhk.processing.*
import java.io.*

val kodein = Kodein {
    val url = "jdbc:sqlite:D:\\Dev\\data\\sqlite\\nhk-easy"
    val driver = "org.sqlite.JDBC"
    val includeVerbs = false

    bind<String>("url") with singleton { url }
    bind<String>("driver") with singleton { driver }
    bind<NhkDatabase>() with singleton { NhkSqlDatabase(url, driver) }
    bind<File>() with singleton { File("D:\\Dev\\data\\jp\\model.bin.gz") }
    bind<ArticleTokenizer>() with singleton { ArticleTokenizer(includeVerbs) }
    bind<ArticleTagger>() with singleton { ArticleTagger(instance(), instance()) }

}