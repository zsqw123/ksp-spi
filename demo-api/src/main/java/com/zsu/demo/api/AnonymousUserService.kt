package com.zsu.demo.api

import com.zsu.ksp.spi.api.SpiImpl

@SpiImpl
class AnonymousUserService : UserService {
    override val name: String = "Anonymous"
    override fun login(): Boolean = false
}