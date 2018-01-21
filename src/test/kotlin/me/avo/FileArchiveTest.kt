package me.avo

import org.amshove.kluent.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import me.avo.kumo.nhk.FileArchive

internal class FileArchiveTest {

    @Test
    fun readarchive() {

        val headlines = FileArchive(TODO()).read()

        headlines.forEach(::println)
        headlines.size shouldBeGreaterThan 1

    }

}