package templates.mahasiswa

import UserSession
import MatkulDTO
impoer DosenPublicDTO
import kotlinx.html.*

fun HTML.matkulDetail(session: UserSession, matkul: MatkulDTO, dosen: DosenPublicDTO, nilai: Int?) {
    head { title { +"Detail Matkul - ${matkul.nama_matkul}" } }
    body {
        h1 { +"Detail Mata Kuliah" }

        p { +"Mahasiswa: ${session.name}" }

        table {
            tr {
                th { +"Kode" }
                td { +matkul.kode_matkul }
            }
            tr {
                th { +"Nama Mata Kuliah" }
                td { +matkul.nama_matkul }
            }
            tr {
                th { +"SKS" }
                td { +"${matkul.sks}" }
            }
            tr {
                th { +"Dosen Pengampu" }
                td { +dosen.nama }
            }
            tr {
                th { +"Nilai" }
                td { +(nilai ?: "Belum dinilai") }
            }
        }

        p {
            a(href = "/mahasiswa") { +"⬅ Kembali ke Dashboard" }
        }
    }
}
