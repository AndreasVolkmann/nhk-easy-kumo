import org.jsoup.nodes.Element
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