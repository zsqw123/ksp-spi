@file:Suppress("DEPRECATION_ERROR")

package com.zsu.ksp.spi.api

import com.zsu.ksp.spi.api.RealSpiLoader.Companion.PKG_SPI_LOADER_MAIN_IMPL
import com.zsu.ksp.spi.api.RealSpiLoader.Companion.SPI_LOADER_MAIN_IMPL
import org.intellij.lang.annotations.Language

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Spi

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SpiImpl

@Deprecated("Don't use it direct.", level = DeprecationLevel.ERROR)
@Retention(AnnotationRetention.BINARY)
@Suppress("unused")
annotation class SpiMeta(
    @Language("json")
    val meta: String,
)

object SpiLoader {
    @PublishedApi
    internal val ghostLoader: RealSpiLoader by lazy {
        try {
            Class.forName("$PKG_SPI_LOADER_MAIN_IMPL.$SPI_LOADER_MAIN_IMPL")
                .getDeclaredConstructor().newInstance() as RealSpiLoader
        } catch (e: Exception) {
            e.printStackTrace()
            throw SpiError("Couldn't instance SpiLoader, check ksp configurations.")
        }
    }

    private class SpiError(message: String) : RuntimeException(message)

    inline fun <reified T> getAllImpl(): List<T> {
        return ghostLoader.getAllImpl(T::class.java)
    }
}

@Deprecated("Don't use it direct.", level = DeprecationLevel.ERROR)
@Suppress("unused")
interface RealSpiLoader {
    fun <T> getAllImpl(clazz: Class<T>): List<T>

    companion object {
        const val PKG_SPI_LOADER_MAIN_IMPL = "com.zsu.ksp.spi.inject.main"
        const val SPI_LOADER_MAIN_IMPL = "RealSpiLoaderImpl"
    }
}
