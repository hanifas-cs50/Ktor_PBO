package com.example.templates.dosen

import com.example.dao.MahasiswaPublicDTO
import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.bimbinganPage(mahasiswaList: List<MahasiswaPublicDTO>) {
  head { title { +"Mahasiswa Bimbingan" } }
  body {
    dosenNavbar()
    h1 { +"Mahasiswa Bimbingan" }

    table {
      tr {
        th { +"NIM" }
        th { +"Nama" }
        th { +"Alamat" }
        th { +"Aksi" }
      }
      mahasiswaList.forEach { mhs ->
        tr {
          td { +mhs.nim }
          td { +mhs.nama }
          td { +mhs.alamat }
          td {
            form(action = "/dosen/bimbingan/delete/${mhs.id_mhs}", method = FormMethod.post) {
              submitInput { value = "Hapus" }
            }
          }
        }
      }
    }

    a("/dosen/bimbingan/add") { +"Tambah Bimbingan" }
  }
}
