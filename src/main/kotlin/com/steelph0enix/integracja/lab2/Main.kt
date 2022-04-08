package com.steelph0enix.integracja.lab2

import com.steelph0enix.integracja.lab2.ui.UIController
import javax.swing.SwingUtilities
import javax.swing.UIManager

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val ui = UIController()
        ui.createAndShowUI(800, 600)
    }
}