package com.example.routes

import com.example.UserSession
import com.example.templates.guestIndex
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun RoleGuard(requiredRole: String) = createRouteScopedPlugin("RoleGuard") {
  onCall { call ->
    val session = call.sessions.get<UserSession>()
    if (session == null || session.role != requiredRole) {
      call.respondRedirect("/login")
      return@onCall
    }
  }
}

fun Application.configureRouting() {
  routing {
    static("/static") {
      resources("static")
    }

    get("/") {
      val session = call.sessions.get<UserSession>()
      if (session == null) {
        call.respondHtml { guestIndex() }
      } else {
        call.respondHtml { guestIndex(session.username) }
      }
    }

    authRoutes()
    
    route("/admin") {
      install(RoleGuard("admin"))
      adminRoutes()
    }

    route("/dosen") {
      install(RoleGuard("dosen"))
      dosenRoutes()
    }

    route("/mahasiswa") {
      install(RoleGuard("mahasiswa"))
      mahasiswaRoutes()
    }
  }
}
