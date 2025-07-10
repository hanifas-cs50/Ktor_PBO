package com.example.templates.components

import kotlinx.html.*

fun FlowContent.mahasiswaNavbar() {
  nav("navbar") {
    a("/mahasiswa", classes = "nav-link") { +"Dashboard" }
    a("/mahasiswa/krs", classes = "nav-link") { +"KRS" }
    a("/mahasiswa/nilai", classes = "nav-link") { +"Nilai" }
    a("/logout", classes = "nav-link") { +"Logout" }
  }
}
