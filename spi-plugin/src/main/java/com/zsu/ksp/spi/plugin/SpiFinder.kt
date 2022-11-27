package com.zsu.ksp.spi.plugin

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import com.zsu.ksp.spi.api.Spi
import com.zsu.ksp.spi.api.SpiImpl
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val spiAnnotationFqn = Spi::class.java.canonicalName
private val spiImplAnnotationFqn = SpiImpl::class.java.canonicalName

class SpiFinder(private val env: SpiLoaderEnv) {
    @OptIn(KspExperimental::class)
    fun findAllSpi(resolver: Resolver): AllSpi {
        val allGhosts = resolver.getDeclarationsFromPackage(PKG_SINGLE_MODULE_SPI)
        val allModuleSpis = allGhosts.filterIsInstance<KSClassDeclaration>()
            .map { it.annotations }
            .flatten()
            .mapNotNull { it.arguments[0].value?.toString() }
            .map { Json.decodeFromString<SingleModuleSpi>(it) }
        val spiMap = HashMap<String, ArrayList<String>>()
        for (singleModuleSpi in allModuleSpis) {
            singleModuleSpi.allSpiFqn.forEach { spiFqn ->
                spiMap[spiFqn] = arrayListOf()
            }
        }
        for (singleModuleSpi in allModuleSpis) {
            singleModuleSpi.allSpiImpl.forEach { spiImpl ->
                val supers = spiImpl.impls
                for (singleSuper in supers) {
                    spiMap[singleSuper]?.add(spiImpl.implFqn)
                }
            }
        }
        return spiMap.map { SingleSpi(it.key, it.value) }
    }

    fun findSingleModuleSpi(resolver: Resolver): SingleModuleSpi {
        val spis = resolver.getSymbolsWithAnnotation(spiAnnotationFqn)
        val spiImpls = resolver.getSymbolsWithAnnotation(spiImplAnnotationFqn)
        val allSpiFqn = spis.filterIsInstance<KSClassDeclaration>()
            .mapNotNull { it.qualifiedName?.asString() }.toList()
        val allSpiImpl = spiImpls.filterIsInstance<KSClassDeclaration>()
            .map { it.toSpiImpl() }.toList()
        return SingleModuleSpi(allSpiFqn, allSpiImpl)
    }

    private fun KSClassDeclaration.toSpiImpl(): SingleSpiImpl {
        val fqn = requireNotNull(qualifiedName?.asString()) { "local class couldn't be a SpiImpl." }
        val supers = superTypes.map { it.resolve().toClassName().canonicalName }.toList()
        return SingleSpiImpl(fqn, supers)
    }
}