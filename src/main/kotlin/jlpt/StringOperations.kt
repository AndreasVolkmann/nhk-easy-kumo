package jlpt

/**
 * Created by Av on 4/23/2017.
 */
val encoder = Charsets.ISO_8859_1.newEncoder()!!

val endOfSentence = listOf('？', '。', '！')
fun String.fixEndOfSentence() = if (this.isBlank() || this.last() in endOfSentence) this else "$this。"

val illegal = listOf("―", "’", "“", "”", "…", "–", "　")
fun String.removeIllegalChars() = illegal.fold(this) { acc, s -> acc.replace(s, "") }

fun String.removeNonJap() = this.dropLastWhile { encoder.canEncode(it) }

fun CharSequence.encodeable() = encoder.canEncode(this)
fun Char.encodeable() = encoder.canEncode(this)