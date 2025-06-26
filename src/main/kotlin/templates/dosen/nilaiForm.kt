package com.example.templates.dosen

import com.example.dao.MahasiswaDTO
import com.example.dao.MatkulDTO
import kotlinx.html.*

fun HTML.nilaiForm(mahasiswa: MahasiswaDTO, matkul: MatkulDTO, nilai: Int?) {
  head { title { +"Input Nilai" } }
  body {
    h1 { +"Input Nilai untuk ${mahasiswa.nama} - ${matkul.nama_matkul}" }

    form(
            action = "/dosen/nilai/${mahasiswa.id_mhs}/${matkul.id_matkul}",
            method = FormMethod.post
    ) {
      p {
        label { +"Nilai: " }
        textInput(name = "nilai") { value = nilai.toString() }
      }
      p { submitInput { value = "Simpan" } }
    }

    p { a(href = "/dosen") { +"Kembali ke Dashboard" } }
  }
}
