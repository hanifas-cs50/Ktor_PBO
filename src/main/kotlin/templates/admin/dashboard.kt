package com.example.templates.admin

import com.example.UserSession
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.adminDashboard(session: UserSession, mhsCount: Int, dosenCount: Int, matkulCount: Int) {
  head { title { +"Admin Dashboard" } }
  body {
    adminNavbar()

    h1 { +"Selamat Datang, ${session.name}" }

    ul {
      li { +"Total Mahasiswa: $mhsCount" }
      li { +"Total Dosen: $dosenCount" }
      li { +"Total Matakuliah: $matkulCount" }
    }
  }
}
