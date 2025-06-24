import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class MatkulDTO(
  val id_matkul: Int,
  val id_dosen: Int,
  val kode_matkul: String,
  val nama_matkul: String,
  val sks: Int
)

object MatkulDAO {

  fun insertMatkul(id_dosen: Int, kode_matkul: String, nama_matkul: String, sks: Int): Int {
    return transaction {
      Matakuliah.insert {
        it[Matakuliah.id_dosen] = id_dosen
        it[Matakuliah.kode_matkul] = kode_matkul
        it[Matakuliah.nama_matkul] = nama_matkul
        it[Matakuliah.sks] = sks
      } get Matakuliah.id_matkul
    }
  }

  fun countMatkul(): Long {
    return transaction {
      Matakuliah.selectAll().count()
    }
  }


  fun getAllMatkul(): List<MatkulDTO> {
    return transaction {
      Matakuliah.selectAll().map {
        MatkulDTO(
          id_matkul = it[Matakuliah.id_matkul],
          id_dosen = it[Matakuliah.id_dosen],
          kode_matkul = it[Matakuliah.kode_matkul],
          nama_matkul = it[Matakuliah.nama_matkul],
          sks = it[Matakuliah.sks]
        )
      }
    }
  }

  fun getMatkulById(id: Int): MatkulDTO? {
    return transaction {
      Matakuliah.selectAll()
        .where { Matakuliah.id_matkul eq id }
        .map {
          MatkulDTO(
            id_matkul = it[Matakuliah.id_matkul],
            id_dosen = it[Matakuliah.id_dosen],
            kode_matkul = it[Matakuliah.kode_matkul],
            nama_matkul = it[Matakuliah.nama_matkul],
            sks = it[Matakuliah.sks]
          )
        }
        .singleOrNull()
    }
  }

  fun getMatkulIdByDosenId(dosenId: Int): Int? {
    return transaction {
      Matakuliah.selectAll()
        .where { Matakuliah.id_dosen eq dosenId }
        .map { it[Matakuliah.id_matkul] }
        .singleOrNull()
    }
  }

  fun isMatkulOwnedByDosen(matkulId: Int, dosenId: Int): Boolean {
    return transaction {
        Matakuliah.selectAll()
          .where { (Matakuliah.id_matkul eq matkulId) and (Matakuliah.id_dosen eq dosenId) }
          .empty().not()  // returns true if a matching row exists
    }
  }

  fun deleteMatkul(id: Int): Boolean {
    return transaction { Matakuliah.deleteWhere { Matakuliah.id_matkul eq id } > 0 }
  }

  fun updateMatkul(id: Int, nama: String): Boolean {
    return transaction {
      Matakuliah.update({ Matakuliah.id_matkul eq id }) { it[Matakuliah.nama_matkul] = nama } > 0
    }
  }
}
