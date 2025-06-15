import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class KrsDTO(
  val id_krs: Int,
  val id_mhs: Int,
  val id_matkul: Int,
  val status: String,
  val nilai: String?
)

object KrsDAO {

  fun insertKrs(id_mhs: Int, id_matkul: Int, status: String, nilai: String): Int {
    return transaction {
      Krs.insert {
        it[Krs.id_mhs] = id_mhs
        it[Krs.id_matkul] = id_matkul
        it[Krs.status] = status
        it[Krs.nilai] = nilai
      } get Krs.id_krs
    }
  }

  fun getAllKrs(): List<KrsDTO> {
    return transaction {
      Krs.selectAll().map {
        KrsDTO(
          id_krs = it[Krs.id_krs],
          id_mhs = it[Krs.id_mhs],
          id_matkul = it[Krs.id_matkul],
          status = it[Krs.status],
          nilai = it[Krs.nilai]
        )
      }
    }
  }

  fun getKrsById(id: Int): KrsDTO? {
    return transaction {
      Krs.selectAll()
        .where { Krs.id_krs eq id }
        .map {
          KrsDTO(
            id_krs = it[Krs.id_krs],
            id_mhs = it[Krs.id_mhs],
            id_matkul = it[Krs.id_matkul],
            status = it[Krs.status],
            nilai = it[Krs.nilai]
          )
        }
        .singleOrNull()
    }
  }
  fun getKrsByIdMatkul(matkul: Int): KrsDTO? {
    return transaction {
      Krs.selectAll()
        .where { Krs.id_matkul eq matkul }
        .map {
          KrsDTO(
            id_krs = it[Krs.id_krs],
            id_mhs = it[Krs.id_mhs],
            id_matkul = it[Krs.id_matkul],
            status = it[Krs.status],
            nilai = it[Krs.nilai]
          )
        }
        .singleOrNull()
    }
  }

  fun deleteKrs(id: Int): Boolean {
    return transaction { Krs.deleteWhere { Krs.id_krs eq id } > 0 }
  }

  fun updateKrs(id: Int, nilai: String, status: String): Boolean {
    return transaction {
      Krs.update({ Krs.id_krs eq id }) {
        it[Krs.nilai] = nilai
        it[Krs.status] = status
      } > 0
    }
  }
}
