package com.steelph0enix.integracja.lab2.models

import org.jetbrains.exposed.dao.id.IntIdTable

object LaptopDBTable : IntIdTable() {
    val manufacturer = varchar("manufacturer", 100).nullable()
    val screenDiagonalInches = double("screenDiagonalInches").nullable()
    val screenResolution = varchar("screenResolution", 20).nullable()
    val screenSurfaceType = varchar("screenSurfaceType", 50).nullable()
    val hasTouchscreen = bool("hasTouchscreen").nullable()
    val cpuName = varchar("cpuName", 100).nullable()
    val physicalCoresCount = integer("physicalCoresCount").nullable()
    val cpuFrequencyMHz = integer("cpuFrequencyMHz").nullable()
    val ramSizeGB = integer("ramSizeGB").nullable()
    val hardDriveCapacityGB = integer("hardDriveCapacityGB").nullable()
    val hardDriveType = varchar("hardDriveType", 50).nullable()
    val gpuName = varchar("gpuName", 100).nullable()
    val gpuMemorySizeGB = integer("gpuMemorySizeGB").nullable()
    val osName = varchar("osName", 100).nullable()
    val externalDriveType = varchar("externalDriveType", 100).nullable()
}