package me.avo.util

import me.avo.kumo.util.makeDate
import org.amshove.kluent.shouldEqual
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

class UtilsTest {

    @Test fun makeDateTest() {
        val given = "[2月17日 15時50分]"
        val result = makeDate(given)

        val currentYear = DateTime.now().year
        result shouldEqual DateTime.parse("$currentYear-02-17")
    }

}