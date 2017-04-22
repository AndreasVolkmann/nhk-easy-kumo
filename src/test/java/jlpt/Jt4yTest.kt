package jlpt

import lingq.LingqApi
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import storage.JlptMongo
import java.net.URLDecoder.decode
import java.net.URLEncoder.encode

/**
 * Created by Av on 4/22/2017.
 */
internal class Jt4yTest {


    @Test
    fun connect() {
        val lessons = Jt4y(fromFile = true).get()
        println("Found ${lessons.size} lessons")


        lessons.forEach {
            JlptMongo.saveLesson(it)
        }

        //lessons.forEach { LingqApi.postLesson(it) }


    }

    @Test
    fun encoding() {
        val url = "http://japanesetest4you.com/flashcard/learn-jlpt-n2-grammar-%e3%81%9a%e3%81%ab%e3%81%99%e3%82%80-zu-ni-sumu/"
        println(url)

        val res = encode(url, "UTF-8")
        println(res)

        val uni = "%e3%81%9a%e3%81%ab%e3%81%99%e3%82%80"

        println(uni)
        println(encode(uni, "UTF-8"))


        decode(uni, "UTF-8").let { println(it) }
        println(decode(res, "UTF-8"))


        val mid = url.substringAfter("grammar-").substringBefore("-")
        val start = url.substringBefore(mid)
        val end = url.substringAfter(mid)
        val final = start + decode(mid, "UTF-8") + end


        (start + mid + end) shouldEqualTo url

        println(final)

    }

}