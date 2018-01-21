package me.avo.kumo.nhk.processing

import me.avo.kumo.nhk.data.*
import java.io.*

class ArticleTagger(private val tokenizer: ArticleTokenizer, file: File) {

    private val wv by lazy { Word2VecFix.loadGoogleBinaryModel(file, false) }

    fun tag(article: Article): Article = tokenizer
            .tokenize(article.body)
            .associate { it to wv.wordsNearest(it, 200) }
            //.onEach { println(it) }
            .flatMap { it.value }
            .let { it.distinct().map { word -> word to it.count { it == word } } }
            .sortedByDescending { it.second }
            .take(5)
            .onEach(::println)
            .map { it.first }
            .let { article.copy(tags = it) }

    private val Article.body get() = "${title}。$content"

}