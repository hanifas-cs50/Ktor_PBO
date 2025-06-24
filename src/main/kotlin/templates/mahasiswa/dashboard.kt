package templates.mahasiswa

import kotlinx.html.*
import UserSession
import MatkulDTO

fun HTML.dashboard(session: UserSession, krs: List<Pair<MatkulDTO, Int?>>) {
    head { title { +"Dashboard Mahasiswa" } }
    body {
        h1 { +"Selamat Datang, ${session.name}" }

        h2 { +"Kartu Rencana Studi (KRS)" }

        table {
            tr {
                th { +"Kode" }
                th { +"Mata Kuliah" }
                th { +"SKS" }
                th { +"Nilai" }
            }
            krs.forEach { (matkul, nilai) ->
                tr {
                    td { +matkul.kode_matkul }
                    td { +matkul.nama_matkul }
                    td { +"${matkul.sks}" }
                    td { +(nilai?.toString() ?: "Belum dinilai") }
                }
            }
        }
    }
}
