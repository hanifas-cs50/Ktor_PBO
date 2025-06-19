package com.example

import com.example.routes.authRoutes
import com.example.routes.dashboard
import com.example.routes.mhsRoutes
import com.example.routes.dosenRoutes 
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureRouting() {
  routing {
    get("/") {
      val session = call.sessions.get<UserSession>()

      if (session == null) {
        call.respondText("Hello World!")
      } else {
        call.respondText("Welcome, ${session.userId}")
      }
    }

    authRoutes()
    dashboard()
    mhsRoutes()
    dosenRoutes()
  }
}
