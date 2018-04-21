package me.avo.kumo.nhk.processing.audio

import com.iheartradio.m3u8.Encoding
import com.iheartradio.m3u8.Format
import com.iheartradio.m3u8.PlaylistParser
import com.iheartradio.m3u8.data.*
import me.avo.kumo.util.getLogger
import org.jcodec.common.JCodecUtil
import org.jcodec.common.TrackType
import sun.plugin.dom.exception.InvalidStateException
import java.io.File
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AudioParser(val id: String) {

    private val audioUrl = "https://nhks-vh.akamaihd.net/i/news/easy/$id.mp4/master.m3u8"

    fun run() {
        getPlaylist(audioUrl)
    }

    fun getPlaylist(audioUrl: String): Unit = URL(audioUrl)
        .openStream()
        .use { PlaylistParser(it, Format.EXT_M3U, Encoding.UTF_8).parse() }
        .run {
            when {
                hasMasterPlaylist() -> {
                    println("Master")
                    handleMasterList(masterPlaylist)
                }
                hasMediaPlaylist() -> {
                    println("Media")
                    handleMediaList(mediaPlaylist)
                }
            }
        }

    fun handleMasterList(playlist: MasterPlaylist) = playlist
        .playlists
        .firstOrNull()
        ?.uri
        ?.let(this::getPlaylist) ?: throw InvalidStateException("Master playlist does not contain any playlists")

    fun handleMediaList(playlist: MediaPlaylist): Collection<File> = playlist.tracks.let { tracks ->
        println("Found ${tracks.size} tracks")
        val cipher = tracks.first().encryptionData.let(this::getCipher)
        val dir = File("C:\\Users\\avolk\\Downloads\\nhk\\wrk\\")

        return tracks
            .associate(this::getSegment)
            .mapValues { (_, bytes) -> cipher.doFinal(bytes) }
            .mapKeys { (name, _) -> File(dir, name) }
            .onEach { (file, bytes) -> file.writeBytes(bytes) }
            .keys
    }

    fun getSegment(track: TrackData): Pair<String, ByteArray> {
        val filename = track.uri.substringAfter(".mp4/").substringBefore("?null=0&id=")
        println(filename)
        val bytes = URL(track.uri).openStream().use { it.readBytes() }
        return filename to bytes
    }

    fun getCipher(data: EncryptionData): Cipher {
        val bytes = URL(data.uri).readBytes()
        val chainmode = "CTR"
        val method = when (data.method) {
            EncryptionMethod.AES -> "AES/$chainmode/NoPadding"
            else -> data.method.name
        }
        val keySpec = SecretKeySpec(bytes, data.method.name)
        logger.trace("Decrypting using method ${data.method} / $method")
        return Cipher
            .getInstance(method)
            .apply { init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(ByteArray(16))) }
    }

    fun demux(file: File) = JCodecUtil.createM2TSDemuxer(file, TrackType.AUDIO).let {
        it.v1.audioTracks.onEach(::println)
            .forEach {

            }
    }

    private val logger = this::class.getLogger()

}