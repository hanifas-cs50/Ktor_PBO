package routes

import AdminDAO
import UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import templates.admin.adminDashboard
import templates.admin.adminForm
import templates.admin.mahasiswa.form
import templates.admin.mahasiswa.index
import templates.admin.dosen.index as dosenIndex
import templates.admin.dosen.form as dosenForm
import utils.NimGenerator
import utils.NidnGenerator

fun Route.adminRoutes() {
  get("/") {
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
    call.respondRedirect("/admin")
  }

  route("/mhs") {
    get {
      val list = MahasiswaDAO.getAllMahasiswa()
      call.respondHtml { index(list) }
    }

    get("/add") { call.respondHtml { form() } }

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
      call.respondHtml { form(mhs) }
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

    get("/add") {
      call.respondHtml { dosenForm() }
    }

    post("/add") {
      val params = call.receiveParameters()
      val nama = params["nama"] ?: return@post call.respondText("Missing nama")
      val alamat = params["alamat"] ?: return@post call.respondText("Missing alamat")
      val password = params["password"] ?: return@post call.respondText("Missing password")

      val nidn = NidnGenerator.generateNidn();

      DosenDAO.insertDosen(nidn, nama, alamat, password)
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
