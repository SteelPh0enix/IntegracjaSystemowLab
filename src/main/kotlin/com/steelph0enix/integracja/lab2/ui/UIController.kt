package com.steelph0enix.integracja.lab2.ui

import com.steelph0enix.integracja.lab2.data.Laptop
import com.steelph0enix.integracja.lab2.enumerateStringList
import com.steelph0enix.integracja.lab2.models.LaptopTableModel
import com.steelph0enix.integracja.lab2.parsers.parseCSVFromFile
import com.steelph0enix.integracja.lab2.removeLastColumns
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableModel

class UIController {
    private val contentFrame = JFrame("Lab2 - Wojciech Olech - Integracja systemów")
    private val loadDataFromCSVButton = JButton("Load data from CSV")
    private val saveDataToCSVButton = JButton("Save data to CSV")
    private var dataTable: JTable? = null
    private var dataTablePane: JScrollPane? = null

    fun createAndShowUI(width: Int, height: Int) {
        contentFrame.layout = BorderLayout()
        contentFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val buttonsPanel = JPanel()

        loadDataFromCSVButton.addActionListener(this::onLoadDataFromCSVClicked)
        buttonsPanel.add(loadDataFromCSVButton)

        saveDataToCSVButton.addActionListener(this::onSaveDataToCSVClicked)
        buttonsPanel.add(saveDataToCSVButton)

        contentFrame.add(buttonsPanel, BorderLayout.PAGE_START)
        contentFrame.size = Dimension(width, height)
        contentFrame.setLocationRelativeTo(null)
        contentFrame.isVisible = true
    }

    private fun onLoadDataFromCSVClicked(e: ActionEvent) {
        val fileChooser = JFileChooser()
        if (fileChooser.showOpenDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            val rawCSV = parseCSVFromFile(selectedFile, ';')
            val normalizedCSV = removeLastColumns(rawCSV, 1)
            val enumeratedCSV = enumerateStringList(normalizedCSV)

            for (line in enumeratedCSV) {
                println(line)
            }
        }
    }

    private fun onSaveDataToCSVClicked(e: ActionEvent) {

    }

    private fun refreshGUI() {
        contentFrame.revalidate()
        contentFrame.repaint()
    }

    private fun removeDataTable() {
        dataTablePane?.let {
            contentFrame.remove(it)
            refreshGUI()
        }
    }

    private fun createDataTable(model: TableModel) {
        removeDataTable()

        val cellRenderer = DefaultTableCellRenderer()
        cellRenderer.horizontalAlignment = JLabel.LEFT

        dataTable = JTable(model)
        dataTablePane = JScrollPane(dataTable)

        dataTable!!.setDefaultRenderer(Int::class.java, cellRenderer)
        contentFrame.add(dataTablePane!!, BorderLayout.CENTER)
        refreshGUI()
    }
}