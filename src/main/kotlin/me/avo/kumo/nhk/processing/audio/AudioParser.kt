package me.avo.kumo.nhk.processing.audio

import com.iheartradio.m3u8.Encoding
import com.iheartradio.m3u8.Format
import com.iheartradio.m3u8.PlaylistParser
import com.iheartradio.m3u8.data.*
import me.avo.kumo.util.getAudioInputStream
import me.avo.kumo.util.getLogger
import me.avo.kumo.util.joinAudioStreams
import me.avo.kumo.util.writeAudio
import org.jcodec.api.transcode.SinkImpl
import org.jcodec.api.transcode.SourceImpl
import org.jcodec.api.transcode.Transcoder
import org.jcodec.common.Codec
import org.jcodec.common.JCodecUtil
import org.jcodec.common.TrackType
import sun.plugin.dom.exception.InvalidStateException
import java.io.File
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.sound.sampled.AudioFileFormat

class AudioParser(
    val id: String,
    private val workingDir: File,
    private val destinationDir: File,
    private val ffmpegPath: String
) {

    private val audioUrl = "https://nhks-vh.akamaihd.net/i/news/easy/$id.mp4/master.m3u8"

    fun run(): List<File> = getPlaylist(audioUrl)
        .masterPlaylist
        .let(::handleMasterList)
        .mediaPlaylist
        .let(::handleMediaList)
        .let(::demuxSegments)
        .also(this::mergeAudio)

    fun mergeAudio(files: Collection<File>) {
        files.map(File::getAudioInputStream)
            .reduce { one, two -> joinAudioStreams(one, two) }
            .let { writeAudio(it, AudioFileFormat.Type.WAVE, File(destinationDir, "audio.wav")) }
    }

    private fun getPlaylist(audioUrl: String): Playlist = URL(audioUrl)
        .openStream()
        .use { PlaylistParser(it, Format.EXT_M3U, Encoding.UTF_8).parse() }

    private fun handleMasterList(playlist: MasterPlaylist) = playlist
        .playlists
        .firstOrNull()
        ?.uri
        ?.let(this::getPlaylist) ?: throw InvalidStateException("Master playlist does not contain any playlists")

    private fun handleMediaList(playlist: MediaPlaylist): Collection<File> = playlist.tracks.let { tracks ->
        println("Found ${tracks.size} tracks")
        val cipher = tracks.first().encryptionData.let(this::getCipher)
        return downloadTracks(tracks, cipher)
    }

    private fun downloadTracks(tracks: List<TrackData>, cipher: Cipher): Collection<File> = tracks
        .associate(this::getSegment)
        .mapValues { (_, bytes) -> cipher.doFinal(bytes) }
        .mapKeys { (name, _) -> File(workingDir, name) }
        .onEach { (file, bytes) -> file.writeBytes(bytes) }
        .keys

    fun getSegment(track: TrackData): Pair<String, ByteArray> {
        val filename = track.uri.substringAfter(".mp4/").substringBefore("?null=0&id=")
        println(filename)
        val bytes = URL(track.uri).openStream().use { it.readBytes() }
        return filename to bytes
    }

    fun getCipher(data: EncryptionData): Cipher {
        val bytes = URL(data.uri).readBytes()
        val chainmode = "CBC"
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

    fun demux(file: File, destination: File) = JCodecUtil.createM2TSDemuxer(file, TrackType.AUDIO).let { muxer ->
        muxer.v1.audioTracks
            .onEach(::println)
            .forEachIndexed { index, track ->
                Transcoder
                    .newTranscoder()
                    .addSource(
                        SourceImpl(
                            file.absolutePath,
                            org.jcodec.common.Format.WAV,
                            null,
                            CodecFinder.getCodec(muxer.v0, index)
                        )
                    )
                    .addSink(
                        SinkImpl(
                            File(destination, file.nameWithoutExtension + ".aac").absolutePath,
                            org.jcodec.common.Format.MOV, null, Codec.AAC
                        )
                    )
                    .create()
                    .transcode()
            }
    }

    fun demuxSegments(files: Collection<File>): List<File> = files.map(this::demuxFfmpeg)

    fun demuxFfmpeg(source: File): File {
        val fullPath = "$ffmpegPath\\bin\\ffmpeg.exe"
        val target = getDestinationFile(source, destinationDir)
        val fullCommand =
            "$fullPath -i ${source.absolutePath} -acodec copy -vcodec copy -map 0:p:keep_pid? -map 0:p:first_remove_pid? -map -0:p:second_remove_pid? ${target.absolutePath}"
        println(fullCommand)
        Runtime.getRuntime().exec(fullCommand).let {
            it.inputStream.bufferedReader().lines().forEach { println(it) }
            it.errorStream.bufferedReader().lines().forEach { println(it) }
            val result = it.waitFor()
            if (result != 0) throw IllegalStateException("Process returned result $result, not 0")
        }
        return target
    }

    fun getDestinationFile(source: File, destinationDir: File) =
        File(destinationDir, source.nameWithoutExtension + ".aac")

    private val logger = this::class.getLogger()

}