package storage

import org.apache.logging.log4j.LogManager


/**
 * Created by Av on 3/27/2017.
 */
object Process {

    private val logger = LogManager.getLogger(Process::class.java)

    val findProcess = "mongod.exe"

    fun isRunning(): Boolean {
        val filenameFilter = "/nh /fi \"Imagename eq $findProcess\""
        val tasksCmd = System.getenv("windir") + "/system32/tasklist.exe " + filenameFilter
        val p = Runtime.getRuntime().exec(tasksCmd)
        val procs = p.inputStream.bufferedReader().useLines {
            it.toList()
        }

        return procs.filter { row -> row.indexOf(findProcess) > -1 }.count() > 0
    }

    fun start(): java.lang.Process? {
        logger.trace("Starting $findProcess ...")
        val pb = ProcessBuilder("D:\\Programme\\MongoDb\\bin\\mongod.exe", "--dbpath", Application.mongoPath)
        pb.inheritIO()
        return pb.start()
    }

    fun stop(process: java.lang.Process) {
        logger.trace("Stopping $findProcess ...")
        process.destroy()
    }


}