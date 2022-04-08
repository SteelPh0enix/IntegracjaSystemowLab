package com.steelph0enix.integracja.lab2.ui

import com.steelph0enix.integracja.lab2.data.Laptop
import com.steelph0enix.integracja.lab2.models.LaptopTableModel
import com.steelph0enix.integracja.lab2.parsers.breakLaptopPropertyListForExport
import com.steelph0enix.integracja.lab2.parsers.fixImportedLaptopPropertyList
import com.steelph0enix.integracja.lab2.parsers.parseCSVFromFile
import com.steelph0enix.integracja.lab2.parsers.stringListToCSVFile
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableModel

class UIController {
    private val contentFrame = JFrame("Lab2 - Wojciech Olech - Integracja systemów")
    private val loadDataFromCSVButton = JButton("Load data from CSV")
    private val saveDataToCSVButton = JButton("Save data to CSV")
    private var dataTable: JTable? = null
    private var dataTablePane: JScrollPane? = null

    private class LaptopTableCellRenderer : DefaultTableCellRenderer() {
        val defaultBackgroundColor: Color? = background

        override fun getTableCellRendererComponent(
            table: JTable?,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ): Component {
            val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
            val model = table?.model as LaptopTableModel
            val itemValue = model.getValueAt(row, column)

            if (itemValue == null) {
                component.background = Color.red
            } else if (itemValue is String && itemValue.isEmpty()) {
                component.background = Color.orange
            } else if (!isSelected) {
                component.background = defaultBackgroundColor
            }

            return component
        }
    }

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
        fileChooser.fileFilter = FileNameExtensionFilter("CSV files", "txt", "csv")
        if (fileChooser.showOpenDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            val rawCSV = parseCSVFromFile(selectedFile, ';')

            val laptopStringList = fixImportedLaptopPropertyList(rawCSV)
            val laptopList = laptopStringList.map { Laptop.fromStringList(it) }
            createDataTable(LaptopTableModel(laptopList))
        }
    }

    private fun onSaveDataToCSVClicked(e: ActionEvent) {
        if (dataTable == null) {
            return
        }

        val fileChooser = JFileChooser()
        if (fileChooser.showSaveDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            var selectedFile = fileChooser.selectedFile
            if (selectedFile.extension.isEmpty()) {
                selectedFile = File(selectedFile.absolutePath + ".txt")
            }

            val laptopListModel = dataTable?.model as LaptopTableModel
            val laptopStringList: List<List<String>> = laptopListModel.laptopList.map { it.toStringList() }
            val exportableLaptopList = breakLaptopPropertyListForExport(laptopStringList)
            stringListToCSVFile(selectedFile, exportableLaptopList, ';')
        }
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

        val cellRenderer = LaptopTableCellRenderer()
        cellRenderer.horizontalAlignment = JLabel.LEFT

        dataTable = JTable(model)
        dataTablePane = JScrollPane(dataTable)

        dataTable!!.setDefaultRenderer(Integer::class.java, cellRenderer)
        dataTable!!.setDefaultRenderer(Double::class.javaObjectType, cellRenderer)
        dataTable!!.setDefaultRenderer(String::class.java, cellRenderer)
        contentFrame.add(dataTablePane!!, BorderLayout.CENTER)
        refreshGUI()
    }
}