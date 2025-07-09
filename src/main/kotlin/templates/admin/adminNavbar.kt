package com.example.templates.components

import kotlinx.html.*

fun FlowContent.adminNavbar() {
  nav("navbar") {
        a("/admin", classes = "nav-link") { +"Dashboard" }
        a("/admin/mhs", classes = "nav-link") { +"Mahasiswa" }
        a("/admin/dosen", classes = "nav-link") { +"Dosen" }
        a("/admin/matkul", classes = "nav-link") { +"Matakuliah" }
        a("/admin/account", classes = "nav-link") { +"Account" }
        a("/logout", classes = "nav-link") { +"Logout" }
    }
}
