package storage

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import util.print

/**
 * Created by Av on 4/22/2017.
 */
internal class JlptMongoTest {

    @Test
    fun loadLessons() { // TODO fix these cases
        val regex = Regex("[a-zA-Z]")
        "test he".contains(regex).shouldBeTrue()


        val lessons = JlptMongo.loadLessons()
        println("Contains illegal")
        fun String.isIllegal() = contains(regex)
        val illegal = lessons.filter { it.text.isIllegal() }.print()


        println("Is blank")
        val empty = lessons.filter { it.text.isBlank() }.print()


        val exclude = illegal + empty
        exclude.shouldBeEmpty()

        /*
        illegal
                .map { it.text.split("。")}
                .map { it.map { it.removeIllegalChars().removeNonJap()}.joinToString("") }
                .filter(String::isIllegal)
                .onEach(::println)
                .shouldBeEmpty()
        */
        /*
        lessons.filterNot { it in exclude }.forEach {
            LingqApi.postLesson(it)
        }*/
    }




}