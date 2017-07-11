package util

import javazoom.jl.decoder.Bitstream
import org.jsoup.nodes.Element
import java.io.BufferedInputStream
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

val sdf = SimpleDateFormat("yyyy-MM-dd")

val currentDate: String get() = sdf.format(Date())

fun Element.getContent() = getElementsByTag("p").html().getText()

fun String.getText() = this
        .replace(Regex("(?s)(<rt>.*?)(?:(?:\r*\n){2}|</rt>)"), "")
        .replace(Regex("(?s)(<span.*?)(?:(?:\r*\n){2}|>)"), "")
        .replace(Regex("(?s)(<a.*?)(?:(?:\r*\n){2}|>)"), "")
        .replace("<ruby>", "")
        .replace("</ruby>", "")
        .replace("</span>", "")
        .replace("<p>", "")
        .replace("</p>", "")
        .replace("</a>", "")
        .replace("<br>", " ")
        .replace(Regex("( )+"), "")
        .trimEnd('\n')

fun Element.getTitle() = html().getText()

fun Element.getUrl() = makeUrl(this.attr("href"))

fun makeUrl(part: String) = Crawler.mainUrl.removeSuffix("/") + part.removePrefix(".")


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

fun URL.read() = try {
    BufferedInputStream(this.openStream()).use {
        it.readBytes()
    }
} catch (ex: Exception) {
    ByteArray(0)
}

fun File.getDuration(): Long = this.inputStream().use {
    val stream = Bitstream(it)
    val h = stream.readFrame()
    val tn = it.channel.size()
    val ms = h.ms_per_frame()
    val bitrate = h.bitrate()
    val frame = h.calculate_framesize()
    println("Frame: $frame, ms: $ms, bitrate: $bitrate, channel: $tn, ${tn / 10000}" )
    tn / 10000
}