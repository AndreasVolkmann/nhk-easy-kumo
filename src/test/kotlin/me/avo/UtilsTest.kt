package me.avo

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import me.avo.kumo.util.makeDate


class UtilsTest {

    @Test
    fun makeDateTest() {
        val given = "[2月17日 15時50分]"
        val result = makeDate(given)

        result shouldEqual "2017-02-17"
    }

}