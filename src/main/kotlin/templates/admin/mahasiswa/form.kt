package templates.admin.mahasiswa

import MahasiswaDTO
import kotlinx.html.*

fun HTML.form(mhs: MahasiswaDTO? = null) {
  head { title { +if (mhs == null) "Tambah Mahasiswa" else "Edit Mahasiswa" } }
  body {
    h1 { +if (mhs == null) "Tambah Mahasiswa" else "Edit Mahasiswa" }

    form(
      action = if (mhs == null) "/admin/mhs/add" else "/admin/mhs/edit/${mhs.id_mhs}",
      method = FormMethod.post
    ) {
      if (mhs != null) {
        p {
          label { +"NIM: " }
          textInput(name = "nim") {
            value = mhs.nim
            disabled = true
          }
        }
      }
      p {
        label { +"Nama: " }
        textInput(name = "nama") { value = mhs?.nama ?: "" }
      }
      p {
        label { +"Alamat: " }
        textArea(rows = "3", cols = "30") {
          name = "alamat"
          +mhs?.alamat.orEmpty()
        }
      }
      p {
        label { +"Password: " }
        passwordInput(name = "password") {
          placeholder = if (mhs == null) "Masukkan password" else "Biarkan kosong jika tidak diubah"
        }
      }
      p { submitInput { value = if (mhs == null) "Tambah" else "Update" } }
    }
  }
}
