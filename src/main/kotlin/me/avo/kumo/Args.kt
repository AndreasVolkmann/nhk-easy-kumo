package me.avo.kumo

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter

object Args {

    @Parameter(names = arrayOf("--api", "-a"), description = "Whether the LingQ API should be used")
    var useApi = false
        private set

    @Parameter(names = arrayOf("--collection", "-c"), description = "The ID of the target collection / course",
            required = true, echoInput = true)
    lateinit var collection: String

    fun parse(args: Array<String>) = JCommander
            .newBuilder()
            .addObject(this)
            .build()
            .parse(*args)

}