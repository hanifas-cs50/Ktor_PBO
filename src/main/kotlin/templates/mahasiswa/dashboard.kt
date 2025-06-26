package com.example.templates.mahasiswa

import kotlinx.html.*
import com.example.UserSession
import com.example.dao.MatkulDTO

fun HTML.dashboard(
    session: UserSession,
    krs: List<Pair<MatkulDTO, Int?>>,
    pembimbing: String?
) {
    head { title { +"Dashboard Mahasiswa" } }
    body {
        h1 { +"Sistem Informasi Akademik" }

        table {
            tr {
                // Mahasiswa Information Section
                td {
                    h3 { +"Mahasiswa Information" }
                    p { +"Nama: ${session.name}" }
                    p { +"NIM: ${session.userId}" }
                    p { +"Pembimbing: ${pembimbing ?: "-"}" }

                    br()
                    h4 { +"Mata Kuliah yang Diambil:" }
                    ul {
                        krs.forEach { (matkul, _) ->
                            li {
                                +"${matkul.kode_matkul} ${matkul.nama_matkul}  SKS: ${matkul.sks}"
                            }
                        }
                    }
                }

                // Mata Kuliah Section (you can replace with available matkul later)
                td {
                    h3 { +"Mata Kuliah" }
                    table {
                        tr {
                            th { +"Kode" }
                            th { +"Nama" }
                            th { +"SKS" }
                        }
                        krs.forEach { (matkul, _) ->
                            tr {
                                td { +matkul.kode_matkul }
                                td { +matkul.nama_matkul }
                                td { +"${matkul.sks}" }
                            }
                        }
                    }
                }
            }
        }

        br()
        h3 { +"Registrasi Mata Kuliah" }
        form(action = "/mahasiswa/krs", method = FormMethod.post) {
            p {
                label { +"Kode Mahasiswa: " }
                textInput(name = "nim") {
                    value = session.userId
                    disabled = true
                }
            }
            p {
                label { +"Kode Mata Kuliah: " }
                textInput(name = "kode_matkul")
            }
            p {
                submitInput { value = "Registrasi" }
            }
        }
    }
}
