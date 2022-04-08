package com.steelph0enix.integracja.lab2.parsers

import java.io.File

fun parseCSVFromFile(file: File, delimiter: Char): List<List<String>> {
    return parseCSVFromLines(file.readLines(), delimiter)
}

fun parseCSVFromLines(lines: List<String>, delimiter: Char): List<List<String>> {
    return lines.map { line -> line.split(delimiter) }
}

fun stringListToCSVFile(outputFile: File, data: List<List<String>>, delimiter: Char) {
    val writer = outputFile.bufferedWriter()
    for (row in data) {
        writer.append(row.reduce { acc, value ->
            acc + delimiter + value
        })
        writer.newLine()
    }
    writer.flush()
    writer.close()
}