import data.Article

fun Article.makeFiles() {
    println("$date - $id - $title")
    imageFile.writeIfNotExists(image)
    audioFile.writeIfNotExists(audio)
    htmlFile.writeIfNotExists(this.toHtml())
}


fun Article.toHtml() = Application::class.java.classLoader.getResource("template.html")
        .readText()
        .replace("{title}", title)
        .replace("{articleUrl}", url)
        .replace("{audioUrl}", audioUrl)
        .replace("{audioLength}", audioFile.getDuration().toString())
        .replace("{body}", content)