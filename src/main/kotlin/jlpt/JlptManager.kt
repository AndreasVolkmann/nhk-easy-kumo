package jlpt

import lingq.LingqApi
import storage.JlptMongo

object JlptManager {

    val regex = Regex("[a-zA-Z]")

    fun getImportedTitles() = LingqApi.getLessons(language = "ja", collection = 274307)

    fun postLessons(amount: Int) = getImportedTitles().let { importedTitles ->
        JlptMongo.loadLessons()
                .filter { it.text.isNotBlank() }
                .filterNot { it.title in importedTitles }
                .filterNot { it.text.contains(regex) }
                .take(amount)
                .also { "Found ${it.size} / $amount lessons to post" }
                .map { it.copy(external_audio = "http://www.freesfx.co.uk/rx2/mp3s/6/18660_1464810669.mp3", duration = 1) }
                .forEach { LingqApi.postLesson(it) }
    }


}