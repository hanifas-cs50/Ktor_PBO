package com.example.templates.admin.matkul

import com.example.dao.DosenPublicDTO
import com.example.dao.MatkulDTO
import com.example.templates.components.adminNavbar
import kotlinx.html.*

fun HTML.form(matkul: MatkulDTO? = null, dosenList: List<DosenPublicDTO>) {
  head { title { +if (matkul == null) "Tambah Matakuliah" else "Edit Matakuliah" } 
        link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
  }
  body {
    adminNavbar()

    h1 { +if (matkul == null) "Tambah Matakuliah" else "Edit Matakuliah" }
    form(
            action =
                    if (matkul == null) "/admin/matkul/add"
                    else "/admin/matkul/edit/${matkul.id_matkul}",
            method = FormMethod.post
    ) {
      p {
        label { +"Kode: " }
        textInput(name = "kode") {
          value = matkul?.kode_matkul ?: ""
          if (matkul != null) disabled = true
        }
      }
      p {
        label { +"Nama: " }
        textInput(name = "nama") { value = matkul?.nama_matkul ?: "" }
      }
      p {
        label { +"SKS: " }
        numberInput(name = "sks") {
          value = matkul?.sks?.toString() ?: ""
          min = "1"
          max = "6"
        }
      }
      p {
        label { +"Dosen Pengampu: " }
        select {
          name = "id_dosen"
          dosenList.forEach {
            option {
              value = it.id_dosen.toString()
              selected = (it.id_dosen == matkul?.id_dosen)
              +it.nama
            }
          }
        }
      }
      p { submitInput { value = if (matkul == null) "Tambah" else "Update" } }
    }
  }
}
