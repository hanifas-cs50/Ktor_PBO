package com.example.templates.auth

import kotlinx.html.*

fun HTML.register() {
  head { title { +"Register" } }

  body {
    h1 { +"Register" }
    form(action = "/register", method = FormMethod.post) {
      p {
        +"Nama:"
        textInput {
          name = "nama"
          required = true
        }
      }
      p {
        +"Alamat:"
        textArea {
          name = "alamat"
          rows = "5"
          required = true
        }
      }
      p {
        +"Password:"
        passwordInput {
          name = "password"
          required = true
        }
      }
      p { submitInput { value = "Register" } }
    }
  }
}
