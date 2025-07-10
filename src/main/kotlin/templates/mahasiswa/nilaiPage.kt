package com.example.templates.mahasiswa

import com.example.dao.MatkulDTO
import kotlinx.html.*
import com.example.templates.components.mahasiswaNavbar

fun HTML.nilaiPage(krsList: List<Pair<MatkulDTO, Int>>) {
    head {
        title { +"Nilai Mahasiswa" }
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
    }
    body {
        mahasiswaNavbar()

        div("dashboard-container") {
            h1("heading") { +"Nilai Saya" }

            if (krsList.isEmpty()) {
                p("nilai-empty-message") {
                    +"Belum ada nilai yang tersedia."
                }
            } else {
                table(classes = "nilai-table") {
                    thead {
                        tr {
                            th { +"Kode" }
                            th { +"Mata Kuliah" }
                            th { +"SKS" }
                            th { +"Nilai" }
                        }
                    }
                    tbody {
                        krsList.forEach { (matkul, nilai) ->
                            tr {
                                td { +matkul.kode_matkul }
                                td { +matkul.nama_matkul }
                                td { +"${matkul.sks}" }
                                td { +"$nilai" }
                            }
                        }
                    }
                }
            }
        }
    }
}
