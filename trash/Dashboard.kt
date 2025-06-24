package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.*

fun Route.dashboard() {
  get("/admin") {
    call.respondHtml {
      head { title { +"Admin Dashboard" } }
      body {
        nav {
          a("/admin") { +"Dashboard" }
          a("/admin/user") { +"Mahasiswa" }
          a("/admin/dosen") { +"Dosen" }
          a("/admin/admin") { +"Admin" }
          a("/admin/matkul") { +"Matakuliah" }
        }
        h1 { +"Dashboard" }
        div {
          div {
            h3 { +"Mahasiswa" }
            p { +"43" }
          }
          div {
            h3 { +"Dosen" }
            p { +"12" }
          }
          div {
            h3 { +"Admin" }
            p { +"1" }
          }
        }
      }
    }
  }
}
