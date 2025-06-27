package com.example.templates.components

import kotlinx.html.*

fun FlowContent.dosenNavbar() {
  nav {
    style = """
      display: flex;
      gap: 1rem;
      padding: 1rem;
      margin-bottom: 1rem;
    """.trimIndent()

    a("/dosen") { +"Dashboard" }
    a("/dosen/nilai") { +"Nilai" }
    a("/dosen/bimbingan") { +"Bimbingan" }
    a("/logout") { +"Logout" }
  }
}
