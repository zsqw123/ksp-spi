package com.zsu.demo.api

interface UserService {
    val name: String
    fun login(): Boolean
}