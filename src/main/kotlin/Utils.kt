import org.jsoup.nodes.Element
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val sdf = SimpleDateFormat("yyyy-MM-dd")

val currentDate: String get() = sdf.format(Date())

fun Element.getText() = this.html()
        .replace(Regex("(?s)(<rt>.*?)(?:(?:\r*\n){2}|</rt>)"), "")
        .replace(Regex("(?s)(<span.*?)(?:(?:\r*\n){2}|>)"), "")
        .replace(Regex("(?s)(<a.*?)(?:(?:\r*\n){2}|>)"), "")
        .replace("<ruby>", "")
        .replace("</ruby>", "")
        .replace("</span>", "")
        .replace("<p>", "")
        .replace("</p>", "")
        .replace("</a>", "")
        .replace(Regex("( )+"), "")

fun Element.getUrl() = makeUrl(this.attr("href"))

fun makeUrl(part: String) = Application.mainUrl.removeSuffix("/") + part.removePrefix(".")


const val gatsu = "月"
const val nichi = "日"

fun Element.getDate() = makeDate(this.getElementsByClass("newsDate").first().text())

fun makeDate(text: String): String {
    val stripped = text.removeSurrounding("[", "]").substringBefore(nichi)

    val year = currentDate.substring(0, 4)
    val month = stripped.substringBefore(gatsu).addZero()
    val day = stripped.substringAfter(gatsu).addZero()

    return "$year-$month-$day"
}

fun String.addZero() = if (this.length == 1) "0$this" else this


fun getIdFromUrl(url: String) = url.substringAfterLast("/").substringBefore(".html")


fun File.notExists() = !this.exists()

fun File.writeIfNotExists(bytes: ByteArray) {
    if (this.notExists()) this.writeBytes(bytes)
}

fun File.writeIfNotExists(text: String) {
    if (this.notExists()) this.writeBytes(text.toByteArray())
}