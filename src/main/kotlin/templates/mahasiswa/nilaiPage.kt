package com.example.templates.mahasiswa

import com.example.dao.MatkulDTO
import kotlinx.html.*
import com.example.templates.components.mahasiswaNavbar

fun HTML.nilaiPage(krsList: List<Pair<MatkulDTO, Int>>) {
  head { title { +"Nilai Mahasiswa" } }
  body {
      mahasiswaNavbar()

      h1 { +"Nilai Saya" }

      table {
          tr {
              th { +"Kode" }
              th { +"Mata Kuliah" }
              th { +"SKS" }
              th { +"Nilai" }
          }
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
