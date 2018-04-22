package me.avo.kumo.nhk.persistence

import com.github.salomonbrys.kodein.*
import me.avo.kumo.*
import me.avo.kumo.nhk.data.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import java.io.File

internal class NhkSqlDatabaseTest {

    @Test
    fun `filter should remove already imported`() {

        val art =
            Article("k10011294871000", "", "", "", "", ByteArray(0), null, File(""), "", mock(), listOf(), false)

        val db = kodein.instance<NhkDatabase>()

        db.filterImported(listOf(art)).shouldNotContain(art)

    }

}