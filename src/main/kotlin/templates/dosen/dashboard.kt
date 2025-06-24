package templates.dosen

import MahasiswaPublicDTO
import MatkulDTO
import UserSession
import kotlinx.html.*

fun HTML.dashboard(dosen: UserSession, mahasiswaMatkul: List<Pair<MahasiswaPublicDTO, MatkulDTO>>) {
  head { title { +"Dosen Dashboard" } }
  body {
    h1 { +"Selamat datang, ${dosen.name}" }
    h2 { +"Mahasiswa Bimbingan Anda" }

    table {
      tr {
        th { +"Nama Mahasiswa" }
        th { +"Mata Kuliah" }
        th { +"Aksi" }
      }
      mahasiswaMatkul.forEach { (mhs, matkul) ->
        tr {
          td { +mhs.nama }
          td { +matkul.nama_matkul }
          td { a(href = "/dosen/nilai/${mhs.id_mhs}/${matkul.id_matkul}") { +"Input Nilai" } }
        }
      }
    }
  }
}
