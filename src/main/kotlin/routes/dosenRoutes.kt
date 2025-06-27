package com.example.routes

import com.example.UserSession
import com.example.dao.*
import com.example.templates.dosen.addBimbinganPage
import com.example.templates.dosen.bimbinganPage
import com.example.templates.dosen.dashboard
import com.example.templates.dosen.dosenNilaiPage
import com.example.templates.dosen.nilaiForm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

suspend fun ApplicationCall.requireDosenSession(): UserSession? {
  val session = sessions.get<UserSession>()
  if (session == null || session.role != "dosen") {
    respondRedirect("/login")
    return null
  }
  return session
}

fun Route.dosenRoutes() {
  get {
    val session = call.requireDosenSession() ?: return@get
    call.respondHtml { dashboard(session) }
  }

  get("/nilai") {
    val session = call.requireDosenSession() ?: return@get
    val krs = KrsDAO.getMahasiswaInMatkul(session.userId)
    call.respondHtml { dosenNilaiPage(krs) }
  }

  get("/nilai/{mahasiswaId}/{matkulId}") {
    val session = call.requireDosenSession() ?: return@get
    val mahasiswaId = call.parameters["mahasiswaId"]?.toIntOrNull()
    val matkulId = call.parameters["matkulId"]?.toIntOrNull()
    if (mahasiswaId == null || matkulId == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    if (!MatkulDAO.isMatkulOwnedByDosen(matkulId, session.userId)) {
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
    val session = call.requireDosenSession() ?: return@post
    val mahasiswaId = call.parameters["mahasiswaId"]?.toIntOrNull()
    val matkulId = call.parameters["matkulId"]?.toIntOrNull()
    val nilai = call.receiveParameters()["nilai"]
    if (mahasiswaId == null || matkulId == null || nilai.isNullOrBlank()) {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }
    if (!MatkulDAO.isMatkulOwnedByDosen(matkulId, session.userId)) {
      call.respond(HttpStatusCode.Forbidden)
      return@post
    }
    KrsDAO.updateKrs(mahasiswaId, nilai.toInt(), matkulId)
    call.respondRedirect("/dosen/nilai")
  }

  get("/bimbingan") {
    val session = call.requireDosenSession() ?: return@get
    val mahasiswaList = DosenBimbingDAO.getMahasiswaDibimbingByDosen(session.userId)
    call.respondHtml { bimbinganPage(mahasiswaList) }
  }

  get("/bimbingan/add") { call.respondHtml { addBimbinganPage() } }

  post("/bimbingan/add") {
    val session = call.requireDosenSession() ?: return@post
    val nim = call.receiveParameters()["nim"]?.trim()
    if (nim.isNullOrEmpty()) {
      call.respondText("NIM wajib diisi", status = HttpStatusCode.BadRequest)
      return@post
    }
    val mahasiswa = MahasiswaDAO.getMahasiswaByNim(nim)
    if (mahasiswa == null) {
      call.respondText("Mahasiswa tidak ditemukan", status = HttpStatusCode.NotFound)
      return@post
    }
    val success = DosenBimbingDAO.assignPembimbing(mahasiswa.id_mhs, session.userId)
    if (!success) {
      call.respondText("Gagal menambah bimbingan", status = HttpStatusCode.Conflict)
      return@post
    }
    call.respondRedirect("/dosen/bimbingan")
  }

  post("/bimbingan/delete/{id}") {
    val session = call.requireDosenSession() ?: return@post
    val idMhs = call.parameters["id"]?.toIntOrNull()
    if (idMhs == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }
    val success = DosenBimbingDAO.deleteBimbingan(idMhs, session.userId)
    if (!success) {
      call.respondText("Gagal menghapus bimbingan", status = HttpStatusCode.InternalServerError)
      return@post
    }
    call.respondRedirect("/dosen/bimbingan")
  }
}
