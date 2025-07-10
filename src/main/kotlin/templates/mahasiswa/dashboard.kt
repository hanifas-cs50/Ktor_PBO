package com.example.templates.mahasiswa

import kotlinx.html.*
import com.example.UserSession
import com.example.templates.components.mahasiswaNavbar

fun HTML.dashboard(session: UserSession) {
  head { 
    title { +"Dashboard Mahasiswa" } 
    link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
  }

  body {
    mahasiswaNavbar()

    div("dashboard-container") {
      h1 { +"Dashboard Mahasiswa" }

      div("dashboard-info") {
        p {
          strong { +"Nama: " }
          +session.name
        }
        p {
          strong { +"NIM: " }
          +session.username
        }
      }

      div("dashboard-menu") {
        h2 { +"Menu Mahasiswa" }
        ul {
          li { a("/mahasiswa/krs") { +"Kelola KRS" } }
          li { a("/mahasiswa/nilai") { +"Lihat Nilai" } }
        }
      }
    }
  }
}
