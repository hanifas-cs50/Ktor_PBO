package com.example.templates.auth

import kotlinx.html.*

fun HTML.register() {
  head { 
    title { +"Register Mahasiswa" } 
    link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
    }
  body {
    h1 { classes = setOf("heading") +"Register Mahasiswa" }

    form(action = "/register", method = FormMethod.post, classes = "form-box") {
        div("form-group") {
            label { +"Nama:" }
            textInput(name = "nama") {
                classes = setOf("form-control")
            }
        }
        div("form-group") {
            label { +"Alamat:" }
            textArea {
                name = "alamat"
                classes = setOf("form-control")
                rows = "4"
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
                value = "Register"
                classes = setOf("btn-submit")
            }
        }
    }

    div("link-container") {
        a("/login", classes = "nav-link") { +"Kembali ke Login" }
    }

    footer {
      + "© 2025 Sistem Rencana Studi - Ktor_PBO."
    } 
  }
}

