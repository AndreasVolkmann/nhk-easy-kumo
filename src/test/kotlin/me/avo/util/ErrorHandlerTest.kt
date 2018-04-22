package me.avo.util

import me.avo.kumo.util.ErrorHandler
import org.junit.jupiter.api.Test

internal class ErrorHandlerTest {

    @Test fun handleTest() {
        val text = "This is to be expected"
        ErrorHandler.handle(Exception(text))
    }

}