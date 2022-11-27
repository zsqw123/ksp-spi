package com.zsu.demo.child

import com.zsu.demo.api.UserService
import com.zsu.ksp.spi.api.SpiImpl

@SpiImpl
class BobUserService : UserService {
    override val name: String = "Bob"
    override fun login(): Boolean = false
}