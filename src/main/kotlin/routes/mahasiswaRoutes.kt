package routes

import UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import templates.mahasiswa.dashboard
import templates.mahasiswa.matkulDetail

fun Route.mahasiswaRoutes() {

  get("/") {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login")
      return@get
    }

    val krs = KrsDAO.getKrsForMahasiswa(session.userId.toInt())

    call.respondHtml { dashboard(session, krs) }
  }

  get("/matkul/{id}") {
    val session = call.sessions.get<UserSession>()
    val matkulId = call.parameters["id"]?.toIntOrNull()

    if (session == null || matkulId == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }

    val detail = KrsDAO.getMatkulDetailForMahasiswa(session.userId.toInt(), matkulId)

    if (detail == null) {
      call.respond(HttpStatusCode.Forbidden, "Anda tidak terdaftar di matkul ini")
      return@get
    }

    val (matkul, dosen, nilai) = detail

    call.respondHtml { matkulDetail(session, matkul, dosen, nilai) }
  }
}
