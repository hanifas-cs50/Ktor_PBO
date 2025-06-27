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
    val username = params["nim"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing NIM")
    val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing Password")

    suspend fun showLoginFailed(message: String = "Username or password incorrect.") {
      call.respondHtml {
        body {
          h1 { +"Login Failed" }
          p { +message }
          a("/login") { +"Back to Login" }
        }
      }
    }

    when {
      username.any { it.isLetter() } -> {
        val admin = AdminDAO.getAdminByUsername(username) ?: return@post showLoginFailed("Numbers only.")
        if (!PasswordUtils.verify(password, admin.password)) return@post showLoginFailed()
        call.sessions.set(
                UserSession(
                        userId = admin.id_admin,
                        username = admin.username,
                        name = admin.nama,
                        role = "admin"
                )
        )
        call.respondRedirect("/admin")
      }
      username.startsWith("01") || username.substring(2, 4) == "01" -> {
        val mhs = MahasiswaDAO.getMahasiswaByNim(username) ?: return@post showLoginFailed()
        if (!PasswordUtils.verify(password, mhs.password)) return@post showLoginFailed()
        call.sessions.set(
                UserSession(
                        userId = mhs.id_mhs,
                        username = mhs.nim,
                        name = mhs.nama,
                        role = "mahasiswa"
                )
        )
        call.respondRedirect("/mahasiswa")
      }
      username.startsWith("05") || username.substring(2, 4) == "05" -> {
        val dosen = DosenDAO.getDosenByNidn(username) ?: return@post showLoginFailed()
        if (!PasswordUtils.verify(password, dosen.password)) return@post showLoginFailed()
        call.sessions.set(
                UserSession(
                        userId = dosen.id_dosen,
                        username = dosen.nidn,
                        name = dosen.nama,
                        role = "dosen"
                )
        )
        call.respondRedirect("/dosen")
      }
      else -> showLoginFailed()
    }
  }

  get("/logout") {
    call.sessions.clear<UserSession>()
    call.respondRedirect("/login")
  }
}
