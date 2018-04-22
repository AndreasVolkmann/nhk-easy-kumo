package me.avo.kumo

import com.beust.jcommander.*

object Args {

    @Parameter(names = ["--api", "-a"], description = "Whether the LingQ API should be used")
    var useApi = false
        private set

    @Parameter(names = ["--collection", "-c"], description = "The ID of the target collection / course",
            required = true, echoInput = true)
    lateinit var collection: String

    @Parameter(names = ["-ffmpeg", "--ffmpeg-path"], description = "File path to ffmpeg install dir", required = true)
    lateinit var ffmepgPath: String

    @Parameter(names = ["--help", "-h"], help = true)
    var help = false
        private set

    fun parse(args: Array<String>) = JCommander
            .newBuilder()
            .addObject(this)
            .build()
            .let {
                it.parse(*args)
                if (help) it.usage()
            }

}