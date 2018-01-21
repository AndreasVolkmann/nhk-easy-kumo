package me.avo.kumo

import com.github.salomonbrys.kodein.*
import me.avo.kumo.nhk.*
import me.avo.kumo.nhk.persistence.*
import me.avo.kumo.util.ErrorHandler
import me.avo.kumo.util.ProcessHandler
import me.avo.kumo.util.driverName
import me.avo.kumo.util.stop
import org.apache.logging.log4j.LogManager

fun main(args: Array<String>) {
    Args.parse(args)
    if (Args.help) return
    System.setProperty("webdriver.chrome.driver", driverName)
    start()
}

fun start() = try {
    val database: NhkDatabase = kodein.instance()
    val crawler = Crawler(Args.collection, Args.useApi, database)
    crawler.fetchAndImport()
} catch (ex: Exception) {
    logger.error(ex)
    ErrorHandler.handle(ex)
}

private val logger = LogManager.getLogger("Kumo")

