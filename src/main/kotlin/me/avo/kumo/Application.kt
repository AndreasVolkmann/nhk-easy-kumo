package me.avo.kumo

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
    Crawler(
        Args.collection,
        Args.ffmepgPath,
        Args.useApi,
        kodein
    ).fetchAndImport()
} catch (ex: Exception) {
    ErrorHandler.handle(ex)
}
