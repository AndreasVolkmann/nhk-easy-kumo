package me.avo.kumo

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import me.avo.kumo.nhk.persistence.FileArchive
import me.avo.kumo.nhk.persistence.NhkDatabase
import me.avo.kumo.nhk.persistence.NhkSqlDatabase
import me.avo.kumo.nhk.processing.ArticleTagger
import me.avo.kumo.nhk.processing.ArticleTokenizer
import java.io.File

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
    bind<FileArchive>() with singleton { FileArchive(instance()) }

}