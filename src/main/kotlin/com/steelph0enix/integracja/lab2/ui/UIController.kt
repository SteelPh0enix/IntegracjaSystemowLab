package com.steelph0enix.integracja.lab2.ui

import com.steelph0enix.integracja.lab2.SQLiteManager
import com.steelph0enix.integracja.lab2.data.Laptop
import com.steelph0enix.integracja.lab2.models.LaptopListXMLModel
import com.steelph0enix.integracja.lab2.models.LaptopTableModel
import com.steelph0enix.integracja.lab2.models.laptopFromXMLModel
import com.steelph0enix.integracja.lab2.models.laptopToXMLModel
import com.steelph0enix.integracja.lab2.parsers.breakLaptopPropertyListForExport
import com.steelph0enix.integracja.lab2.parsers.fixImportedLaptopPropertyList
import com.steelph0enix.integracja.lab2.parsers.parseCSVFromFile
import com.steelph0enix.integracja.lab2.parsers.stringListToCSVFile
import org.simpleframework.xml.core.Persister
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableModel

class UIController {
    private val contentFrame = JFrame("Lab2 - Wojciech Olech - Integracja systemów")
    private val loadDataFromCSVButton = JButton("Load data from CSV")
    private val saveDataToCSVButton = JButton("Save data to CSV")
    private val loadDataFromXMLButton = JButton("Load data from XML")
    private val saveDataToXMLButton = JButton("Save data to XML")
    private val saveDataToDatabase = JButton("Save data to database")
    private val loadDataFromDatabase = JButton("Load data from database")
    private val userMessageLabel = JLabel("")
    private var dataTable: JTable? = null
    private var dataTablePane: JScrollPane? = null
    private val databaseManager = SQLiteManager()

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

            if (model.isRowDuplicate(row)) {
                component.background = Color.RED
            } else if (itemValue == null) {
                component.background = Color.orange
            } else if (itemValue is String && itemValue.isEmpty()) {
                component.background = Color.orange
            } else if (!isSelected) {
                if (model.wasColumnModified(row, column)) {
                    component.background = defaultBackgroundColor
                } else {
                    component.background = Color.LIGHT_GRAY
                }
            }

            return component
        }
    }

    fun createAndShowUI(width: Int, height: Int) {
        contentFrame.layout = BorderLayout(5, 5)
        contentFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val buttonsPanel = JPanel()

        loadDataFromCSVButton.addActionListener(this::onLoadDataFromCSVClicked)
        buttonsPanel.add(loadDataFromCSVButton)

        loadDataFromXMLButton.addActionListener(this::onLoadDataFromXMLClicked)
        buttonsPanel.add(loadDataFromXMLButton)

        loadDataFromDatabase.addActionListener(this::onLoadDataFromDatabaseClicked)
        buttonsPanel.add(loadDataFromDatabase)

        saveDataToCSVButton.addActionListener(this::onSaveDataToCSVClicked)
        buttonsPanel.add(saveDataToCSVButton)

        saveDataToXMLButton.addActionListener(this::onSaveDataToXMLClicked)
        buttonsPanel.add(saveDataToXMLButton)

        saveDataToDatabase.addActionListener(this::onSaveDataToDatabaseClicked)
        buttonsPanel.add(saveDataToDatabase)

        val utilsPanel = JPanel()
        utilsPanel.border = EmptyBorder(5, 5, 5, 5)
        utilsPanel.add(userMessageLabel)

        contentFrame.add(buttonsPanel, BorderLayout.PAGE_START)
        contentFrame.add(utilsPanel, BorderLayout.PAGE_END)
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
            val tableModel = LaptopTableModel(laptopList)
            createDataTable(tableModel)

            val rowsAmount = laptopList.size

            setInfoMessage("Loaded $rowsAmount items from CSV file with ${tableModel.duplicatesCount()} duplicates")
        }
    }

    private fun onSaveDataToCSVClicked(e: ActionEvent) {
        if (dataTable == null) {
            return
        }

        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("Text CSV files", "txt")
        if (fileChooser.showSaveDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            var selectedFile = fileChooser.selectedFile
            if (selectedFile.extension.isEmpty()) {
                selectedFile = File(selectedFile.absolutePath + ".txt")
            }

            val laptopListModel = dataTable?.model as LaptopTableModel
            val laptopStringList: List<List<String>> =
                laptopListModel.laptopListWithoutDuplicates().map { it.toStringList() }
            val exportableLaptopList = breakLaptopPropertyListForExport(laptopStringList)
            stringListToCSVFile(selectedFile, exportableLaptopList, ';')
            laptopListModel.resetModificationState()
            refreshGUI()

            setInfoMessage("Data saved as CSV in $selectedFile")
        }
    }

    private fun onLoadDataFromXMLClicked(e: ActionEvent) {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("XML files", "xml")
        if (fileChooser.showOpenDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            val serializer = Persister()

            val xmlModel = serializer.read(LaptopListXMLModel::class.java, selectedFile)
            val laptopList = xmlModel.laptopList.map { laptop -> laptopFromXMLModel(laptop) }
            val tableModel = LaptopTableModel(laptopList)
            createDataTable(tableModel)

            val rowsAmount = laptopList.size

            setInfoMessage("Loaded $rowsAmount items from XML file, with ${tableModel.duplicatesCount()} duplicates")
        }
    }

    private fun onSaveDataToXMLClicked(e: ActionEvent) {
        if (dataTable == null) {
            return
        }


        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("XML files", "xml")
        if (fileChooser.showSaveDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            var selectedFile = fileChooser.selectedFile
            if (selectedFile.extension.isEmpty()) {
                selectedFile = File(selectedFile.absolutePath + ".xml")
            }

            val laptopListModel = dataTable?.model as LaptopTableModel
            val laptopXMLList = laptopListModel.laptopListWithoutDuplicates().map { laptop ->
                laptopToXMLModel(laptop)
            }

            val laptopListXMLModel = LaptopListXMLModel(laptopXMLList)

            val serializer = Persister()
            serializer.write(laptopListXMLModel, selectedFile)
            laptopListModel.resetModificationState()
            refreshGUI()

            setInfoMessage("Data saved as XML in $selectedFile")
        }
    }

    private fun onLoadDataFromDatabaseClicked(e: ActionEvent) {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("SQLite files", "sqlite")
        if (fileChooser.showOpenDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            val serializer = Persister()

            val laptopList = databaseManager.readLaptopListFromDatabase(selectedFile.path)
            val tableModel = LaptopTableModel(laptopList)
            createDataTable(tableModel)

            val rowsAmount = laptopList.size

            setInfoMessage("Loaded $rowsAmount items from SQLite database, with ${tableModel.duplicatesCount()} duplicates")
        }

    }
    private fun onSaveDataToDatabaseClicked(e: ActionEvent) {
        if (dataTable == null) {
            return
        }

        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter("SQLite database", "sqlite")
        if (fileChooser.showSaveDialog(contentFrame) == JFileChooser.APPROVE_OPTION) {
            var selectedFile = fileChooser.selectedFile
            if (selectedFile.extension.isEmpty()) {
                selectedFile = File(selectedFile.absolutePath + ".sqlite")
            }

            val laptopListModel = dataTable?.model as LaptopTableModel
            val laptopList = laptopListModel.laptopListWithoutDuplicates()
            databaseManager.saveLaptopListToDatabase(selectedFile.path, laptopList)

            laptopListModel.resetModificationState()
            refreshGUI()

            setInfoMessage("Data saved in SQLite database in $selectedFile")
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
        dataTable!!.setDefaultRenderer(Boolean::class.javaObjectType, cellRenderer)
        contentFrame.add(dataTablePane!!, BorderLayout.CENTER)
        refreshGUI()
    }

    private fun setInfoMessage(message: String) {
        userMessageLabel.text = message
    }
}