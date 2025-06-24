import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class MahasiswaDTO(
  val id_mhs: Int,
  val nim: String,
  val nama: String,
  val alamat: String,
  val password: String
)
data class MahasiswaPublicDTO(
  val id_mhs: Int,
  val nim: String,
  val nama: String,
  val alamat: String
)

object MahasiswaDAO {

  fun insertMahasiswa(nim: String, nama: String, alamat: String, password: String): Int {
    return transaction {
      Mahasiswa.insert {
        it[Mahasiswa.nim] = nim
        it[Mahasiswa.nama] = nama
        it[Mahasiswa.alamat] = alamat
        it[Mahasiswa.password] = password
      } get Mahasiswa.id_mhs
    }
  }

  fun countMahasiswa(): Long {
    return transaction {
      Mahasiswa.selectAll().count()
    }
  }


  fun getAllMahasiswa(): List<MahasiswaPublicDTO> {
    return transaction {
      Mahasiswa.selectAll().map {
        MahasiswaPublicDTO(
          id_mhs = it[Mahasiswa.id_mhs],
          nim = it[Mahasiswa.nim],
          nama = it[Mahasiswa.nama],
          alamat = it[Mahasiswa.alamat]
        )
      }
    }
  }

  fun getMahasiswaById(id: Int): MahasiswaDTO? {
    return transaction {
      Mahasiswa.selectAll()
      .where { Mahasiswa.id_mhs eq id }
      .map {
        MahasiswaDTO(
          id_mhs = it[Mahasiswa.id_mhs],
          nim = it[Mahasiswa.nim],
          nama = it[Mahasiswa.nama],
          alamat = it[Mahasiswa.alamat],
          password = it[Mahasiswa.password]
        )
      }
      .singleOrNull()
    }
  }

  fun getMahasiswaByNim(nim: String): MahasiswaDTO? {
    return transaction {
      Mahasiswa.selectAll()
        .where { Mahasiswa.nim eq nim }
        .map {
          MahasiswaDTO(
            id_mhs = it[Mahasiswa.id_mhs],
            nim = it[Mahasiswa.nim],
            nama = it[Mahasiswa.nama],
            alamat = it[Mahasiswa.alamat],
            password = it[Mahasiswa.password]
          )
        }
        .singleOrNull()
    }
  }

  fun deleteMahasiswa(id: Int): Boolean {
    return transaction { Mahasiswa.deleteWhere { Mahasiswa.id_mhs eq id } > 0 }
  }

  fun updateMahasiswa(id: Int, nama: String, alamat: String, password: String? = null): Boolean {
    return transaction {
      Mahasiswa.update({ Mahasiswa.id_mhs eq id }) {
        it[Mahasiswa.nama] = nama
        it[Mahasiswa.alamat] = alamat
        if (!password.isNullOrBlank()) {
          it[Mahasiswa.password] = password
        }
      } > 0
    }
  }
}
