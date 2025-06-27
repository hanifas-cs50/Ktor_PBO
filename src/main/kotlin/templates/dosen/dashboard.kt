package com.example.templates.dosen

import com.example.UserSession
import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.dashboard(session: UserSession) {
  head { title { +"Dashboard Dosen" } }
  body {
    dosenNavbar()
    h1 { +"Dashboard Dosen" }
    p { +"Nama: ${session.name}" }
    p { +"NIDN: ${session.username}" }
  }
}
