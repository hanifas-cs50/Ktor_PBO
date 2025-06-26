package com.example.templates.admin.matkul

import com.example.dao.MatkulWithDosenDTO
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.index(list: List<MatkulWithDosenDTO>) {
  head { title { +"Data Matakuliah" } }
  body {
    adminNavbar()
    h1 { +"Data Matakuliah" }
    a("/admin/matkul/add") { +"+ Tambah Matakuliah" }
    table {
      tr {
        th { +"ID" }
        th { +"Kode" }
        th { +"Nama" }
        th { +"SKS" }
        th { +"Dosen Pengampu" }
        th { +"Aksi" }
      }
      list.forEach {
        tr {
          td { +"${it.id_matkul}" }
          td { +it.kode_matkul }
          td { +it.nama_matkul }
          td { +"${it.sks}" }
          td { +it.nama_dosen }
          td {
            a("/admin/matkul/edit/${it.id_matkul}") { +"Edit" }
            +" | "
            a("/admin/matkul/delete/${it.id_matkul}") { +"Hapus" }
          }
        }
      }
    }
  }
}
