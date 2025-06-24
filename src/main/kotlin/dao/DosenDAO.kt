import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class DosenDTO(
  val id_dosen: Int,
  val nidn: String,
  val nama: String,
  val alamat: String,
  val password: String
)

data class DosenPublicDTO(
  val id_dosen: Int,
  val nidn: String,
  val nama: String,
  val alamat: String
)

object DosenDAO {

  fun insertDosen(nidn: String, nama: String, alamat: String, password: String): Int {
    return transaction {
      Dosen.insert {
        it[Dosen.nidn] = nidn
        it[Dosen.nama] = nama
        it[Dosen.alamat] = alamat
        it[Dosen.password] = password
      } get Dosen.id_dosen
    }
  }

  fun countDosen(): Long {
    return transaction {
      Dosen.selectAll().count()
    }
  }


  fun getAllDosen(): List<DosenPublicDTO> {
    return transaction {
      Dosen.selectAll().map {
        DosenPublicDTO(
          id_dosen = it[Dosen.id_dosen],
          nidn = it[Dosen.nidn],
          nama = it[Dosen.nama],
          alamat = it[Dosen.alamat]
        )
      }
    }
  }

  fun getDosenById(id: Int): DosenDTO? {
    return transaction {
      Dosen.selectAll()
      .where { Dosen.id_dosen eq id }
      .map {
        DosenDTO(
          id_dosen = it[Dosen.id_dosen],
          nidn = it[Dosen.nidn],
          nama = it[Dosen.nama],
          alamat = it[Dosen.alamat],
          password = it[Dosen.password]
        )
      }
      .singleOrNull()
    }
  }

  fun getDosenByNidn(nidn: String): DosenDTO? {
    return transaction {
      Dosen.selectAll()
      .where { Dosen.nidn eq nidn }
      .map {
        DosenDTO(
          id_dosen = it[Dosen.id_dosen],
          nidn = it[Dosen.nidn],
          nama = it[Dosen.nama],
          alamat = it[Dosen.alamat],
          password = it[Dosen.password]
        )
      }
      .singleOrNull()
    }
  }

  fun deleteDosen(id: Int): Boolean {
    return transaction { Dosen.deleteWhere { Dosen.id_dosen eq id } > 0 }
  }

  fun updateDosen(id: Int, alamat: String, nama: String, password: String? = null): Boolean {
    return transaction {
      Dosen.update({ Dosen.id_dosen eq id }) {
        it[Dosen.nama] = nama
        it[Dosen.alamat] = alamat
        if (!password.isNullOrBlank()) {
          it[Mahasiswa.password] = password
        }
      } > 0
    }
  }
}
