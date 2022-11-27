package com.zsu.ksp.spi.plugin

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo

@Suppress("unused")
class SpiKspLoader : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SpiProcessor(SpiLoaderEnv(environment))
    }
}

class SpiProcessor(private val env: SpiLoaderEnv) : SymbolProcessor {
    private val finder = SpiFinder(env)
    private val generator = SpiFileGenerator(env)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        return when (env.moduleType) {
            SpiLoaderEnv.ModuleType.MAIN -> processMain(resolver)
            SpiLoaderEnv.ModuleType.CHILD -> processChild(resolver)
            SpiLoaderEnv.ModuleType.MIX -> {
                processChild(resolver)
                processMain(resolver)
            }
        }
    }

    private fun processChild(resolver: Resolver): List<KSAnnotated> {
        val singleModuleSpi = finder.findSingleModuleSpi(resolver)
        generator.generateSubSpiBridgeFile(singleModuleSpi)
            ?.writeTo(env.generator, Dependencies.ALL_FILES)
        return emptyList()
    }

    private fun processMain(resolver: Resolver): List<KSAnnotated> {
        val allSpi = finder.findAllSpi(resolver)
        generator.generateMainSpiFile(allSpi)
            ?.writeTo(env.generator, Dependencies.ALL_FILES)
        return emptyList()
    }

}

class SpiLoaderEnv(private val environment: SymbolProcessorEnvironment) {
    enum class ModuleType(val typeName: String) {
        CHILD("child"),
        MAIN("main"),
        MIX("mix"),
    }

    val moduleType: ModuleType by lazy {
        when (environment.options["spi-loader-type"]) {
            "main" -> ModuleType.MAIN
            "mix" -> ModuleType.MIX
            else -> ModuleType.CHILD
        }
    }

    val loaderName: String by lazy {
        environment.options["spi-loader-name"]
            ?: throw IllegalArgumentException("Must specify ksp property [spi-loader-name].")
    }

    val generator: CodeGenerator by lazy { environment.codeGenerator }
}
