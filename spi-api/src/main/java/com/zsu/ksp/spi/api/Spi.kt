package com.zsu.ksp.spi.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Spi

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SpiImpl

object SpiLoader {
    @PublishedApi
    internal val ghostLoader: RealSpiLoader by lazy {
        try {
            Class.forName("com.zsu.ksp.spi.inject.RealSpiLoaderImpl")
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

@PublishedApi
internal interface RealSpiLoader {
    fun <T> getAllImpl(clazz: Class<T>): List<T>
}