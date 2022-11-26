package com.zsu.ksp.spi.plugin

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import com.zsu.ksp.spi.api.Spi
import com.zsu.ksp.spi.api.SpiImpl

private val spiAnnotationFqn = Spi::class.java.canonicalName
private val spiImplAnnotationFqn = SpiImpl::class.java.canonicalName

class SpiFinder(private val env: SpiLoaderEnv) {
    fun findAllSpi(resolver: Resolver): AllSpi {

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