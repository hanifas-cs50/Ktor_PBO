package com.example.templates.components

import kotlinx.html.*

fun FlowContent.mahasiswaNavbar() {
  nav {
    style = """
      display: flex;
      gap: 1rem;
      padding: 1rem;
      margin-bottom: 1rem;
    """.trimIndent()

    a("/mahasiswa") { +"Dashboard" }
    a("/mahasiswa/krs") { +"KRS" }
    a("/mahasiswa/nilai") { +"Nilai" }
    a("/logout") { +"Logout" }
  }
}
