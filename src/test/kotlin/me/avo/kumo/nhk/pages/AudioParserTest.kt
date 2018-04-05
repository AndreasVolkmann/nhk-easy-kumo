package me.avo.kumo.nhk.pages

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AudioParserTest {

    @Test fun run() {
        AudioParser("k10011389831000").run()
    }

}