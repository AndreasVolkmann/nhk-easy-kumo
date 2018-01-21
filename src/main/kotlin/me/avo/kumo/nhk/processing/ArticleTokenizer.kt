package me.avo.kumo.nhk.processing

import com.atilika.kuromoji.ipadic.*

class ArticleTokenizer(includeVerbs: Boolean) {

    private val tokenizer = Tokenizer()

    private val filters = listOf<(Token) -> Boolean>(
            { it.partOfSpeechLevel1 == "名詞" || (includeVerbs && it.partOfSpeechLevel1 == "動詞") },
            //{ it.partOfSpeechLevel1 !in listOf("接続詞", "記号") },
            { it.partOfSpeechLevel2 == "一般" || it.partOfSpeechLevel2 == "自立" }
    )

    fun tokenize(input: String): List<String> = tokenizer
            .tokenize(input)
            .filter { token -> filters.all { it.invoke(token) } }
            .map { it.baseForm }

    fun analyzeWord(word: String) = tokenizer.tokenize(word).first().let {
        println(it)
        println(it.partOfSpeechLevel1)
        println(it.partOfSpeechLevel2)
        println(it.partOfSpeechLevel3)
        println(it.partOfSpeechLevel4)
    }

}