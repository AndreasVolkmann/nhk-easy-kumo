package me.avo.kumo.nhk.data

data class Category(
    val label: String,
    val keywords: List<String>
) {

    constructor(label: String, vararg keywords: String): this(label, keywords.toList())

    val allContestants: List<String> = keywords + label

}