package com.example.routes

// import com.example.templates.mahasiswa.matkulDetail
import com.example.UserSession
import com.example.dao.KrsDAO
import com.example.dao.MatkulDAO
import com.example.templates.mahasiswa.dashboard
import com.example.templates.mahasiswa.krsPage
import com.example.templates.mahasiswa.nilaiPage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

suspend fun ApplicationCall.requireMahasiswaSession(): UserSession? {
  val session = sessions.get<UserSession>()
  if (session == null || session.role != "mahasiswa") {
    respondRedirect("/login")
    return null
  }
  return session
}

fun Route.mahasiswaRoutes() {
  get {
    val session = call.requireMahasiswaSession() ?: return@get
    call.respondHtml { dashboard(session) }
  }

  get("/krs") {
    val session = call.requireMahasiswaSession() ?: return@get
    val krs = KrsDAO.getKrsForMahasiswa(session.userId).filter { it.second == null } // only belum dinilai
    call.respondHtml { krsPage(krs) }
  }

  post("/krs") {
    val session = call.requireMahasiswaSession() ?: return@post
    val kode = call.receiveParameters()["kode_matkul"]?.trim()
    if (kode.isNullOrEmpty()) {
      call.respondText("Kode Mata Kuliah wajib diisi", status = HttpStatusCode.BadRequest)
      return@post
    }

    val matkul = MatkulDAO.getMatkulByKode(kode)
    if (matkul == null) {
      call.respondText("Mata Kuliah tidak ditemukan", status = HttpStatusCode.NotFound)
      return@post
    }
        
    val inserted = KrsDAO.insertKrs(session.userId, matkul.id_matkul)
    if (inserted <= 0) {
      call.respondText(
        "Gagal registrasi. Anda sudah mengambil mata kuliah ini.",
        status = HttpStatusCode.Conflict
      )
      return@post
    }
    call.respondRedirect("/mahasiswa/krs")
  }

  get("/nilai") {
    val session = call.requireMahasiswaSession() ?: return@get
    val krs = KrsDAO
      .getKrsForMahasiswa(session.userId)
      .filter { it.second != null } // sudah dinilai
      .map { it.first to (it.second ?: 0) }
    call.respondHtml { nilaiPage(krs) }
  }
}
