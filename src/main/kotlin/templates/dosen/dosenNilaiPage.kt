package com.example.templates.dosen

import com.example.dao.MahasiswaPublicDTO
import com.example.dao.MatkulDTO
import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.dosenNilaiPage(krsList: List<Pair<MahasiswaPublicDTO, MatkulDTO>>) {
    head {
        title { +"Penilaian Mahasiswa" }
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
    }

    body {
        dosenNavbar()

        div("dashboard") {
            h1("heading") { +"Penilaian Mahasiswa" }

            if (krsList.isEmpty()) {
                p { +"Belum ada data KRS untuk dinilai." }
            } else {
                table {
                    thead {
                        tr {
                            th { +"NIM" }
                            th { +"Nama" }
                            th { +"Mata Kuliah" }
                            th { +"Aksi" }
                        }
                    }
                    tbody {
                        krsList.forEach { (mhs, matkul) ->
                            tr {
                                td { +mhs.nim }
                                td { +mhs.nama }
                                td { +matkul.nama_matkul }
                                td {
                                    a(
                                        href = "/dosen/nilai/${mhs.id_mhs}/${matkul.id_matkul}",
                                        classes = "action-link"
                                    ) { +"Edit Nilai" }
                                }
                            }
                        }
                    }
                }
            }

            p {
                a(href = "/dosen", classes = "nav-link") {
                    +"← Kembali ke Dashboard"
                }
            }
        }
    }
}
