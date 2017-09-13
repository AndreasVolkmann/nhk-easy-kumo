package me.avo.kumo

import me.avo.kumo.nhk.Crawler
import me.avo.kumo.util.ErrorHandler
import me.avo.kumo.util.ProcessHandler
import me.avo.kumo.util.driverName
import me.avo.kumo.util.stop
import org.apache.logging.log4j.LogManager

fun main(args: Array<String>) {
    Args.parse(args)
    System.setProperty("webdriver.chrome.driver", driverName)
    val process = if (ProcessHandler.isRunning()) null else ProcessHandler.start()
    start(process)
}

fun start(process: Process?) = try {
    Thread.sleep(2000)
    if (ProcessHandler.isRunning().not()) logger.warn("Process is not running!")
    val crawler = Crawler(Args.collection, Args.useApi)
    crawler.fetchAndImport()
} catch (ex: Exception) {
    logger.error(ex)
    ErrorHandler.handle(ex)
} finally {
    process.stop()
}

private val logger = LogManager.getLogger("Kumo")

