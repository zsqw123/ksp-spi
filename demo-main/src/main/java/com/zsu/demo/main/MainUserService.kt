package com.zsu.demo.main

import com.zsu.demo.api.UserService

class MainUserService : UserService {
    override val name: String = "main"
    override fun login(): Boolean = true
}