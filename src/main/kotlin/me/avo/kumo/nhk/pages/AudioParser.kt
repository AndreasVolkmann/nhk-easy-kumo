package me.avo.kumo.nhk.pages

import com.iheartradio.m3u8.Encoding
import com.iheartradio.m3u8.Format
import com.iheartradio.m3u8.PlaylistParser
import com.iheartradio.m3u8.data.Playlist
import org.jcodec.common.JCodecUtil
import org.jcodec.common.TrackType
import sun.plugin.dom.exception.InvalidStateException
import java.io.File
import java.net.URL

class AudioParser(val id: String) {

    private val audioUrl = "https://nhks-vh.akamaihd.net/i/news/easy/$id.mp4/master.m3u8"

    fun run() {
        getPlaylist(audioUrl)
    }

    fun getPlaylist(audioUrl: String) {
        println(audioUrl)
        val playlist = URL(audioUrl).openStream().use {
            PlaylistParser(it, Format.EXT_M3U, Encoding.UTF_8).parse()
        }

        if (playlist.hasMasterPlaylist()) {
            println("Master")
            return handleMasterList(playlist)
        }
        if (playlist.hasMediaPlaylist()) {
            println("Media")
            return handleMediaList(playlist)
        }
    }

    fun handleMasterList(playlist: Playlist) = playlist
        .masterPlaylist
        .playlists
        .firstOrNull()
        ?.uri
        ?.let(this::getPlaylist) ?: throw InvalidStateException("Master playlist does not contain any playlists")

    fun handleMediaList(playlist: Playlist) {

        playlist.mediaPlaylist.tracks.let {
            println("Found ${it.size} tracks")
            if (it.any { it.isEncrypted }) {
                println("Encrypted")
            }

            it.forEach {
                println(it.uri)
            }
        }
    }


    fun demux() {
        val filename = "segment2_0_a.ts"
        val file = File("C:\\Users\\avolk\\Downloads\\$filename")
        JCodecUtil.createM2TSDemuxer(file, TrackType.AUDIO).let {
            it.v1.audioTracks.forEach { println(it) }
        }

    }

}