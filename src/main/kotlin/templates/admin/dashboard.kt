package com.example.templates.admin

import com.example.UserSession
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.adminDashboard(session: UserSession, mhsCount: Int, dosenCount: Int, matkulCount: Int) {
    head {
        title { +"Admin Dashboard" }
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
    }
    body {
        adminNavbar()

        div("dashboard-container") {
            h1("heading") { +"Selamat Datang, ${session.name}" }

            div("stats-grid") {
                div("stat-box") { +"Total Mahasiswa: $mhsCount" }
                div("stat-box") { +"Total Dosen: $dosenCount" }
                div("stat-box") { +"Total Matakuliah: $matkulCount" }
            }
        }

        footer {
            +"© 2025 Sistem Informasi KRS Mahasiswa"
        }
    }
}