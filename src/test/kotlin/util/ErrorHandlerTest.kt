package util

import org.junit.jupiter.api.Test

internal class ErrorHandlerTest {

    @Test
    fun handleTest() {
        val text = "This is to be expected"
        ErrorHandler.handle(Exception(text))
    }

}