package me.avo.kumo.util

import org.apache.logging.log4j.LogManager
import java.awt.Toolkit
import javax.swing.JOptionPane

object ErrorHandler {

    fun handle(ex: Exception) {
        logger.error("error", ex)
        Toolkit.getDefaultToolkit().beep()
        JOptionPane.showMessageDialog(null, ex, "蜘蛛 has encountered an error!", JOptionPane.WARNING_MESSAGE)
        throw ex
    }

    private val logger = LogManager.getLogger("Kumo")

}