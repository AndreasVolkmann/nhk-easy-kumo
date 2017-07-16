val os: String = System.getProperty("os.name")
val isWindows = os.contains("windows", true)
val mongoPath = if (isWindows) "D:\\data\\db" else "/data/db"

val processName = if (isWindows) "mongod.exe" else "mongod"

val path = if (isWindows) "D:\\Programme\\MongoDb\\bin\\$processName"
else "/usr/local/Cellar/mongodb/3.2.6/bin/$processName"

val cmd = if (!isWindows) "ps aux | grep [-i] \$$processName | wc -l"
else System.getenv("windir") + "/system32/tasklist.exe /nh /fi \"Imagename eq $processName\""


val driverName = if (isWindows) "chromedriver.exe" else "chromedriver"