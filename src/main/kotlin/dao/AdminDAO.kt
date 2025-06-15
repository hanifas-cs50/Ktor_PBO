import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class AdminDTO(
  val id_admin: Int,
  val username: String,
  val nama: String,
  val password: String
)
data class AdminPublicDTO(
  val id_admin: Int,
  val username: String,
  val nama: String
)

object AdminDAO {

  fun insertAdmin(username: String, nama: String, password: String): Int {
    val hashedPassword = PasswordUtils.hash(password)

    return transaction {
      Admin.insert {
        it[Admin.username] = username
        it[Admin.nama] = nama
        it[Admin.password] = hashedPassword
      } get Admin.id_admin
    }
  }

  fun getAllAdmin(): List<AdminPublicDTO> {
    return transaction {
      Admin.selectAll().map {
        AdminPublicDTO(
          id_admin = it[Admin.id_admin],
          username = it[Admin.username],
          nama = it[Admin.nama]
        )
      }
    }
  }

  fun getAdminByUsername(username: String): AdminDTO? {
    return transaction {
      Admin.selectAll()
      .where { Admin.username eq username }
      .map {
        AdminDTO(
          id_admin = it[Admin.id_admin],
          username = it[Admin.username],
          nama = it[Admin.nama],
          password = it[Admin.password]
        )
      }
      .singleOrNull()
    }
  }

  fun deleteAdmin(id: Int): Boolean {
    return transaction { Admin.deleteWhere { Admin.id_admin eq id } > 0 }
  }

  fun updateAdmin(id: Int, nama: String): Boolean {
    return transaction {
      Admin.update({ Admin.id_admin eq id }) {
        it[Admin.nama] = nama
      } > 0
    }
  }
}
