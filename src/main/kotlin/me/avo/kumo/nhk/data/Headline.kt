package me.avo.kumo.nhk.data

import org.joda.time.DateTime

data class Headline(
    val id: String,
    val title: String,
    val date: DateTime,
    val url: String
)