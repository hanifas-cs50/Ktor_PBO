package com.example.dao

import com.example.utils.Dosen
import com.example.utils.Dosen_Bimbing
import com.example.utils.Mahasiswa
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class DosenBimbingDTO(
        val id_admin: Int,
        val username: String,
        val nama: String,
        val password: String
)

object DosenBimbingDAO {

  fun assignPembimbing(idMhs: Int, idDosen: Int): Boolean {
    return try {
      transaction {
        Dosen_Bimbing.insert {
          it[Dosen_Bimbing.id_dosen] = idDosen
          it[Dosen_Bimbing.id_mhs] = idMhs
        }
      }
      true
    } catch (e: Exception) {
      false
    }
  }

  fun getMahasiswaDibimbingByDosen(idDosen: Int): List<MahasiswaPublicDTO> {
    return transaction {
      (Dosen_Bimbing innerJoin Mahasiswa)
        .selectAll()
        .where { Dosen_Bimbing.id_dosen eq idDosen }
        .map {
          MahasiswaPublicDTO(
            id_mhs = it[Mahasiswa.id_mhs],
            nim = it[Mahasiswa.nim],
            nama = it[Mahasiswa.nama],
            alamat = it[Mahasiswa.alamat]
          )
        }
    }
  }

  fun getPembimbingByMahasiswaId(idMhs: Int): String? {
    return transaction {
      (Dosen_Bimbing innerJoin Dosen)
        .selectAll()
        .where { Dosen_Bimbing.id_mhs eq idMhs }
        .limit(1)
        .firstOrNull()
        ?.get(Dosen.nama)
    }
  }

  fun deleteBimbingan(idMhs: Int, idDosen: Int): Boolean {
    return transaction {
      Dosen_Bimbing.deleteWhere {
        (Dosen_Bimbing.id_mhs eq idMhs) and (Dosen_Bimbing.id_dosen eq idDosen)
      } > 0
    }
  }
}
