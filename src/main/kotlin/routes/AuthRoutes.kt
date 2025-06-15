package com.example.routes

import com.example.UserSession
import com.example.utils.NimGenerator
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
  get("/register/admin") {
    val result = AdminDAO.insertAdmin("admin", "Admin", "testpass")

    if (result > 0) {
      call.respondHtml {
        body {
          h1 { +"Registered admin successfully" }
          a("/login") { +"Go to Login" }
        }
      }
    } else {
      call.respondText("Admin Register Failed", status = HttpStatusCode.BadRequest)
    }
  }

  get("/login") {
    call.respondHtml {
      head { title { +"Login" } }
      body {
        h1 { +"Login" }
        form(action = "/login", method = FormMethod.post) {
          p {
            +"NIM:"
            textInput(name = "nim")
          }
          p {
            +"Password:"
            passwordInput(name = "password")
          }
          p { submitInput { value = "Login" } }
        }
      }
    }
  }

  post("/login") {
    val params = call.receiveParameters()
    val nim = params["nim"] ?: return@post call.respondText("Missing NIM", status = HttpStatusCode.BadRequest)
    val password = params["password"] ?: return@post call.respondText("Missing Password", status = HttpStatusCode.BadRequest)

    val sliced = nim.substring(2, 4)

    suspend fun respondLoginFailed(message: String = "NIM or password incorrect.") = call.respondHtml {
      body {
        h1 { +"Login Failed" }
        p { +message }
        a("/login") { +"Back to Login" }
      }
    }

    val isAuthenticated = when {
      sliced == "01" -> {
        val mahasiswa = MahasiswaDAO.getMahasiswaByNim(nim) ?: return@post respondLoginFailed()
        if (PasswordUtils.verify(password, mahasiswa.password)) {
          call.sessions.set(UserSession(nim, "mahasiswa", mahasiswa.nama))
          true
        } else {
          false
        }
      }
  
      sliced == "05" -> {
        val dosen = DosenDAO.getDosenByNidn(nim) ?: return@post respondLoginFailed()
        if (PasswordUtils.verify(password, dosen.password)) {
          call.sessions.set(UserSession(nim, "dosen", dosen.nama))
          true
        } else {
          false
        }
      }
  
      // I want to return "Numbers only" even when password is wrong
      sliced.any { it.isLetter() } -> {
        val admin = AdminDAO.getAdminByUsername(nim) ?: return@post respondLoginFailed("Numbers only.")
        if (PasswordUtils.verify(password, admin.password)) {
          call.sessions.set(UserSession(nim, "admin", admin.nama))
          true
        } else {
          false
        }
      }
  
      else -> false
    }  

    if (!isAuthenticated) return@post respondLoginFailed()
    
    when {
      sliced == "01" -> call.respondRedirect("/user")
      sliced == "05" -> call.respondRedirect("/dosen")
      sliced.any { it.isLetter() } -> call.respondRedirect("/admin")
    }
  }

  get("/register") {
    call.respondHtml {
      head { title { +"Register" } }

      body {
        h1 { +"Register" }
        form(action = "/register", method = FormMethod.post) {
          p {
            +"Nama:"
            textInput {
              name = "nama"
              required = true
            }
          }
          p {
            +"Alamat:"
            textArea {
              name = "alamat"
              rows = "5"
              required = true
            }
          }
          p {
            +"Password:"
            passwordInput {
              name = "password"
              required = true
            }
          }
          p { submitInput { value = "Register" } }
        }
      }
    }
  }

  post("/register") {
    val params = call.receiveParameters()
    val nama = params["nama"] ?: return@post call.respondText("Missing nama", status = HttpStatusCode.BadRequest)
    val alamat = params["alamat"] ?: return@post call.respondText("Missing alamat", status = HttpStatusCode.BadRequest)
    val password = params["password"] ?: return@post call.respondText("Missing password", status = HttpStatusCode.BadRequest)

    val nim = NimGenerator.generateNim()
    val hashedPassword = PasswordUtils.hash(password)

    println("NIM: $nim")
    println("Nama: $nama")
    println("Alamat: $alamat")
    println("Hashed password: $hashedPassword")

    MahasiswaDAO.insertMahasiswa(nim, nama, alamat, hashedPassword)

    call.respondHtml {
      body {
        h1 { +"Registered mahasiswa dengan:" }
        p { +" NIM: $nim dan Nama: $nama" }
        a("/login") { +"Go to Login" }
      }
    }
  }
}
