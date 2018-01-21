package me.avo.kumo.nhk

import com.github.salomonbrys.kodein.*
import me.avo.kumo.*
import org.junit.jupiter.api.*

internal class NhkSqlDatabaseTest {

    @Test
    fun connect() {

        NhkSqlDatabase(
                kodein.instance("url"),
                kodein.instance("driver"))

    }

}