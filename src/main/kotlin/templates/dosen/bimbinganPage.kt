package com.example.templates.dosen

import com.example.templates.components.dosenNavbar
import com.example.dao.MahasiswaPublicDTO
import kotlinx.html.*

fun HTML.bimbinganPage(mahasiswaList: List<MahasiswaPublicDTO>) {
    head {
        title { +"Tambah Bimbingan" }
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
    }

    body {
        dosenNavbar()

        div("form-container") {
            h1("heading") { +"Tambah Mahasiswa Bimbingan" }

            form(action = "/dosen/bimbingan/add", method = FormMethod.post) {
                div("form-group") {
                    label {
                        htmlFor = "nim"
                        +"NIM Mahasiswa:"
                    }
                    textInput(name = "nim") {
                        id = "nim"
                        placeholder = "Masukkan NIM"
                    }
                }

                div("form-group") {
                    submitInput(classes = "submit-button") {
                        value = "Tambahkan"
                    }
                }
            }

            p {
                a(href = "/dosen/bimbingan", classes = "action-link") {
                    +"← Kembali ke Daftar Bimbingan"
                }
            }
        }
    }
}
