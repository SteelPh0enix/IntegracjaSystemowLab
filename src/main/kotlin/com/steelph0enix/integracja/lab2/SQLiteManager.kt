package com.steelph0enix.integracja.lab2

import com.steelph0enix.integracja.lab2.data.Laptop
import com.steelph0enix.integracja.lab2.models.LaptopDBTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

class SQLiteManager {
    private fun resultRowToLaptop(result: ResultRow): Laptop {
        return Laptop(
            id = result[LaptopDBTable.id].value,
            manufacturer = result[LaptopDBTable.manufacturer],
            screenDiagonalInches = result[LaptopDBTable.screenDiagonalInches],
            screenResolution = Laptop.screenResolutionFromString(result[LaptopDBTable.screenResolution]),
            screenSurfaceType = result[LaptopDBTable.screenSurfaceType],
            hasTouchscreen = result[LaptopDBTable.hasTouchscreen],
            cpuName = result[LaptopDBTable.cpuName],
            physicalCoresCount = result[LaptopDBTable.physicalCoresCount],
            cpuFrequencyMHz = result[LaptopDBTable.cpuFrequencyMHz],
            ramSizeGB = result[LaptopDBTable.ramSizeGB],
            hardDriveCapacityGB = result[LaptopDBTable.hardDriveCapacityGB],
            hardDriveType = result[LaptopDBTable.hardDriveType],
            gpuName = result[LaptopDBTable.gpuName],
            gpuMemorySizeGB = result[LaptopDBTable.gpuMemorySizeGB],
            osName = result[LaptopDBTable.osName],
            externalDriveType = result[LaptopDBTable.externalDriveType],
        )
    }

    fun readLaptopListFromDatabase(databasePath: String): List<Laptop> {
        if (!File(databasePath).exists()) {
            throw Exception("Selected database file does not exists!")
        }

        Database.connect("jdbc:sqlite:$databasePath", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        val laptopList = mutableListOf<Laptop>()

        transaction {
            val laptopDBList = LaptopDBTable.selectAll()
            laptopDBList.map {
                laptopList += resultRowToLaptop(it)
            }
        }

        return laptopList
    }

    private fun findLastBackupFile(databasePath: String, maxBackups: Int = 100): String? {
        var backupIndex = 1;
        while (File("$databasePath.backup$backupIndex").exists() && backupIndex <= maxBackups) {
            backupIndex++
        }

        if (backupIndex > maxBackups) {
            return null
        }
        return "$databasePath.backup$backupIndex"
    }

    private fun backupExistingDatabaseIfExists(
        databasePath: String,
        deleteOriginal: Boolean = true,
    ) {
        val dbFile = File(databasePath)
        if (dbFile.exists()) {
            val lastBackupFile = findLastBackupFile(databasePath)
            if (lastBackupFile != null) {
                val dbBackupFile = File(lastBackupFile)
                dbFile.copyTo(dbBackupFile)
                if (deleteOriginal) {
                    dbFile.delete()
                }
            } else {
                throw Exception("Cannot create backup file, delete some of the existing backups first!")
            }
        }
    }

    fun saveLaptopListToDatabase(databasePath: String, laptopList: List<Laptop>) {
        backupExistingDatabaseIfExists(databasePath)

        Database.connect("jdbc:sqlite:$databasePath", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            SchemaUtils.create(LaptopDBTable)
            LaptopDBTable.batchInsert(laptopList) { laptop ->
                this[LaptopDBTable.id] = laptop.id
                this[LaptopDBTable.manufacturer] = laptop.manufacturer
                this[LaptopDBTable.screenDiagonalInches] = laptop.screenDiagonalInches
                this[LaptopDBTable.screenResolution] = laptop.screenResolutionString()
                this[LaptopDBTable.screenSurfaceType] = laptop.screenSurfaceType
                this[LaptopDBTable.hasTouchscreen] = laptop.hasTouchscreen
                this[LaptopDBTable.cpuName] = laptop.cpuName
                this[LaptopDBTable.physicalCoresCount] = laptop.physicalCoresCount
                this[LaptopDBTable.cpuFrequencyMHz] = laptop.cpuFrequencyMHz
                this[LaptopDBTable.ramSizeGB] = laptop.ramSizeGB
                this[LaptopDBTable.hardDriveCapacityGB] = laptop.hardDriveCapacityGB
                this[LaptopDBTable.hardDriveType] = laptop.hardDriveType
                this[LaptopDBTable.gpuName] = laptop.gpuName
                this[LaptopDBTable.gpuMemorySizeGB] = laptop.gpuMemorySizeGB
                this[LaptopDBTable.osName] = laptop.osName
                this[LaptopDBTable.externalDriveType] = laptop.externalDriveType
            }
        }
    }
}