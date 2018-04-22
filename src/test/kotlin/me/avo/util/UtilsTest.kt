package me.avo.util

import me.avo.kumo.util.makeDate
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test


class UtilsTest {

    @Test fun makeDateTest() {
        val given = "[2月17日 15時50分]"
        val result = makeDate(given)

        result shouldEqual "2017-02-17"
    }

}