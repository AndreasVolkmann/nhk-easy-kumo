package util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by Av on 5/1/2017.
 */
internal class ErrorHandlerTest {

    @Test
    fun handleTest() {
        val text = "This is to be expected"
        ErrorHandler.handle(Exception(text))

    }

}