package me.avo.kumo

import com.github.salomonbrys.kodein.instance
import me.avo.kumo.nhk.Crawler
import me.avo.kumo.util.ErrorHandler
import me.avo.kumo.util.driverName

fun main(args: Array<String>) {
    Args.parse(args)
    if (Args.help) return
    start()
}

fun start() = try {
    System.setProperty("webdriver.chrome.driver", driverName)

    val crawler = Crawler(
        Args.collection,
        Args.ffmepgPath,
        Args.useApi,
        kodein
    )

    crawler.fetchAndImport()

} catch (ex: Exception) {
    val handler = kodein.instance<ErrorHandler>()
    handler.handle(ex)
}
