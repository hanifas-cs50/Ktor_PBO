package com.example.templates.auth

import kotlinx.html.*

fun HTML.register() {
  head { title { +"Register Mahasiswa" } }
  body {
      h1 { +"Register Mahasiswa" }

      form(action = "/register", method = FormMethod.post) {
          p {
              +"Nama:"
              textInput { name = "nama" }
          }
          p {
              +"Alamat:"
              textArea { name = "alamat" }
          }
          p {
              +"Password:"
              passwordInput { name = "password" }
          }
          p {
              submitInput { value = "Register" }
          }
      }

      p {
          a("/login") { +"Kembali ke Login" }
      }
  }
}

