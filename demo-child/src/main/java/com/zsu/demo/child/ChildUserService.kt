package com.zsu.demo.child

import com.zsu.demo.api.UserService

class ChildUserService : UserService {
    override val name: String = "child"
    override fun login(): Boolean = false
}