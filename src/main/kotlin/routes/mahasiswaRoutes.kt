package com.example.routes

import com.example.UserSession
import com.example.dao.DosenBimbingDAO
import com.example.dao.KrsDAO
import com.example.dao.MatkulDAO
import com.example.templates.mahasiswa.dashboard
import com.example.templates.mahasiswa.matkulDetail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

fun Route.mahasiswaRoutes() {

  get {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login")
      return@get
    }

    val krs = KrsDAO.getKrsForMahasiswa(session.userId.toInt())
    val pembimbing = DosenBimbingDAO.getPembimbingByMahasiswaId(session.userId.toInt())

    call.respondHtml { dashboard(session, krs, pembimbing) }
  }

  post("/krs") {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login")
      return@post
    }

    val params = call.receiveParameters()
    val kodeMatkul = params["kode_matkul"]?.trim()

    if (kodeMatkul.isNullOrEmpty()) {
      call.respondText("Kode Mata Kuliah wajib diisi", status = HttpStatusCode.BadRequest)
      return@post
    }

    // Get matkul ID from kode
    val matkul = MatkulDAO.getMatkulByKode(kodeMatkul)
    if (matkul == null) {
      call.respondText("Mata kuliah tidak ditemukan", status = HttpStatusCode.NotFound)
      return@post
    }

    val inserted = KrsDAO.insertKrs(session.userId.toInt(), matkul.id_matkul)

    if (inserted <= 0) {
      call.respondText(
              "Gagal registrasi. Mungkin sudah terdaftar?",
              status = HttpStatusCode.Conflict
      )
      return@post
    }

    call.respondRedirect("/mahasiswa")
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
