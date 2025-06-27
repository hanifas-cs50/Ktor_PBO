package com.example

import com.example.routes.configureRouting
import com.example.utils.Database
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Int,
    val username: String,
    val name: String,
    val role: String
)

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  install(Sessions) {
    cookie<UserSession>("user_session") {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 3600
      cookie.httpOnly = true // Prevent JavaScript access (XSS protection)
    }
  }
  Database.init()
  configureRouting()
}
