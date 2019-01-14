package me.avo.kumo

import com.apurebase.arkenv.parse
import me.avo.kumo.nhk.Crawler
import me.avo.kumo.util.ErrorHandler
import me.avo.kumo.util.driverName
import org.kodein.di.generic.instance

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
    val handler by kodein.instance<ErrorHandler>()
    handler.handle(ex)
}
