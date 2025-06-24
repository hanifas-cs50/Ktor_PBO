package templates.admin.mahasiswa

import MahasiswaPublicDTO
import kotlinx.html.*

fun HTML.index(list: List<MahasiswaPublicDTO>) {
  head { title { +"Data Mahasiswa" } }
  body {
    h1 { +"Data Mahasiswa" }
    a("/admin/mhs/add") { +"+ Tambah Mahasiswa" }
    table {
      tr {
        th { +"ID" }
        th { +"NIM" }
        th { +"Nama" }
        th { +"Alamat" }
        th { +"Aksi" }
      }
      list.forEach {
        tr {
          td { +"${it.id_mhs}" }
          td { +it.nim }
          td { +it.nama }
          td { +it.alamat }
          td {
            a("/admin/mhs/edit/${it.id_mhs}") { +"Edit" }
            +" | "
            a("/admin/mhs/delete/${it.id_mhs}") { +"Hapus" }
          }
        }
      }
    }
  }
}
