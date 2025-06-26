package com.example.templates.dosen

import com.example.dao.MahasiswaPublicDTO
import kotlinx.html.*

fun HTML.bimbinganIndex(mahasiswaList: List<MahasiswaPublicDTO>) {
    head { title { +"Mahasiswa Bimbingan" } }
    body {
        h1 { +"Daftar Mahasiswa yang Dibimbing" }

        table {
            thead {
                tr {
                    th { +"NIM" }
                    th { +"Nama" }
                    th { +"Alamat" }
                    th { +"Aksi" }
                }
            }
            tbody {
                mahasiswaList.forEach { mhs ->
                    tr {
                        td { +mhs.nim }
                        td { +mhs.nama }
                        td { +mhs.alamat }
                        td {
                            form(
                                action = "/dosen/bimbingan/delete/${mhs.id_mhs}",
                                method = FormMethod.post
                            ) {
                                submitInput {
                                    value = "Hapus"
                                    onClick = "return confirm('Yakin ingin menghapus pembimbingan?')"
                                }
                            }
                        }
                    }
                }
            }
        }

        a("/dosen") { +"← Kembali ke Dashboard" }
    }
}