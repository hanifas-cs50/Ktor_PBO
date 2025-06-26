package com.example.templates.admin.dosen

import com.example.dao.DosenDTO
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.form(dosen: DosenDTO? = null) {
  head { title { +if (dosen == null) "Tambah Dosen" else "Edit Dosen" } }
  body {
    h1 { +if (dosen == null) "Tambah Dosen" else "Edit Dosen" }

    form(
      action =
        if (dosen == null) "/admin/dosen/add"
        else "/admin/dosen/edit/${dosen.id_dosen}",
      method = FormMethod.post
    ) {
      if (dosen != null) {
        p {
          label { +"NIDN: " }
          textInput(name = "nidn") {
            value = dosen.nidn
            disabled = true
          }
        }
      }
      p {
        label { +"Nama: " }
        textInput(name = "nama") { value = dosen?.nama ?: "" }
      }
      p {
        label { +"Alamat: " }
        textArea(rows = "3", cols = "30") {
          name = "alamat"
          +dosen?.alamat.orEmpty()
        }
      }
      if (dosen != null) {
        p {
          label { +"Password: " }
          passwordInput(name = "password") {
            placeholder = "Biarkan kosong jika tidak diubah"
          }
        }
      }
      p { submitInput { value = if (dosen == null) "Tambah" else "Update" } }
    }
  }
}
