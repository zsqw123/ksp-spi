package com.zsu.demo.child

import com.zsu.demo.api.UserService

class BobUserService : UserService {
    override val name: String = "Bob"
    override fun login(): Boolean = false
}