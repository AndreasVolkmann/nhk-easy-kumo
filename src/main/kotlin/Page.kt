import java.io.File


interface Page {

    val url: String

    val path get() = "${this::class.java.simpleName}_$currentDate.html"

    fun writeFile(text: String): String {
        val fullPath = "files/$path"
        File(fullPath).writeText(text)
        return fullPath
    }

    fun get(): String

}