package me.avo.kumo.nhk.processing

import me.avo.kumo.nhk.data.*
import java.io.*

class ArticleTagger(private val tokenizer: ArticleTokenizer, file: File) {

    private val wv by lazy { Word2VecFix.loadGoogleBinaryModel(file, false) }

    val categories = listOf(
        Category("社会", "事件", "事故", "裁判", "気象", "地震", "話題", "皇室"),
        Category("政治", "選挙", "行政"),
        Category("経済", "企業", "産業", "財政", "マーケット", "スタートアップ"),
        Category("国際", "アジア", "オセアニア", "北米", "欧州", "中東", "アフリカ", "中南米", "ヨーロッパ"),
        Category("サイエンス", "医療", "科学", "技術", "環境"),
        Category("スポーツ", "野球", "サッカー", "ラグビー", "テニス", "競馬", "ゴルフ", "相撲"),
        Category("教育", "受験", "学校", "大学", "学ぶ", "学生")
    )

    fun tag(article: Article): Article = tokenizer
        .tokenize(article.body)
        .flatMap { token -> evaluateCategory(token) }
        .groupAverage()
        .sortedByDescending { it.second }
        .filterIndexed { index, (_, similarity) -> index == 0 || similarity > 0.1 }
        .take(2)
        .map { it.first }
        .map(Category::label)
        .let { article.copy(tags = it) }

    fun evaluateCategory(token: String): List<Pair<Category, Double>> = categories
        .flatMap { it.calcCategorySimilarity(token) }
        .groupAverage()

    private fun <T> List<Pair<T, Double>>.groupAverage(): List<Pair<T, Double>> = this
        .groupBy { it.first } // group by category
        .mapValues { it.value.map { it.second } } // map out the categories from the value
        .map { it.key to it.value.average() } // value to average -> List

    private fun Category.calcCategorySimilarity(token: String) = allContestants.map { this to it.getSimilarity(token) }

    private fun String.getSimilarity(token: String) = wv.similarity(token, this)

    private val Article.body get() = "${title}。$content"

}