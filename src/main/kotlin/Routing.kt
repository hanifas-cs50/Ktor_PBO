package routes

import UserSession
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
    route("/admin") { adminRoutes() }
    route("/mahasiswa") { mahasiswaRoutes() }
    route("/dosen") { dosenRoutes() }
  }
}
