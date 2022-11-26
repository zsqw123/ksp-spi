package com.zsu.ksp.spi.plugin

class SingleModuleSpi(
    val allSpiFqn: List<String>,
    val allSpiImpl: List<SingleSpiImpl>,
)

class SingleSpiImpl(
    val implFqn: String,
    val impls: List<String>,
)

class SingleSpi(
    val spiFqn: String,
    val spiImpls: List<String>,
)

typealias AllSpi = ArrayList<SingleSpi>