package com.steelph0enix.integracja.lab2.parsers

fun fixImportedLaptopPropertyList(laptopPropertyList: List<List<String>>): List<List<String>> {
    return laptopPropertyList.mapIndexed { index, laptopProperties ->
        val wantedLaptopPropertiesCount = laptopProperties.size - 2
        // 1. add first column with laptop indexes
        // 2. remove last, empty column
        // 3. fix laptop fields
        listOf(index.toString()) + fixLaptopStringListFields(laptopProperties.slice(0..wantedLaptopPropertiesCount))
    }
}

fun breakLaptopPropertyListForExport(laptopPropertyList: List<List<String>>): List<List<String>> {
    return laptopPropertyList.map { laptopProperties ->
        breakLaptopStringListFields(laptopProperties.drop(1)) + ""
    }
}

fun fixLaptopStringListFields(fields: List<String>): List<String> {
    if (fields.size < 13) {
        return listOf()
    }

    val fixedFields = fields.toMutableList()
    // screen diagonal
    fixedFields[1] = fixedFields[1].removeSuffix("\"")
    // memory sizes with gb suffixes
    fixedFields[8] = fixedFields[8].uppercase().removeSuffix("GB")
    fixedFields[9] = fixedFields[9].uppercase().removeSuffix("GB")
    fixedFields[12] = fixedFields[12].uppercase().removeSuffix("GB")
    // bool fields
    fixedFields[4] = if (fixedFields[4].lowercase() == "tak") "true" else "false"
    return fixedFields
}

fun breakLaptopStringListFields(fields: List<String>): List<String> {
    if (fields.size < 13) {
        return listOf()
    }

    val brokenFields = fields.toMutableList()
    // screen diagonal
    brokenFields[1] += "\""
    // memory sizes with gb suffixes
    brokenFields[8] += "GB"
    brokenFields[9] += "GB"
    brokenFields[12] += "GB"
    // bool fields
    brokenFields[4] = if (brokenFields[4].lowercase() == "true") "tak" else "nie"
    return brokenFields
}