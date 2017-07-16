package storage

import cmd
import mongoPath
import org.apache.logging.log4j.LogManager
import path
import processName

object ProcessHandler {

    fun isRunning(): Boolean {
        val p = Runtime.getRuntime().exec(cmd)
        val procs = p.inputStream.bufferedReader().useLines {
            it.toList()
        }
        return procs.filter { row -> row.indexOf(processName) > -1 }.count() > 0
    }

    fun start(): Process? {
        logger.trace("Starting $processName ...")
        val pb = ProcessBuilder(path, "--dbpath", mongoPath)
        pb.inheritIO()
        return pb.start()
    }

    fun stop(process: Process?) = process?.let {
        logger.trace("Stopping $processName ...")
        it.destroy()
    }

    private val logger = LogManager.getLogger(ProcessHandler::class.java)

}

fun Process?.stop() = ProcessHandler.stop(this)