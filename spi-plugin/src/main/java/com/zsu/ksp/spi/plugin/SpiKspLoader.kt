package com.zsu.ksp.spi.plugin

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

@Suppress("unused")
class SpiKspLoader : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SpiProcessor(SpiLoaderEnv(environment))
    }
}

@OptIn(KspExperimental::class)
class SpiProcessor(private val env: SpiLoaderEnv) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        TODO("Not yet implemented")
    }
}

class SpiLoaderEnv(private val environment: SymbolProcessorEnvironment) {
    enum class ModuleType(val typeName: String) {
        CHILD("child"),
        MAIN("main"),
    }

    val moduleType: ModuleType by lazy {
        val envTypeName = environment.options["spi-loader-type"]
        if (envTypeName == "main") return@lazy ModuleType.MAIN
        return@lazy ModuleType.CHILD
    }

    val loaderName: String = environment.options["spi-loader-name"]
        ?: throw IllegalArgumentException("Must specify ksp property [spi-loader-name].")

    val generator: CodeGenerator by lazy { environment.codeGenerator }
}
