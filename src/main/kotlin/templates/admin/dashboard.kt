package templates.admin

import UserSession
import kotlinx.html.*
import templates.components.adminNavbar

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
