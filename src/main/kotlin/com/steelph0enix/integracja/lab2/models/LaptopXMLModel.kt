package com.steelph0enix.integracja.lab2.models

import com.steelph0enix.integracja.lab2.data.Laptop
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Root(name="laptops")
class LaptopListXMLModel(@field:ElementList(inline = true) val laptopList: List<LaptopXMLModel>) {
    @field:Attribute
    var moddate: String = generateModdate()

    private fun generateModdate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm")
        return current.format(formatter)
    }
}

@Root(name="laptop")
class LaptopXMLModel(
    @field:Attribute val id: Int,
    @field:Element(required = false) val manufacturer: String?,
    @field:Element val screen: ScreenXMLModel,
    @field:Element val processor: CPUXMLModel,
    @field:Element(required = false) val ram: String?,
    @field:Element val disc: DiscXMLModel,
    @field:Element val graphic_card: GPUXMLModel,
    @field:Element(required = false) val os: String?,
    @field:Element(required = false) val disc_reader: String?
)

class ScreenXMLModel(
    @field:Attribute(required = false) val touch: String?,
    @field:Element(required = false) val size: String?,
    @field:Element(required = false) val resolution: String?,
    @field:Element(required = false) val type: String?
)

class CPUXMLModel(
    @field:Element(required = false) val name: String?,
    @field:Element(required = false) val physical_cores: Int?,
    @field:Element(required = false) val clock_speed: Int?
)

class DiscXMLModel(
    @field:Attribute(required = false) val type: String?,
    @field:Element(required = false) val storage: String?
)

class GPUXMLModel(
    @field:Element(required = false) val name: String?,
    @field:Element(required = false) val memory: String?
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