@file:Suppress("DEPRECATION_ERROR")

package com.zsu.ksp.spi.plugin

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.zsu.ksp.spi.api.RealSpiLoader
import com.zsu.ksp.spi.api.SpiMeta
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SpiFileGenerator(private val env: SpiLoaderEnv) {
    private val fileLevelSuppressAnnotation = AnnotationSpec.builder(Suppress::class)
        .addMember("%S", "DEPRECATION_ERROR").build()

    fun generateSubSpiBridgeFile(singleModuleSpi: SingleModuleSpi): FileSpec? {
        if (singleModuleSpi.allSpiFqn.isEmpty() &&
            singleModuleSpi.allSpiImpl.isEmpty()
        ) return null
        val ghostAnnotation = AnnotationSpec
            .builder(SpiMeta::class.asTypeName())
            .addMember("meta = %S", Json.encodeToString(singleModuleSpi))
            .build()
        val ghostClass = TypeSpec.classBuilder(env.loaderName)
            .addAnnotation(ghostAnnotation)
            .build()
        return FileSpec.builder(PKG_SINGLE_MODULE_SPI, env.loaderName)
            .addAnnotation(fileLevelSuppressAnnotation)
            .addType(ghostClass)
            .build()
    }

    fun generateMainSpiFile(allSpi: AllSpi): FileSpec? {
        if (allSpi.isEmpty()) return null
        val tTypeVariable = TypeVariableName("T")

        val allImplFunBuilder = FunSpec.builder("getAllImpl")
            .addModifiers(KModifier.OVERRIDE)
            .addTypeVariable(tTypeVariable)
            .addParameter("clazz", Class::class.asTypeName().parameterizedBy(tTypeVariable))
            .addStatement("val result = when (clazz.canonicalName) {")
            .returns(List::class.asTypeName().parameterizedBy(tTypeVariable))

        for (singleSpi in allSpi) allImplFunBuilder.apply {
            val singleSpiImpls = singleSpi.spiImpls.joinToString(", ") { "$it()" }
            addStatement("%S -> listOf($singleSpiImpls)", singleSpi.spiFqn)
        }

        allImplFunBuilder.apply {
            addStatement("else -> emptyList<T>()")
            addStatement("}")
            addStatement("return result")
        }

        val realLoaderClassName = TypeSpec.classBuilder(RealSpiLoader.SPI_LOADER_REAL_IMPL)
            .addSuperinterface(RealSpiLoader::class)
            .addFunction(allImplFunBuilder.build())
            .build()

        return FileSpec.builder(RealSpiLoader.PKG_SPI_LOADER_REAL_IMPL, RealSpiLoader.SPI_LOADER_REAL_IMPL)
            .addAnnotation(fileLevelSuppressAnnotation)
            .addType(realLoaderClassName)
            .build()
    }
}
