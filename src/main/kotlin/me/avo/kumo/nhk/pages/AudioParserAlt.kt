package me.avo.kumo.nhk.pages

import com.comcast.viper.hlsparserj.AbstractPlaylist
import com.comcast.viper.hlsparserj.MasterPlaylist
import com.comcast.viper.hlsparserj.PlaylistFactory
import com.comcast.viper.hlsparserj.PlaylistVersion
import com.comcast.viper.hlsparserj.tags.UnparsedTag
import com.comcast.viper.hlsparserj.tags.master.StreamInf
import java.net.URL

class AudioParserAlt(id: String) {

    val url = "https://nhks-vh.akamaihd.net/i/news/easy/$id.mp4/master.m3u8"

    init {
        val playlist = parsePlaylist(url)
        inspect(playlist)
    }

    fun StreamInf.getInfo() =
        "Program ID: [$programId]; Bandwidth: [$bandwidth]; Codecs: [$codecs]; Resolution: [$resolution]; Audio: [$audio];"

    fun processMaster(playlist: MasterPlaylist) {
        playlist.variantStreams.map { it.getInfo() }.onEach(::println)
    }

    private fun Collection<UnparsedTag>.getTagByName(name: String) = single { it.tagName == name }

    fun getKeyTag(tags: List<UnparsedTag>) = tags
        .getTagByName("EXT-X-KEY")
        .also { println("${it.tagName}: ${it.attributes}") }
        .let { it.rawTag.substringAfter(it.tagName) }
        .also(::println)
        .removePrefix(":")
        .split(",")
        .let {
            val method = it[0].substringAfter("=")
            val uri = it[1].substringAfter("=").removeSurrounding("\"")
            method to uri
        }

    fun parsePlaylist(url: String) = PlaylistFactory.parsePlaylist(PlaylistVersion.DEFAULT, URL(url), 5000, 5000, 5000)


    fun inspect(playlist: AbstractPlaylist): Unit = when (playlist) {
        is MasterPlaylist -> {
            processMaster(playlist)
            val uri = playlist.variantStreams.first().uri
            val sublist = parsePlaylist(uri)
            inspect(sublist)
        }
        else -> {
            println("Not master playlist")
            val key = getKeyTag(playlist.tags)
            println(key)
        }
    }

}