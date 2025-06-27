package com.example.templates.auth

import kotlinx.html.*

fun HTML.login() {
  head { title { +"Login" } }
  body {
      h1 { +"Login" }

      form(action = "/login", method = FormMethod.post) {
          p {
              +"NIM:"
              textInput { name = "nim" }
          }
          p {
              +"Password:"
              passwordInput { name = "password" }
          }
          p {
              submitInput { value = "Login" }
          }
      }

      p {
          a("/") { +"Kembali ke Beranda" }
          +" | "
          a("/register") { +"Register Mahasiswa" }
      }
  }
}
