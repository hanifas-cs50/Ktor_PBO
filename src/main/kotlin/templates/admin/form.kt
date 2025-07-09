package com.example.templates.admin

import com.example.dao.AdminDTO
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.adminForm(admin: AdminDTO) {
  head { title { +"Edit Admin" } 
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
  }
  body {
    adminNavbar()

    h1 { +"Edit Admin" }

    form(action = "/admin/account/${admin.id_admin}", method = FormMethod.post) {
      table {
        tr {
          td { label { +"Nama:" } }
          td {
            textInput(name = "nama") {
              value = admin.nama.toString()
              required = true
            }
          }
        }
        tr {
          td { label { +"Password:" } }
          td {
            passwordInput(name = "password") {
              placeholder = "Kosongkan jika Sama"
            }
          }
        }
        tr {
          td { colSpan = "2" }
          td {
            submitInput {
              value = "Update"
            }
          }
        }
      }
    }

    p {
      a("/admin/delete/${admin.id_admin}") {
        +"Delete Account"
      }
    }
  }
}