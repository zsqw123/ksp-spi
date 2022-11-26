package com.zsu.demo.main

import com.zsu.demo.api.UserService
import com.zsu.ksp.spi.api.SpiLoader

fun main() {
    println("Start Login...")
    val allUsers = SpiLoader.getAllImpl<UserService>()
    val outputContent = allUsers.joinToString("\n") {
        "${it.name}, login success: ${it.login()}"
    }
    println(outputContent)
    println("End Login")
}