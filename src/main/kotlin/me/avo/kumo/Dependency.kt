package me.avo.kumo

import com.github.salomonbrys.kodein.*
import me.avo.kumo.nhk.*
import me.avo.kumo.nhk.persistence.*

val kodein = Kodein {
    val url = "jdbc:sqlite:D:\\Dev\\data\\sqlite\\nhk-easy"
    val driver = "org.sqlite.JDBC"

    bind<String>("url") with singleton { url }
    bind<String>("driver") with singleton { driver }
    bind<NhkDatabase>() with singleton { NhkSqlDatabase(url, driver) }

}