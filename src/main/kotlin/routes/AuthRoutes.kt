package com.example.routes

import com.example.UserSession
import com.example.dao.AdminDAO
import com.example.dao.DosenDAO
import com.example.dao.MahasiswaDAO
import com.example.templates.auth.login
import com.example.templates.auth.register
import com.example.utils.NimGenerator
import com.example.utils.PasswordUtils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.*

fun Route.authRoutes() {

  get("/register") { call.respondHtml { register() } }

  post("/register") {
    val params = call.receiveParameters()
    val nama = params["nama"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing nama")
    val alamat = params["alamat"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing alamat")
    val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing password")

    val nim = NimGenerator.generateNim()
    val hashedPassword = PasswordUtils.hash(password)

    MahasiswaDAO.insertMahasiswa(nim, nama, alamat, hashedPassword)

    call.respondHtml {
      body {
        h1 { +"Registered mahasiswa:" }
        p { +"NIM: $nim" }
        p { +"Nama: $nama" }
        a("/login") { +"Go to Login" }
      }
    }
  }

  get("/register/admin") {
    val existing = AdminDAO.getAdminByUsername("admin")

    if (existing != null) {
      call.respondHtml {
        body {
          h1 { +"Admin already registered." }
          a("/login") { +"Go to Login" }
        }
      }
      return@get
    }

    val hashed = PasswordUtils.hash("testpass")
    val result = AdminDAO.insertAdmin("admin", "Admin", hashed)

    if (result > 0) {
      call.respondHtml {
        body {
          h1 { +"Registered admin successfully" }
          a("/login") { +"Go to Login" }
        }
      }
    } else {
      call.respond(HttpStatusCode.BadRequest, "Admin Register Failed")
    }
  }

  get("/login") {
    val session = call.sessions.get<UserSession>()
    when (session?.role) {
      "admin" -> call.respondRedirect("/admin")
      "dosen" -> call.respondRedirect("/dosen")
      "mahasiswa" -> call.respondRedirect("/mahasiswa")
      else -> call.respondHtml { login() }
    }
  }

  post("/login") {
    val params = call.receiveParameters()
    val nim = params["nim"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing NIM")
    val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Password")

    suspend fun showLoginFailed(message: String = "NIM or password incorrect.") {
      call.respondHtml {
        body {
          h1 { +"Login Failed" }
          p { +message }
          a("/login") { +"Back to Login" }
        }
      }
    }

    val roleHint = nim.substring(2, 4)
    val roleLetter = nim.any { it.isLetter() }

    if (roleLetter) {
      val admin = AdminDAO.getAdminByUsername(nim)
      if (admin == null) return@post showLoginFailed("Numbers only.")
      if (!PasswordUtils.verify(password, admin.password)) return@post showLoginFailed()
      call.sessions.set(UserSession(admin.id_admin.toString(), "admin", admin.nama))
      return@post call.respondRedirect("/admin")
    }

    if (roleHint == "01") {
      val mhs = MahasiswaDAO.getMahasiswaByNim(nim) ?: return@post showLoginFailed()
      if (!PasswordUtils.verify(password, mhs.password)) return@post showLoginFailed()
      call.sessions.set(UserSession(nim, "mahasiswa", mhs.nama))
      return@post call.respondRedirect("/mahasiswa")
    }

    if (roleHint == "05") {
      val dosen = DosenDAO.getDosenByNidn(nim) ?: return@post showLoginFailed()
      if (!PasswordUtils.verify(password, dosen.password)) return@post showLoginFailed()
      call.sessions.set(UserSession(nim, "dosen", dosen.nama))
      return@post call.respondRedirect("/dosen")
    }

    showLoginFailed()
  }

  get("/logout") {
    call.sessions.clear<UserSession>()
    call.respondRedirect("/login")
  }
}
