package templates.components

import kotlinx.html.*

fun FlowContent.adminNavbar() {
  nav {
    style = """
      display: flex;
      gap: 1rem;
      padding: 1rem;
      margin-bottom: 1rem;
    """.trimIndent()

    a("/admin") { +"Dashboard" }
    a("/admin/mhs") { +"Mahasiswa" }
    a("/admin/dosen") { +"Dosen" }
    a("/admin/matkul") { +"Matakuliah" }
    a("/admin/account") { +"Account" }
  }
}
