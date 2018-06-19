package me.avo.util

import me.avo.kumo.util.ErrorHandler
import me.avo.testKodein
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance

internal class ErrorHandlerTest {

    private val handler by testKodein.instance<ErrorHandler>()

    @Disabled
    @Test fun handleTest() {
        val text = "This is to be expected"
        val func = { handler.handle(Exception(text)) }
        func shouldThrow Exception::class
    }

}