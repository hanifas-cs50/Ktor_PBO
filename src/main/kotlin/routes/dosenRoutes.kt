package com.example.routes

import com.example.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import com.example.templates.dosen.dashboard
import com.example.templates.dosen.bimbinganIndex
import com.example.templates.dosen.nilaiForm
import com.example.dao.MatkulDAO
import com.example.dao.MahasiswaDAO
import com.example.dao.DosenDAO
import com.example.dao.KrsDAO
import com.example.dao.DosenBimbingDAO

fun Route.dosenRoutes() {
  get {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login") // or return 403
      return@get
    }

    val dosen = DosenDAO.getDosenByNidn(session.userId)
    if (dosen == null) {
      call.respond(HttpStatusCode.NotFound, "Dosen not found")
      return@get
    }

    val idMatkul = MatkulDAO.getMatkulIdByDosenId(dosen.id_dosen)
    if (idMatkul == null) {
      call.respond(HttpStatusCode.NotFound, "Dosen ini tidak punya matkul")
      return@get
    }

    val krs = KrsDAO.getMahasiswaInMatkul(dosen.id_dosen, idMatkul)

    call.respondHtml { dashboard(session, krs) }
  }
  
  get("/bimbingan") {
      val session = call.sessions.get<UserSession>() ?: return@get call.respondRedirect("/login")
      val mahasiswaList = DosenBimbingDAO.getMahasiswaDibimbingByDosen(session.userId.toInt())
      call.respondHtml { bimbinganIndex(mahasiswaList) }
  }

  post("/bimbingan/delete/{id}") {
      val session = call.sessions.get<UserSession>() ?: return@post call.respondRedirect("/login")
      val idMhs = call.parameters["id"]?.toIntOrNull()
          ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid Mahasiswa ID")

      val success = DosenBimbingDAO.deleteBimbingan(idMhs, session.userId.toInt())
      if (!success) {
          call.respondText("Failed to delete bimbingan", status = HttpStatusCode.InternalServerError)
      } else {
          call.respondRedirect("/dosen/bimbingan")
      }
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
