package com.example.templates.dosen

import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.addBimbinganPage() {
  head { title { +"Tambah Bimbingan" } }
  body {
    dosenNavbar()
    h1 { +"Tambah Mahasiswa Bimbingan" }

    form(action = "/dosen/bimbingan/add", method = FormMethod.post) {
      p {
        +"NIM Mahasiswa:"
        textInput { name = "nim" }
      }
      p { submitInput { value = "Tambahkan" } }
    }
  }
}
