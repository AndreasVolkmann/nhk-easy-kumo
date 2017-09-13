package me.avo.kumo.jlpt

val encoder = Charsets.ISO_8859_1.newEncoder()!!

val endOfSentence = listOf('？', '。', '！')
fun String.fixEndOfSentence() = if (this.isBlank() || this.last() in endOfSentence) this else "$this。"

val illegal = listOf("―", "’", "“", "”", "…", "–", "　")
fun String.removeIllegalChars() = illegal.fold(this) { acc, s -> acc.replace(s, "") }

fun String.removeNonJap() = dropLastWhile(Char::encodeable)

fun CharSequence.encodeable() = encoder.canEncode(this)
fun Char.encodeable() = encoder.canEncode(this)