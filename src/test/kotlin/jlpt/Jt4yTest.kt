package jlpt

import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotEqualTo
import org.jsoup.Jsoup
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import storage.JlptMongo
import util.loadResource
import java.net.URLDecoder.decode
import java.net.URLEncoder.encode

internal class Jt4yTest {

    @Test @Disabled
    fun connect() {
        val lessons = Jt4y(Level.N5, fromFile = true).get()
        println("Found ${lessons.size} lessons")

        lessons.forEach {
            JlptMongo.saveLesson(it)
        }
    }

    @Test
    fun contentTest() = checkContent("ContentTest.html", listOf(
            "彼女の笑顔にはどこか寂しげなところがあった。",
            "彼女は苦しげにうめいて横たわった。",
            "学生たちは楽しげにおしゃべりしていた。",
            "「大丈夫？」彼は訝しげにもう一度聞いた。",
            "老人が、何か言いたげに近づいて来た。",
            "山田は片手で物憂げに髪を掻いた。",
            "子供たちは庭でいかにも楽しげに遊んでいる。",
            "「そうか」彼は考え深げに言った。"
    ))

    @Test
    fun includeFirstExample() = checkContent("FirstLineTest.html", lines = listOf(
            "君は慎み深い人だ。彼が気に入っているだけのことはある。",
            "なるほど彼はすぐれた美男であった。ヨーロッパじゅうに美男の名をとどろかしただけのことはある。"
    ))

    @Test
    fun excludeForm() = checkContent("FormTest.html", listOf(
            "今日中にファックス、あるいは、メールで送ってください。",
            "あるいは、あなたの言うとおりかもしれません。",
            "その男は不思議に思われるほど何も知らなかった。あるいは、話したくないのかもしれなかった。",
            "来週の月曜日の午前はどうですか。あるいは火曜日の午後でもかまいませんが。",
            "人間が未来を予想できないということは、あるいはいいことかもしれない。",
            "彼女はわかっていた。あるいは、わかっていると思っていた。"))

    @Test
    fun advertisingBeforeForm() = checkContent("AdvertisingTest.html", lines = listOf(
            "鈴木さんは字がきれいなだけでなく文章も上手だ。",
            "あの歌手は歌が上手なだけでなく、自分で曲も作る。"
    ))

    fun checkContent(fileName: String, lines: List<String>) {
        val resource = Resource.File(fileName)
        val html = this::class.loadResource(fileName)
        val doc = Jsoup.parse(html)
        val content = Jt4yLesson(resource).extractContent(doc)
        content shouldEqualTo lines.joinToString("")
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

    @Test
    fun removeNonJap() {
        val input = "どうせダメなんだなどと言っていては、何もできなくなるよ。 dou se dame nanda nado to itte ite wa, nanimo dekinakunaru yo. If you keep saying things like “it wont work anyway”"
        val res = input.removeIllegalChars().removeNonJap()
        res shouldEqualTo "どうせダメなんだなどと言っていては、何もできなくなるよ。"
    }

    @Test
    fun removeIllegal() {
        val input = "test―“”"
        val res = input.removeIllegalChars()
        res shouldEqualTo "test"
        "–" shouldNotEqualTo "―"
    }

    @Test
    fun canEncode() {
        val term = "."
        term.encodeable().shouldBeTrue()
    }

    @Test
    fun endOfSentence() {
        val noEnd = "test"
        val res = noEnd.fixEndOfSentence()
        res shouldEqualTo "$noEnd。"
    }

}