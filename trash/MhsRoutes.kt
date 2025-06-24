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

fun Route.mhsRoutes() {
  route("/admin/user") {
    get("") {
      val mahasiswaList = MahasiswaDAO.getAllMahasiswa()

      call.respondHtml {
        head { title { +"Mahasiswa List" } }
        body {
          nav {
            a("/admin") { +"Dashboard" }
            a("/admin/user") { +"Mahasiswa" }
            a("/admin/dosen") { +"Dosen" }
            a("/admin/admin") { +"Admin" }
            a("/admin/matkul") { +"Matakuliah" }
          }
          h1 { +"Mahasiswa List" }
          table {
            thead {
              tr {
                th { +"No." }
                th { +"NIM" }
                th { +"Nama" }
                th { +"Alamat" }
                th {
                  attributes["colspan"] = "2"
                  +"Actions"
                }
              }
            }
            tbody {
              if (mahasiswaList.isEmpty()) {
                tr {
                  td {
                    attributes["colspan"] = "5"
                    +"No Mahasiswa found."
                  }
                }
              } else {
                mahasiswaList.forEachIndexed { index, mahasiswa ->
                  tr {
                    td { +"$index." }
                      td { +mahasiswa.nim }
                      td { +mahasiswa.nama }
                      td { +mahasiswa.alamat }
                      td { a("/admin/user/edit/${mahasiswa.id_mhs}") { +"Edit" } }
                      td { a("/admin/user/delete/${mahasiswa.nim}") { +"Delete" } }
                  }
              }
              }
            }
          }
        }
      }
    }
    get("/edit/{id}") {
      val id = call.parameters["id"]?.toIntOrNull()

      if (id != null) {
        val mahasiswa = MahasiswaDAO.getMahasiswaById(id)

        if (mahasiswa != null) {
          call.respondHtml {
            head { title { +"Mahasiswa Detail" } }
            body {
              nav {
                a("/admin") { +"Dashboard" }
                a("/admin/user") { +"Mahasiswa" }
                a("/admin/dosen") { +"Dosen" }
                a("/admin/admin") { +"Admin" }
                a("/admin/matkul") { +"Matakuliah" }
              }
              h1 { +"Mahasiswa Detail" }
              table {
                tr {
                  th { +"NIM" }
                  td { +mahasiswa.nim }
                }
                tr {
                  th { +"Nama" }
                  td { +mahasiswa.nama }
                }
                tr {
                  th { +"Alamat" }
                  td { +mahasiswa.alamat }
                }
              }
              a("/admin/user") { +"Back to Mahasiswa List" }
            }
          }
        } else {
          call.respond(HttpStatusCode.NotFound, "Mahasiswa with ID $id not found.")
        }
      } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid user ID, must be an integer")
      }
    }
  }
}
