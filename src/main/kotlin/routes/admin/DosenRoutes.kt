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

fun Route.dosenRoutes() {
    route("/admin/dosen") {

        // List semua dosen
        get("") {
            val dosenList = DosenDAO.getAllDosen()

            call.respondHtml {
                head { title { +"Daftar Dosen" } }
                body {
                    nav {
                        a("/admin") { +"Dashboard" }
                        a("/admin/user") { +"Mahasiswa" }
                        a("/admin/dosen") { +"Dosen" }
                        a("/admin/admin") { +"Admin" }
                        a("/admin/matkul") { +"Matakuliah" }
                    }
                    h1 { +"Daftar Dosen" }
                    table {
                        thead {
                            tr {
                                th { +"No." }
                                th { +"NIDN" }
                                th { +"Nama" }
                                th { +"Alamat" }
                                th {
                                    attributes["colspan"] = "2"
                                    +"Actions"
                                }
                            }
                        }
                        tbody {
                            if (dosenList.isEmpty()) {
                                tr {
                                    td {
                                        attributes["colspan"] = "5"
                                        +"Tidak ada data dosen."
                                    }
                                }
                            } else {
                                dosenList.forEachIndexed { index, dosen ->
                                    tr {
                                        td { +"${index + 1}" }
                                        td { +dosen.nidn }
                                        td { +dosen.nama }
                                        td { +dosen.alamat }
                                        td { a("/admin/dosen/edit/${dosen.id_dosen}") { +"Edit" } }
                                        td { a("/admin/dosen/delete/${dosen.id_dosen}") { +"Delete" } }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tampilkan detail dosen by ID
        get("/edit/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val dosen = DosenDAO.getAllDosen().find { it.id_dosen == id }

                if (dosen != null) {
                    call.respondHtml {
                        head { title { +"Detail Dosen" } }
                        body {
                            nav {
                                a("/admin") { +"Dashboard" }
                                a("/admin/user") { +"Mahasiswa" }
                                a("/admin/dosen") { +"Dosen" }
                                a("/admin/admin") { +"Admin" }
                                a("/admin/matkul") { +"Matakuliah" }
                            }
                            h1 { +"Detail Dosen" }
                            table {
                                tr {
                                    th { +"NIDN" }
                                    td { +dosen.nidn }
                                }
                                tr {
                                    th { +"Nama" }
                                    td { +dosen.nama }
                                }
                                tr {
                                    th { +"Alamat" }
                                    td { +dosen.alamat }
                                }
                            }
                            a("/admin/dosen") { +"Kembali ke daftar dosen" }
                        }
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound, "Dosen dengan ID $id tidak ditemukan.")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "ID dosen tidak valid.")
            }
        }
    }
}