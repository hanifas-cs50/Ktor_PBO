package com.example.templates.mahasiswa

import com.example.dao.KrsDTO
import com.example.dao.MatkulDTO
import kotlinx.html.*
import com.example.templates.components.mahasiswaNavbar

fun HTML.krsPage(krsList: List<Pair<MatkulDTO, Int?>>) {
    head { title { +"KRS Mahasiswa" } }
    body {
        mahasiswaNavbar()

        h1 { +"Kartu Rencana Studi (KRS)" }

        h2 { +"Tambah KRS" }
        form(action = "/mahasiswa/krs", method = FormMethod.post) {
            p {
                +"Kode Mata Kuliah:"
                textInput { name = "kode_matkul" }
            }
            p {
                submitInput { value = "Tambahkan" }
            }
        }

        h2 { +"Daftar KRS Belum Dinilai" }
        table {
            tr {
                th { +"Kode" }
                th { +"Mata Kuliah" }
                th { +"SKS" }
            }
            krsList.forEach { (matkul, _) ->
                tr {
                    td { +matkul.kode_matkul }
                    td { +matkul.nama_matkul }
                    td { +"${matkul.sks}" }
                }
            }
        }
    }
}
