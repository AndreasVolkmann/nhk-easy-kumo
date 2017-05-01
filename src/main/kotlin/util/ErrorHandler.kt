package util

import java.awt.Toolkit
import javax.swing.JOptionPane

/**
 * Created by Av on 5/1/2017.
 */
object ErrorHandler {

    fun handle(ex: Exception) {
        val optionPane = JOptionPane(ex, JOptionPane.WARNING_MESSAGE)
        val dialog = optionPane.createDialog("蜘蛛 has encountered an error!")
        Toolkit.getDefaultToolkit().beep()
        dialog.isAlwaysOnTop = true
        dialog.isVisible = true
        throw ex
    }

}