package me.avo.douwa

import me.avo.kumo.other.douwa.Douwa
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
internal class DouwaTest {

    @Test fun load() {
        val hanashi = Douwa().load()
        println(hanashi)
    }

}