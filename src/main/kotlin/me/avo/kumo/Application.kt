package me.avo.kumo

import me.avo.kumo.nhk.*
import me.avo.kumo.util.*
import org.apache.logging.log4j.*

fun main(args: Array<String>) {
    Args.parse(args)
    if (Args.help) return
    start()
}

fun start() = try {
    System.setProperty("webdriver.chrome.driver", driverName)
    Crawler(Args.collection, Args.useApi, kodein).fetchAndImport()
} catch (ex: Exception) {
    ErrorHandler.handle(ex)
}
