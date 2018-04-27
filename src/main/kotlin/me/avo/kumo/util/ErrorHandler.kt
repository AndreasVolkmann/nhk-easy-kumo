package me.avo.kumo.util

import me.avo.kumo.nhk.data.ArticleException
import me.avo.kumo.nhk.persistence.NhkDatabase
import org.apache.logging.log4j.LogManager
import java.awt.Toolkit
import javax.swing.JOptionPane

class ErrorHandler(private val nhkDatabase: NhkDatabase) {

    fun handle(ex: Exception) {
        logger.error("error", ex)
        Toolkit.getDefaultToolkit().beep()

        if (ex is ArticleException) {
            // TODO add option to ignore
            println(ex.articleId)
            JOptionPane.showMessageDialog(null, ex, "蜘蛛 has encountered an error!", JOptionPane.WARNING_MESSAGE)
            if (false) {
                nhkDatabase.ignoreArticle(ex.articleId)
            }
        } else {
            JOptionPane.showMessageDialog(null, ex, "蜘蛛 has encountered an error!", JOptionPane.WARNING_MESSAGE)
        }

        throw ex
    }

    private val logger = LogManager.getLogger("Kumo")

}