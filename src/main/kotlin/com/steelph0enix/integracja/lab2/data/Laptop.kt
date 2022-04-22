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

    fun toStringList(): List<String> {
        return listOf(
            id.toString(),
            manufacturer ?: "",
            screenDiagonalInches?.toString() ?: "",
            screenResolutionString() ?: "",
            screenSurfaceType ?: "",
            hasTouchscreen?.toString()?.lowercase() ?: "",
            cpuName ?: "",
            physicalCoresCount?.toString() ?: "",
            frequencyMHz?.toString() ?: "",
            ramSizeGB?.toString() ?: "",
            hardDriveCapacityGB?.toString() ?: "",
            hardDriveType ?: "",
            gpuName ?: "",
            gpuMemorySizeGB?.toString() ?: "",
            osName ?: "",
            externalDriveType ?: ""
        )
    }

    companion object {
        fun fromStringList(properties: List<String>): Laptop {
            if (properties.size < 16) {
                throw RuntimeException("Not enough fields in the list to convert it to Laptop (expected: 16, actual: ${properties.size})!")
            }

            val laptop = Laptop(0)

            laptop.id = properties[0].toIntOrNull() ?: 0
            laptop.manufacturer = properties[1]
            laptop.screenDiagonalInches = properties[2].toDoubleOrNull()
            laptop.screenResolution = screenResolutionFromString(properties[3])
            laptop.screenSurfaceType = properties[4]
            laptop.hasTouchscreen = properties[5].toBooleanStrictOrNull()
            laptop.cpuName = properties[6]
            laptop.physicalCoresCount = properties[7].toIntOrNull()
            laptop.frequencyMHz = properties[8].toIntOrNull()
            laptop.ramSizeGB = properties[9].toIntOrNull()
            laptop.hardDriveCapacityGB = properties[10].toIntOrNull()
            laptop.hardDriveType = properties[11]
            laptop.gpuName = properties[12]
            laptop.gpuMemorySizeGB = properties[13].toIntOrNull()
            laptop.osName = properties[14]
            laptop.externalDriveType = properties[15]

            return laptop
        }

        fun screenResolutionFromString(resolution: String?): Pair<Int, Int>? {
            val resolutionSplit = resolution?.split("x", ",")
            if (resolutionSplit?.size == 2) {
                val screenWidth = resolutionSplit[0].toInt()
                val screenHeight = resolutionSplit[1].toInt()
                return Pair(screenWidth, screenHeight)
            }

            return null
        }
    }


    fun screenResolutionString(): String? {
        return if (screenResolution != null) "${screenResolution?.first}x${screenResolution?.second}" else null
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
