package routes

import UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import templates.dosen.dashboard
import templates.dosen.nilaiForm

fun Route.dosenRoutes() {
  get("/") {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login") // or return 403
      return@get
    }

    val idMatkul = MatkulDAO.getMatkulIdByDosenId(session.userId.toInt())
    if (idMatkul == null) {
      call.respond(HttpStatusCode.NotFound, "Dosen ini tidak punya matkul")
      return@get
    }
    
    val krs = KrsDAO.getMahasiswaInMatkul(session.userId.toInt(), idMatkul)

    call.respondHtml { dashboard(session, krs) }
  }

  get("/nilai/{mahasiswaId}/{matkulId}") {
    val session = call.sessions.get<UserSession>()
    val mahasiswaId = call.parameters["mahasiswaId"]?.toIntOrNull()
    val matkulId = call.parameters["matkulId"]?.toIntOrNull()

    if (session == null || mahasiswaId == null || matkulId == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }

    if (!MatkulDAO.isMatkulOwnedByDosen(matkulId, session.userId.toInt())) {
      call.respond(HttpStatusCode.Forbidden)
      return@get
    }

    val mahasiswa = MahasiswaDAO.getMahasiswaById(mahasiswaId)
    val matkul = MatkulDAO.getMatkulById(matkulId)
    val nilai = KrsDAO.getNilai(mahasiswaId, matkulId)

    if (mahasiswa == null || matkul == null) {
      call.respond(HttpStatusCode.NotFound)
      return@get
    }

    call.respondHtml { nilaiForm(mahasiswa, matkul, nilai) }
  }

  post("/nilai/{mahasiswaId}/{matkulId}") {
    val session = call.sessions.get<UserSession>()
    val mahasiswaId = call.parameters["mahasiswaId"]?.toIntOrNull()
    val matkulId = call.parameters["matkulId"]?.toIntOrNull()
    val params = call.receiveParameters()
    val nilai = params["nilai"]

    if (session == null || mahasiswaId == null || matkulId == null || nilai == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    if (!MatkulDAO.isMatkulOwnedByDosen(matkulId, session.userId.toInt())) {
      call.respond(HttpStatusCode.Forbidden)
      return@post
    }

    KrsDAO.updateKrs(mahasiswaId, matkulId, nilai)
    call.respondRedirect("/dosen")
  }
}
