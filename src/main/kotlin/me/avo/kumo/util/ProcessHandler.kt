package me.avo.kumo.util

object ProcessHandler {

    fun isRunning(): Boolean = Runtime.getRuntime()
            .exec(cmd)
            .inputStream
            .bufferedReader()
            .useLines { it.toList() }
            .filter { it.indexOf(processName) > -1 }
            .count() > 0

    fun start(): Process? {
        logger.trace("Starting $processName ...")
        return ProcessBuilder(path, "--dbpath", mongoPath).run {
            inheritIO()
            start()
        }
    }

    fun stop(process: Process?) = process?.let {
        logger.trace("Stopping $processName ...")
        it.destroy()
    }

    private val logger = this::class.getLogger()

}

fun Process?.stop() = ProcessHandler.stop(this)

val os: String = System.getProperty("os.name")
val isWindows = os.contains("windows", true)
val mongoPath = if (isWindows) "D:\\me.avo.kumo.data\\db" else "/me/avo/kumo/data/db"

val processName = if (isWindows) "mongod.exe" else "mongod"

val path = if (isWindows) "D:\\Programme\\MongoDb\\bin\\$processName"
else "/usr/local/Cellar/mongodb/3.2.6/bin/$processName"

val cmd = if (!isWindows) "ps aux | grep [-i] \$$processName | wc -l"
else System.getenv("windir") + "/system32/tasklist.exe /nh /fi \"Imagename eq $processName\""


val driverName = if (isWindows) "chromedriver.exe" else "chromedriver"