package com.zsu.demo.main

import com.zsu.demo.api.UserService

class AliceUserService : UserService {
    override val name: String = "Alice"
    override fun login(): Boolean = true
}