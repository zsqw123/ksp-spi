package com.zsu.ksp.spi.plugin

import kotlinx.serialization.Serializable

@Serializable
class SingleModuleSpi(
    val allSpiFqn: List<String>,
    val allSpiImpl: List<SingleSpiImpl>,
)

@Serializable
class SingleSpiImpl(
    val implFqn: String,
    val impls: List<String>,
)

class SingleSpi(
    val spiFqn: String,
    val spiImpls: List<String>,
)

typealias AllSpi = List<SingleSpi>

internal const val PKG_SINGLE_MODULE_SPI = "com.zsu.ksp.spi.single"