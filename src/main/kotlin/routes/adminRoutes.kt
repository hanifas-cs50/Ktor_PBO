package com.example.routes

import com.example.UserSession
import com.example.dao.*
import com.example.templates.admin.adminDashboard
import com.example.templates.admin.adminForm
import com.example.templates.admin.dosen.form as dosenForm
import com.example.templates.admin.dosen.index as dosenIndex
import com.example.templates.admin.mahasiswa.form as mahasiswaForm
import com.example.templates.admin.mahasiswa.index as mahasiswaIndex
import com.example.templates.admin.matkul.index as matkulIndex
import com.example.templates.admin.matkul.form as matkulForm
import com.example.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

fun Route.adminRoutes() {
  get {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login")
      return@get
    }

    val mhsCount = MahasiswaDAO.countMahasiswa().toInt()
    val dosenCount = DosenDAO.countDosen().toInt()
    val matkulCount = MatkulDAO.countMatkul().toInt()

    call.respondHtml { adminDashboard(session, mhsCount, dosenCount, matkulCount) }
  }

  get("/account") {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
      call.respondRedirect("/login")
      return@get
    }

    val admin = AdminDAO.getAdminById(session.userId.toInt())
    if (admin == null) {
      call.respond(HttpStatusCode.NotFound, "Admin not found")
      return@get
    }

    call.respondHtml { adminForm(admin) }
  }

  post("/account/{id}") {
    val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respondText("Invalid ID")

    val params = call.receiveParameters()
    val nama = params["nama"] ?: return@post call.respondText("Missing nama")
    val password = params["password"] ?: return@post call.respondText("Missing password")

    AdminDAO.updateAdmin(id, nama, password)
    call.respondRedirect("/admin")
  }

  get("/delete/{id}") {
    val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid ID")
    AdminDAO.deleteAdmin(id)
    call.sessions.clear<UserSession>()
    call.respondRedirect("/login")
  }

  route("/mhs") {
    get {
      val list = MahasiswaDAO.getAllMahasiswa()
      call.respondHtml { mahasiswaIndex(list) }
    }

    get("/add") { call.respondHtml { mahasiswaForm() } }

    post("/add") {
      val params = call.receiveParameters()
      val nama = params["nama"] ?: return@post call.respondText("Missing nama", status = HttpStatusCode.BadRequest)
      val alamat = params["alamat"] ?: return@post call.respondText("Missing alamat", status = HttpStatusCode.BadRequest)

      val nim = NimGenerator.generateNim()
      val hashedPassword = PasswordUtils.hash(nim)

      MahasiswaDAO.insertMahasiswa(nim, nama, alamat, hashedPassword)
      call.respondRedirect("/admin/mhs")
    }

    get("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull()
      val mhs = id?.let { MahasiswaDAO.getMahasiswaById(it) }
      if (mhs == null) return@get call.respondText("Mahasiswa not found")
      call.respondHtml { mahasiswaForm(mhs) }
    }

    post("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respondText("Invalid ID")
      val params = call.receiveParameters()
      val nama = params["nama"] ?: return@post call.respondText("Missing Nama")
      val alamat = params["alamat"] ?: return@post call.respondText("Missing alamat")
      val password = params["password"]

      if (!password.isNullOrBlank()) {
        val hashedPassword = PasswordUtils.hash(password)
        MahasiswaDAO.updateMahasiswa(id, nama, alamat, hashedPassword)
      } else {
        MahasiswaDAO.updateMahasiswa(id, nama, alamat)
      }

      call.respondRedirect("/admin/mhs")
    }

    get("/delete/{id}") {
      val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid ID")
      MahasiswaDAO.deleteMahasiswa(id)
      call.respondRedirect("/admin/mhs")
    }
  }

  route("/dosen") {
    get {
      val list = DosenDAO.getAllDosen()
      call.respondHtml { dosenIndex(list) }
    }

    get("/add") { call.respondHtml { dosenForm() } }

    post("/add") {
      val params = call.receiveParameters()
      val nama = params["nama"] ?: return@post call.respondText("Missing nama")
      val alamat = params["alamat"] ?: return@post call.respondText("Missing alamat")

      val nidn = NidnGenerator.generateNidn()
      val hashedPassword = PasswordUtils.hash(nidn)

      DosenDAO.insertDosen(nidn, nama, alamat, hashedPassword)
      call.respondRedirect("/admin/dosen")
    }

    get("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull()
      val dosen = id?.let { DosenDAO.getDosenById(it) }
      if (dosen == null) return@get call.respondText("Dosen not found")
      call.respondHtml { dosenForm(dosen) }
    }

    post("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respondText("Invalid ID")
      val params = call.receiveParameters()
      val nama = params["nama"] ?: return@post call.respondText("Missing nama")
      val alamat = params["alamat"] ?: return@post call.respondText("Missing alamat")
      val password = params["password"]

      if (!password.isNullOrBlank()) {
        val hashedPassword = PasswordUtils.hash(password)
        DosenDAO.updateDosen(id, nama, alamat, hashedPassword)
      } else {
        DosenDAO.updateDosen(id, nama, alamat)
      }

      call.respondRedirect("/admin/dosen")
    }

    get("/delete/{id}") {
      val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid ID")
      DosenDAO.deleteDosen(id)
      call.respondRedirect("/admin/dosen")
    }
  }

  route("/matkul") {
    get {
      val list = MatkulDAO.getAllMatkulWithDosenName()
      call.respondHtml { matkulIndex(list) }
    }

    get("/add") {
      val dosenList = DosenDAO.getAllDosen()
      call.respondHtml { matkulForm(dosenList = dosenList) }
    }

    post("/add") {
      val params = call.receiveParameters()
      val kode = params["kode"] ?: return@post call.respondText("Missing kode")
      val nama = params["nama"] ?: return@post call.respondText("Missing nama")
      val sks = params["sks"]?.toIntOrNull() ?: return@post call.respondText("Invalid sks")
      val dosenId =
              params["id_dosen"]?.toIntOrNull() ?: return@post call.respondText("Invalid dosen ID")
      MatkulDAO.insertMatkul(dosenId, kode, nama, sks)
      call.respondRedirect("/admin/matkul")
    }

    get("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull()
      val matkul = id?.let { MatkulDAO.getMatkulById(it) }
      val dosenList = DosenDAO.getAllDosen()
      if (matkul == null) return@get call.respondText("Matkul not found")
      call.respondHtml { matkulForm(matkul, dosenList) }
    }

    post("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respondText("Invalid ID")
      val params = call.receiveParameters()
      val nama = params["nama"] ?: return@post call.respondText("Missing nama")
      val sks = params["sks"]?.toIntOrNull() ?: return@post call.respondText("Invalid sks")
      val dosenId =
              params["id_dosen"]?.toIntOrNull() ?: return@post call.respondText("Invalid dosen ID")
      MatkulDAO.updateMatkul(id, nama, sks, dosenId)
      call.respondRedirect("/admin/matkul")
    }

    get("/delete/{id}") {
      val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid ID")
      MatkulDAO.deleteMatkul(id)
      call.respondRedirect("/admin/matkul")
    }
  }
}

// get("/admin/add") { call.respondHtml { adminForm() } }

// post("/admin/add") {
//   val params = call.receiveParameters()
//   val nama = params["nama"] ?: return@post call.respondText("Missing nama")
//   val username = params["username"] ?: return@post call.respondText("Missing username")
//   val password = params["password"] ?: return@post call.respondText("Missing password")
//   AdminDAO.insertAdmin(username, nama, password)
//   call.respondRedirect("/admin")
// }
