package com.steelph0enix.integracja.lab2

fun enumerateStringList(stringList: List<List<String>>): List<List<String>> {
    return stringList.mapIndexed { index, line ->
        listOf(index.toString()) + line
    }
}

fun removeLastColumns(data: List<List<String>>, columnsToRemove: Int): List<List<String>> {
    if (data.isEmpty()) {
        return listOf(listOf())
    }
    val wantedColumns = data[0].size - columnsToRemove - 1
    return data.map { line ->
        line.slice(0..wantedColumns)
    }
}