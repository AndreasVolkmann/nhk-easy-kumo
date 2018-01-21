package me.avo.kumo.jlpt

import me.avo.kumo.other.jlpt.*
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test

class Jt4yResourceTest {


    @Test fun extract() {
        val resource = Resource.File("Learn JLPT N5 Grammar_ の (no) – 1 – Japanesetest4you.com.html")


        Jt4yLesson(resource).extractAudio(resource.load()).let {
            it.first() shouldEqualTo "http://japanesetest4you.com/audio/n5g-no1-1.mp3?_=1"
            println(it)

        }



    }

}