package me.avo.util

import com.github.salomonbrys.kodein.instance
import me.avo.kumo.util.ErrorHandler
import me.avo.testKodein
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class ErrorHandlerTest {

    private val handler = testKodein.instance<ErrorHandler>()

    @Test fun handleTest() {
        val text = "This is to be expected"
        val func = { handler.handle(Exception(text)) }
        func shouldThrow Exception::class
    }

}