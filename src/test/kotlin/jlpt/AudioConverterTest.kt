package jlpt

import org.junit.jupiter.api.Test

internal class AudioConverterTest {

    @Test fun join() {
        val name1 = "no-1"
        val url1 = "http://japanesetest4you.com/audio/n5g-no1-1.mp3?_=1"
        val name2 = "no-2"
        val url2 = "http://japanesetest4you.com/audio/n5g-no1-2.mp3?_=2"


        val one = AudioConverter.convert(name1, url1)
        val two = AudioConverter.convert(name2, url2)

        AudioConverter.join(one, two)

    }

}