package com.example.templates

import kotlinx.html.*

fun HTML.guestIndex(userId: String? = null) {
  head { title { +"Welcome" } 
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
        }
  body {
    h1 { 
      classes = setOf("heading")
      +if (userId == null) "Hello World" else "Welcome, ${userId}"
    }

    div {
      classes = setOf("link-container")
  
      a("/login") { 
        classes = setOf("nav-link")
        +"Login" }
      a("/register") { 
        classes = setOf("nav-link")
        +"Register" }
      a("/register/admin") { 
        classes = setOf("nav-link")
        +"Register Admin" }
    }

    footer {
      + "© 2025 Sistem Rencana Studi - Ktor_PBO."
    }
  }
}
