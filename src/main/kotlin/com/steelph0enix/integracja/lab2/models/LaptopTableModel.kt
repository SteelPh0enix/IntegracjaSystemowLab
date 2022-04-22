package com.steelph0enix.integracja.lab2.models

import com.steelph0enix.integracja.lab2.data.Laptop
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.table.AbstractTableModel

fun <T> isDuplicateOfPreviousElement(elements: List<T>, elementIndex: Int): Boolean {
    val element = elements[elementIndex]
    for (i in 0 until elementIndex) {
        if (element == elements[i]) {
            println("Found duplicate, element $i")
            return true
        }
        println("Element $i is not a duplicate")
    }

    return false
}

class LaptopTableModel(val laptopList: List<Laptop>) : AbstractTableModel() {
    private var isDuplicateRowStates = mutableListOf<Boolean>()

    init {
        markDuplicates()
    }

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

    private var valueModificationStates = MutableList(laptopList.size) { MutableList(columnNames.size) { false } }

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

    private fun markDuplicates() {
        isDuplicateRowStates = mutableListOf()
        for (laptopIndex in laptopList.indices) {
            isDuplicateRowStates.add(isDuplicateOfPreviousElement(laptopList, laptopIndex))
        }
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
            0 -> if (newValue as Int != laptop.id) laptop.id = newValue else return
            1 -> if (newValue as String != laptop.manufacturer) laptop.manufacturer = newValue else return
            2 -> if (newValue as Double != laptop.screenDiagonalInches) laptop.screenDiagonalInches =
                newValue else return
            3 -> {
                val laptopRes = Laptop.screenResolutionFromString(newValue as String)
                if (laptopRes == null) {
                    showValidationError(
                        "Screen resolution",
                        "invalid format, should be [width]x[height], for example 1920x1080 for 1080p display"
                    )
                    return
                } else {
                    if (laptopRes != laptop.screenResolution) laptop.screenResolution = laptopRes else return
                }
            }
            4 -> if (newValue as String != laptop.screenSurfaceType) laptop.screenSurfaceType = newValue else return
            5 -> if (newValue as Boolean != laptop.hasTouchscreen) laptop.hasTouchscreen = newValue else return
            6 -> if (newValue as String != laptop.cpuName) laptop.cpuName = newValue else return
            7 -> if (newValue as Int != laptop.physicalCoresCount) laptop.physicalCoresCount = newValue else return
            8 -> if (newValue as Int != laptop.frequencyMHz) laptop.frequencyMHz = newValue else return
            9 -> if (newValue as Int != laptop.ramSizeGB) laptop.ramSizeGB = newValue else return
            10 -> if (newValue as Int != laptop.hardDriveCapacityGB) laptop.hardDriveCapacityGB = newValue else return
            11 -> if (newValue as String != laptop.hardDriveType) laptop.hardDriveType = newValue else return
            12 -> if (newValue as String != laptop.gpuName) laptop.gpuName = newValue else return
            13 -> if (newValue as Int != laptop.gpuMemorySizeGB) laptop.gpuMemorySizeGB = newValue else return
            14 -> if (newValue as String != laptop.osName) laptop.osName = newValue else return
            15 -> if (newValue as String != laptop.externalDriveType) laptop.externalDriveType = newValue else return
        }

        valueModificationStates[rowIndex][columnIndex] = true
        markDuplicates()
    }

    fun wasColumnModified(rowIndex: Int, columnIndex: Int) = valueModificationStates[rowIndex][columnIndex]

    fun resetModificationState() {
        valueModificationStates = MutableList(laptopList.size) { MutableList(columnNames.size) { false } }
    }

    fun isRowDuplicate(rowIndex: Int) = isDuplicateRowStates[rowIndex]
}