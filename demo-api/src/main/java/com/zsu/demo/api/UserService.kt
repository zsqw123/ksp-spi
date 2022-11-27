package com.zsu.demo.api

import com.zsu.ksp.spi.api.Spi

@Spi
interface UserService {
    val name: String
    fun login(): Boolean
}