package com.example.templates.dosen

import com.example.dao.MahasiswaDTO
import com.example.dao.MatkulDTO
import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.nilaiForm(mahasiswa: MahasiswaDTO, matkul: MatkulDTO, nilai: Int?) {
    head {
        title { +"Input Nilai" }
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
    }

    body {
        dosenNavbar()

        div("form-container") {
            h1("heading") { +"Input Nilai" }

            p {
                +"Mahasiswa: ${mahasiswa.nama}"
                br()
                +"Mata Kuliah: ${matkul.nama_matkul}"
            }

            form(
                action = "/dosen/nilai/${mahasiswa.id_mhs}/${matkul.id_matkul}",
                method = FormMethod.post
            ) {
                div("form-group") {
                    label {
                        htmlFor = "nilai"
                        +"Nilai:"
                    }
                    textInput(name = "nilai") {
                        id = "nilai"
                        placeholder = "Masukkan nilai"
                        value = nilai?.toString() ?: ""
                        required = true
                    }
                }

                div("form-group") {
                    submitInput {
                        value = "Simpan"
                        classes = setOf("submit-btn")
                    }
                }
            }

          div(classes = "kembali-dashboard") {
            a(href = "/dosen", classes = "nav-link") {
              +"← Kembali ke Dashboard"
          }
      }
        }
    }
}
