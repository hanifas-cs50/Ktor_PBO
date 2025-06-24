package templates.admin

import AdminDTO
import kotlinx.html.*

fun HTML.adminForm(admin: AdminDTO) {
  head { title { +"Edit Admin" } }
  body {
    h1 { +"Edit Admin" }
    form(action = "/admin/account/${admin.id_admin}", method = FormMethod.post) {
      p {
        label { +"Nama: " }
        textInput(name = "nama") { value = admin.nama.toString() }
      }
      p {
        label { +"Password: " }
        passwordInput(name = "password") {}
      }
      p { submitInput { +"Update" } }
    }

    p { a("/admin/delete/${admin.id_admin}") { +"Delete Account" } }
  }
}
