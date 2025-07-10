package com.example.templates.dosen

import com.example.UserSession
import com.example.templates.components.dosenNavbar
import kotlinx.html.*

fun HTML.dashboard(session: UserSession) {
    head {
        title { +"Dashboard Dosen" }
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css") // Pastikan file CSS terhubung
    }

    body {
        dosenNavbar()

        div("dashboard-container") {
            h1("heading") { +"Dashboard Dosen" }

            div("dashboard-info") {
                p { +"Nama: ${session.name}" }
                p { +"NIDN: ${session.username}" }
            }

            div("dashboard-menu") {
                h2 { +"Menu" }
                ul {
                    li {
                        a(href = "/dosen/krs") { +"Kelola KRS Mahasiswa" }
                    }
                    li {
                        a(href = "/dosen/nilai") { +"Input Nilai Mahasiswa" }
                    }
                }
            }
        }

        footer {
            +"© 2025 Sistem Informasi Akademik"
        }
    }
}
