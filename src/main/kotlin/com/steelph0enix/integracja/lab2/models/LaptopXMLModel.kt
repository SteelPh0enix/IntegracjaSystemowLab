package com.steelph0enix.integracja.lab2.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Root
class LaptopListXMLModel(@property:ElementList(inline = true) val laptopList: List<LaptopXMLModel>) {
    @Attribute
    val moddate: String

    init {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd T HH:mm")
        moddate = current.format(formatter)
    }
}

class LaptopXMLModel(
    @property:Attribute val id: Int,
    @property:Element val manufacturer: String,
    @property:Element val screen: ScreenXMLModel,
    @property:Element val processor: CPUXMLModel,
    @property:Element val ram: String,
    @property:Element val disc: DiscXMLModel,
    @property:Element val graphic_card: GPUXMLModel,
    @property:Element val os: String,
    @property:Element val disc_reader: String
    ) {
}

class ScreenXMLModel(
    @property:Attribute val touch: String,
    @property:Element val size: String,
    @property:Element val resolution: String,
    @property:Element val type: String
)

class CPUXMLModel(
    @property:Element val name: String,
    @property:Element val physical_cores: Int,
    @property:Element val clock_speed: Int
)

class DiscXMLModel(
    @property:Attribute val type: String,
    @property:Element val storage: String
)

class GPUXMLModel(
    @property:Element val name: String,
    @property:Element val memory: String
)