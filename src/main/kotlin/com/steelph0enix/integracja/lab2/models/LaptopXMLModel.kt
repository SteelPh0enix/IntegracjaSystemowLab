package com.steelph0enix.integracja.lab2.models

import com.steelph0enix.integracja.lab2.data.Laptop
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Root(name = "laptops", strict = false)
class LaptopListXMLModel(
    @field:ElementList(name = "laptop", inline = true)
    @param:ElementList(name = "laptop", inline = true)
    val laptopList: List<LaptopXMLModel> = listOf()
) {
    @field:Attribute
    var moddate: String = generateModdate()

    private fun generateModdate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm")
        return current.format(formatter)
    }
}

@Root(name = "laptop", strict = false)
data class LaptopXMLModel(
    @field:Attribute(name = "id", required = true)
    @param:Attribute(name = "id", required = true)
    val id: Int = 0,

    @field:Element(name = "manufacturer", required = false)
    @param:Element(name = "manufacturer", required = false)
    val manufacturer: String? = null,

    @field:Element(name = "screen", required = true)
    @param:Element(name = "screen", required = true)
    val screen: ScreenXMLModel = ScreenXMLModel(),

    @field:Element(name = "processor", required = true)
    @param:Element(name = "processor", required = true)
    val processor: CPUXMLModel = CPUXMLModel(),

    @field:Element(name = "ram", required = false)
    @param:Element(name = "ram", required = false)
    val ram: String? = null,

    @field:Element(name = "disc", required = true)
    @param:Element(name = "disc", required = true)
    val disc: DiscXMLModel = DiscXMLModel(),

    @field:Element(name = "graphic_card", required = true)
    @param:Element(name = "graphic_card", required = true)
    val graphic_card: GPUXMLModel = GPUXMLModel(),

    @field:Element(name = "os", required = false)
    @param:Element(name = "os", required = false)
    val os: String? = null,

    @field:Element(name = "disc_reader", required = false)
    @param:Element(name = "disc_reader", required = false)
    val disc_reader: String? = null
)

@Root(strict = false)
class ScreenXMLModel(
    @field:Attribute(name = "touch", required = false)
    @param:Attribute(name = "touch", required = false)
    val touch: String? = null,

    @field:Element(name = "size", required = false)
    @param:Element(name = "size", required = false)
    val size: String? = null,

    @field:Element(name = "resolution", required = false)
    @param:Element(name = "resolution", required = false)
    val resolution: String? = null,

    @field:Element(name = "type", required = false)
    @param:Element(name = "type", required = false)
    val type: String? = null
)

@Root(strict = false)
class CPUXMLModel(
    @field:Element(name = "name", required = false)
    @param:Element(name = "name", required = false)
    val name: String? = null,

    @field:Element(name = "physical_cores", required = false)
    @param:Element(name = "physical_cores", required = false)
    val physical_cores: Int? = null,

    @field:Element(name = "clock_speed", required = false)
    @param:Element(name = "clock_speed", required = false)
    val clock_speed: Int? = null
)

@Root(strict = false)
class DiscXMLModel(
    @field:Attribute(name = "type", required = false)
    @param:Attribute(name = "type", required = false)
    val type: String? = null,

    @field:Element(name = "storage", required = false)
    @param:Element(name = "storage", required = false)
    val storage: String? = null
)

@Root(strict = false)
class GPUXMLModel(
    @field:Element(name = "name", required = false)
    @param:Element(name = "name", required = false)
    val name: String? = null,

    @field:Element(name = "memory", required = false)
    @param:Element(name = "memory", required = false)
    val memory: String? = null
)

fun laptopToXMLModel(laptop: Laptop): LaptopXMLModel {
    return LaptopXMLModel(
        id = laptop.id,
        manufacturer = laptop.manufacturer,
        screen = ScreenXMLModel(
            touch = laptop.hasTouchscreen?.let { if (it) "yes" else "no" },
            size = laptop.screenDiagonalInches?.toString(),
            resolution = laptop.screenResolutionString(),
            type = laptop.screenSurfaceType
        ),
        processor = CPUXMLModel(
            name = laptop.cpuName,
            physical_cores = laptop.physicalCoresCount,
            clock_speed = laptop.frequencyMHz
        ),
        ram = laptop.ramSizeGB?.let { it.toString() + "GB" },
        disc = DiscXMLModel(
            type = laptop.hardDriveType,
            storage = laptop.hardDriveCapacityGB?.let { it.toString() + "GB" }
        ),
        graphic_card = GPUXMLModel(
            name = laptop.gpuName,
            memory = laptop.gpuMemorySizeGB?.let { it.toString() + "GB" }
        ),
        os = laptop.osName,
        disc_reader = laptop.externalDriveType
    )
}

fun laptopFromXMLModel(laptopXML: LaptopXMLModel): Laptop {
    return Laptop(
        id = laptopXML.id,
        manufacturer = laptopXML.manufacturer,
        screenDiagonalInches = laptopXML.screen.size?.toDouble(),
        screenResolution = Laptop.screenResolutionFromString(laptopXML.screen.resolution),
        screenSurfaceType = laptopXML.screen.type,
        hasTouchscreen = laptopXML.screen.touch?.let { it == "yes" },
        cpuName = laptopXML.processor.name,
        physicalCoresCount = laptopXML.processor.physical_cores,
        frequencyMHz = laptopXML.processor.clock_speed,
        ramSizeGB = laptopXML.ram?.uppercase()?.removeSuffix("GB")?.toIntOrNull(),
        hardDriveCapacityGB = laptopXML.disc.storage?.uppercase()?.removeSuffix("GB")?.toIntOrNull(),
        hardDriveType = laptopXML.disc.type,
        gpuName = laptopXML.graphic_card.name,
        gpuMemorySizeGB = laptopXML.graphic_card.memory?.uppercase()?.removeSuffix("GB")?.toIntOrNull(),
        osName = laptopXML.os,
        externalDriveType = laptopXML.disc_reader
    )
}