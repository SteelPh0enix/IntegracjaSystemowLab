package com.steelph0enix.integracja.lab2.models

import com.steelph0enix.integracja.lab2.data.Laptop
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.table.AbstractTableModel

class LaptopTableModel(val laptopList: List<Laptop>) : AbstractTableModel() {
    private val columnNames = listOf(
        "Lp.",
        "Producent",
        "Przekątna ekranu",
        "Rozdzielczość ekranu",
        "Rodzaj powierzchni ekranu",
        "Czy ekran jest dotykowy?",
        "Nazwa CPU",
        "Liczba rdzeni fizycznych",
        "Prędkość taktowania (MHz)",
        "Wielkość pamięci RAM (GB)",
        "Pojemność dysku (GB)",
        "Rodzaj dysku",
        "Nazwa GPU",
        "Pamięć GPU (GB)",
        "Nazwa systemu operacyjnego",
        "Rodzaj napędu fizycznego"
    )

    private val columnTypes = listOf(
        Integer::class.java,        // ID
        String::class.java,     // manufacturer
        Double::class.javaObjectType,     // screen diagonal
        String::class.java,     // screen resolution
        String::class.java,     // screen surface type
        Boolean::class.javaObjectType,    // has touchscreen
        String::class.java,     // cpu name
        Integer::class.java,        // physical cores count
        Integer::class.java,        // frequency
        Integer::class.java,        // ram size
        Integer::class.java,        // hdd capacity
        String::class.java,     // hdd type
        String::class.java,     // gpu name
        Integer::class.java,        // gpu mem size
        String::class.java,     // os name
        String::class.java,     // external drive type
    )

    override fun getColumnClass(columnIndex: Int): Class<out Any> = columnTypes[columnIndex]
    override fun getColumnName(column: Int): String = columnNames[column]
    override fun getRowCount(): Int = laptopList.size
    override fun getColumnCount(): Int = columnNames.size
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true

    private fun showValidationError(fieldName: String, reason: String) {
        JOptionPane.showMessageDialog(
            JFrame(),
            "$fieldName invalid - $reason",
            "Validation error",
            JOptionPane.ERROR_MESSAGE
        )
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val laptop = laptopList[rowIndex]
        return when (columnIndex) {
            0 -> laptop.id
            1 -> laptop.manufacturer
            2 -> laptop.screenDiagonalInches
            3 -> laptop.screenResolutionString()
            4 -> laptop.screenSurfaceType
            5 -> laptop.hasTouchscreen
            6 -> laptop.cpuName
            7 -> laptop.physicalCoresCount
            8 -> laptop.frequencyMHz
            9 -> laptop.ramSizeGB
            10 -> laptop.hardDriveCapacityGB
            11 -> laptop.hardDriveType
            12 -> laptop.gpuName
            13 -> laptop.gpuMemorySizeGB
            14 -> laptop.osName
            15 -> laptop.externalDriveType
            else -> null
        }
    }

    override fun setValueAt(newValue: Any?, rowIndex: Int, columnIndex: Int) {
        val laptop = laptopList[rowIndex]
        when (columnIndex) {
            0 -> laptop.id = newValue as Int
            1 -> laptop.manufacturer = newValue as String
            2 -> laptop.screenDiagonalInches = newValue as Double
            3 -> if (!laptop.screenResolutionFromString(newValue as String)) showValidationError(
                "Screen resolution",
                "invalid format, should be [width]x[height], for example 1920x1080 for 1080p display"
            )
            4 -> laptop.screenSurfaceType = newValue as String
            5 -> laptop.hasTouchscreen = newValue as Boolean
            6 -> laptop.cpuName = newValue as String
            7 -> laptop.physicalCoresCount = newValue as Int
            8 -> laptop.frequencyMHz = newValue as Int
            9 -> laptop.ramSizeGB = newValue as Int
            10 -> laptop.hardDriveCapacityGB = newValue as Int
            11 -> laptop.hardDriveType = newValue as String
            12 -> laptop.gpuName = newValue as String
            13 -> laptop.gpuMemorySizeGB = newValue as Int
            14 -> laptop.osName = newValue as String
            15 -> laptop.externalDriveType = newValue as String
        }
    }
}