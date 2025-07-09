package com.example.templates.admin.dosen

import com.example.dao.DosenPublicDTO
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.index(list: List<DosenPublicDTO>) {
  head { title { +"Data Dosen" } 
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
  }
  body {
    adminNavbar()

    h1 { +"Data Dosen" }
    a("/admin/dosen/add") { +"+ Tambah Dosen" }
    table(classes = "table-dosen") {
      tr {
        th { +"ID" }
        th { +"NIDN" }
        th { +"Nama" }
        th { +"Aksi" }
      }
      list.forEach {
        tr {
          td { +"${it.id_dosen}" }
          td { +it.nidn }
          td { +it.nama }
          td {
            a("/admin/dosen/edit/${it.id_dosen}") { +"Edit" }
            +" | "
            a("/admin/dosen/delete/${it.id_dosen}") { +"Hapus" }
          }
        }
      }
    }
  }
}
