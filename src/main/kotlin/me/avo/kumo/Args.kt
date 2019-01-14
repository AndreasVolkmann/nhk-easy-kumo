package me.avo.kumo

import com.apurebase.arkenv.Arkenv

object Args : Arkenv() {

    val useApi: Boolean by argument("--api", "-a") {
        description = "Whether the LingQ API should be used"
    }

    val collection: String by argument("--collection", "-c") {
        description = "The ID of the target collection / course"
    }

    val ffmepgPath: String by argument("-ffmpeg", "--ffmpeg-path") {
        description = "File path to ffmpeg install dir"
    }
}
