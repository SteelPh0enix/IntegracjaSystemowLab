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
    var cpuFrequencyMHz: Int? = null,
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
            cpuFrequencyMHz?.toString() ?: "",
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
            laptop.cpuFrequencyMHz = properties[8].toIntOrNull()
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
    CPU: $cpuName, $physicalCoresCount cores, ${cpuFrequencyMHz}MHz
    RAM size: $ramSizeGB
    HDD: ${hardDriveCapacityGB}GB, type: $hardDriveType
    GPU: $gpuName, memory: ${gpuMemorySizeGB}GB
    OS: $osName
    External drive: $externalDriveType
"""

    fun equalsExceptID(other: Laptop): Boolean {
        return other.manufacturer == manufacturer &&
                other.screenDiagonalInches == screenDiagonalInches &&
                other.screenResolution == screenResolution &&
                other.screenSurfaceType == screenSurfaceType &&
                other.hasTouchscreen == hasTouchscreen &&
                other.cpuName == cpuName &&
                other.physicalCoresCount == physicalCoresCount &&
                other.cpuFrequencyMHz == cpuFrequencyMHz &&
                other.ramSizeGB == ramSizeGB &&
                other.hardDriveCapacityGB == hardDriveCapacityGB &&
                other.hardDriveType == hardDriveType &&
                other.gpuName == gpuName &&
                other.gpuMemorySizeGB == gpuMemorySizeGB &&
                other.osName == osName &&
                other.externalDriveType == externalDriveType
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Laptop

        if (id != other.id) return false
        if (manufacturer != other.manufacturer) return false
        if (screenDiagonalInches != other.screenDiagonalInches) return false
        if (screenResolution != other.screenResolution) return false
        if (screenSurfaceType != other.screenSurfaceType) return false
        if (hasTouchscreen != other.hasTouchscreen) return false
        if (cpuName != other.cpuName) return false
        if (physicalCoresCount != other.physicalCoresCount) return false
        if (cpuFrequencyMHz != other.cpuFrequencyMHz) return false
        if (ramSizeGB != other.ramSizeGB) return false
        if (hardDriveCapacityGB != other.hardDriveCapacityGB) return false
        if (hardDriveType != other.hardDriveType) return false
        if (gpuName != other.gpuName) return false
        if (gpuMemorySizeGB != other.gpuMemorySizeGB) return false
        if (osName != other.osName) return false
        if (externalDriveType != other.externalDriveType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (manufacturer?.hashCode() ?: 0)
        result = 31 * result + (screenDiagonalInches?.hashCode() ?: 0)
        result = 31 * result + (screenResolution?.hashCode() ?: 0)
        result = 31 * result + (screenSurfaceType?.hashCode() ?: 0)
        result = 31 * result + (hasTouchscreen?.hashCode() ?: 0)
        result = 31 * result + (cpuName?.hashCode() ?: 0)
        result = 31 * result + (physicalCoresCount ?: 0)
        result = 31 * result + (cpuFrequencyMHz ?: 0)
        result = 31 * result + (ramSizeGB ?: 0)
        result = 31 * result + (hardDriveCapacityGB ?: 0)
        result = 31 * result + (hardDriveType?.hashCode() ?: 0)
        result = 31 * result + (gpuName?.hashCode() ?: 0)
        result = 31 * result + (gpuMemorySizeGB ?: 0)
        result = 31 * result + (osName?.hashCode() ?: 0)
        result = 31 * result + (externalDriveType?.hashCode() ?: 0)
        return result
    }
}
