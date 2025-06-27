package com.example.templates.mahasiswa

import kotlinx.html.*
import com.example.UserSession
import com.example.dao.MatkulDTO
import com.example.templates.components.mahasiswaNavbar

fun HTML.dashboard(session: UserSession) {
  head { title { +"Dashboard Mahasiswa" } }
  body {
      mahasiswaNavbar()

      h1 { +"Dashboard Mahasiswa" }

      p { +"Nama: ${session.name}" }
      p { +"NIM: ${session.username}" }
  }
}

