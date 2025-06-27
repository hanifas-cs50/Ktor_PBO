package com.example.templates.dosen

import com.example.dao.MahasiswaPublicDTO
import com.example.dao.MatkulDTO
import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.dosenNilaiPage(krsList: List<Pair<MahasiswaPublicDTO, MatkulDTO>>) {
  head { title { +"Penilaian Mahasiswa" } }
  body {
    dosenNavbar()
    h1 { +"Penilaian Mahasiswa" }

    table {
      tr {
        th { +"NIM" }
        th { +"Nama" }
        th { +"Mata Kuliah" }
        th { +"Aksi" }
      }
      krsList.forEach { (mhs, matkul) ->
        tr {
          td { +mhs.nim }
          td { +mhs.nama }
          td { +matkul.nama_matkul }
          td { a("/dosen/nilai/${mhs.id_mhs}/${matkul.id_matkul}") { +"Edit Nilai" } }
        }
      }
    }
  }
}
