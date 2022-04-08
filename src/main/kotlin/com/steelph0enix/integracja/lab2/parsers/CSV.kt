package com.steelph0enix.integracja.lab2.parsers

import java.io.File

fun parseCSVFromFile(file: File, delimiter: Char): List<List<String>> {
    return parseCSVFromLines(file.readLines(), delimiter)
}

fun parseCSVFromString(data: String, delimiter: Char, newline: String): List<List<String>> {
    return parseCSVFromLines(data.split(newline), delimiter)
}

fun parseCSVFromLines(lines: List<String>, delimiter: Char): List<List<String>> {
    return lines.map { line -> line.split(delimiter) }
}