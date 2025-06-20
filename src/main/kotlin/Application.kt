package com.example

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import com.example.configureRouting
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
  val userId: String,
  val role: String,
  val name: String
)

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  install(Sessions) {
    cookie<UserSession>("user_session") {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 3600
      // cookie.httpOnly = true // Prevent JavaScript access (XSS protection)
    }
  }
  Database.init()
  configureRouting()
}
