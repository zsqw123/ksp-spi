package com.zsu.demo.main

import com.zsu.demo.api.UserService
import com.zsu.ksp.spi.api.SpiImpl

@SpiImpl
class AliceUserService : UserService {
    override val name: String = "Alice"
    override fun login(): Boolean = true
}