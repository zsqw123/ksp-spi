package com.zsu.ksp.spi.plugin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SingleModuleSpi(
    @SerialName("f")
    val allSpiFqn: List<String>,
    @SerialName("i")
    val allSpiImpl: List<SingleSpiImpl>,
)

@Serializable
class SingleSpiImpl(
    @SerialName("f")
    val implFqn: String,
    @SerialName("i")
    val impls: List<String>,
)

class SingleSpi(
    val spiFqn: String,
    val spiImpls: List<String>,
)

typealias AllSpi = List<SingleSpi>

internal const val PKG_SINGLE_MODULE_SPI = "com.zsu.ksp.spi.single"