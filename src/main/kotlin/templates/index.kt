package com.example.templates

import kotlinx.html.*

fun HTML.guestIndex(userId: String? = null) {
  head { title { +"Welcome" } }
  body {
    h1 { 
      style = """
        padding: 1rem;
      """.trimIndent()
      +if (userId == null) "Hello World" else "Welcome, ${userId}"
    }

    div {
      style = """
        display: flex;
        gap: 1rem;
        padding: 1rem;
        margin-bottom: 1rem;
      """.trimIndent()
  
      a("/login") { +"Login" }
      a("/register") { +"Register" }
      a("/register/admin") { +"Register Admin" }
    }
  }
}
