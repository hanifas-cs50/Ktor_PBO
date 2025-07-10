package com.example.templates.auth

import kotlinx.html.*

fun HTML.login() {
  head { title { +"Login" } 
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css") 
        }
  body {
      h1 { classes = setOf("heading") +"Login" }

      form(action = "/login", method = FormMethod.post, classes = "form-box") {
           div("form-group") {
            label { +"NIM:" }
            textInput(name = "nim") {
                classes = setOf("form-control")
            }
        }
        div("form-group") {
            label { +"Password:" }
            passwordInput(name = "password") {
                classes = setOf("form-control")
            }
        }
        div("form-group") {
            submitInput {
                value = "Login"
                classes = setOf("btn-submit")
            }
        }
    }   

    div("link-container") {
        a("/", classes = "nav-link") { +"Kembali ke Beranda" }
        a("/register", classes = "nav-link") { +"Register Mahasiswa" }
    }

    footer {
      + "© 2025 Sistem Rencana Studi - Ktor_PBO."
    }
  }
}
