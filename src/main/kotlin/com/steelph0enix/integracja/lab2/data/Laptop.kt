package com.steelph0enix.integracja.lab2.data

class Laptop(
    var id: Int,
    var manufacturer: String? = null,
    var screenDiagonalInches: Double? = null,
    var screenResolution: Pair<Int, Int>? = null,
    var screenSurfaceType: String? = null,
    var hasTouchscreen: Boolean? = null,
    var cpuName: String? = null,
    var physicalCoresCount: Int? = null,
    var frequencyMHz: Int? = null,
    var ramSizeGB: Int? = null,
    var hardDriveCapacityGB: Int? = null,
    var hardDriveType: String? = null,
    var gpuName: String? = null,
    var gpuMemorySizeGB: Int? = null,
    var osName: String? = null,
    var externalDriveType: String? = null
) {
    fun screenResolutionFromString(resolution: String): Boolean {
        val resolutionSplit = resolution.split("x", ",")
        if (resolutionSplit.size == 2) {
            val screenWidth = resolutionSplit[0].toInt()
            val screenHeight = resolutionSplit[1].toInt()
            screenResolution = Pair(screenWidth, screenHeight)
            return true
        }

        return false
    }

    fun screenResolutionString(): String? {
        return if (screenResolution != null) "${screenResolution?.first}x${screenResolution?.second}" else null
    }

    fun fromStringList(properties: List<String>) {
        if (properties.size < 16) {
            throw RuntimeException("Not enough fields in the list to convert it to Laptop (expected: 16, actual: ${properties.size})!")
        }

        id = properties[0].toIntOrNull() ?: 0
        manufacturer = properties[1]
        screenDiagonalInches = properties[2].toDoubleOrNull()
        screenResolutionFromString(properties[3])
        screenSurfaceType = properties[4]
        hasTouchscreen = properties[5].toBooleanStrictOrNull()
        cpuName = properties[6]
        physicalCoresCount = properties[7].toIntOrNull()
        frequencyMHz = properties[8].toIntOrNull()
        ramSizeGB = properties[9].toIntOrNull()
        hardDriveCapacityGB = properties[10].toIntOrNull()
        hardDriveType = properties[11]
        gpuName = properties[12]
        gpuMemorySizeGB = properties[13].toIntOrNull()
        osName = properties[14]
        externalDriveType = properties[15]
    }

    override fun toString(): String =
        """Laptop #$id, manufacturer: $manufacturer
    Screen: $screenDiagonalInches", resolution: ${screenResolutionString()}, type: $screenSurfaceType, has touch: $hasTouchscreen
    CPU: $cpuName, $physicalCoresCount cores, ${frequencyMHz}MHz
    RAM size: $ramSizeGB
    HDD: ${hardDriveCapacityGB}GB, type: $hardDriveType
    GPU: $gpuName, memory: ${gpuMemorySizeGB}GB
    OS: $osName
    External drive: $externalDriveType
"""
}
