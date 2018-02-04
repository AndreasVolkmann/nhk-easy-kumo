package me.avo.kumo.util

import org.apache.logging.log4j.*
import java.awt.*
import javax.swing.*

object ErrorHandler {

    fun handle(ex: Exception) {
        logger.error("error", ex)
        Toolkit.getDefaultToolkit().beep()
        JOptionPane(ex, JOptionPane.WARNING_MESSAGE).createDialog("蜘蛛 has encountered an error!").apply {
            isAlwaysOnTop = true
            isVisible = true
        }
        throw ex
    }

    private val logger = LogManager.getLogger("Kumo")

}