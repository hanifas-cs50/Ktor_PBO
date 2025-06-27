package com.example.dao

import com.example.utils.Dosen
import com.example.utils.Krs
import com.example.utils.Mahasiswa
import com.example.utils.Matakuliah
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class KrsDTO(
  val id_krs: Int,
  val id_mhs: Int,
  val id_matkul: Int,
  val status: String,
  val nilai: Int?
)

object KrsDAO {

  fun insertKrs(idMahasiswa: Int, idMatkul: Int): Int {
    return transaction {
      // Check for existing record
      val existing = Krs.selectAll()
        .where { (Krs.id_mhs eq idMahasiswa) and (Krs.id_matkul eq idMatkul) }
        .singleOrNull()

      if (existing != null) {
        val status = existing[Krs.status]
        if (status == "Belum Dinilai" || status == "Lulus") {
          // Already enrolled or passed
          return@transaction -1 // Signal rejection
        }
      }

      // Otherwise allow insert
      Krs.insert {
        it[Krs.id_mhs] = idMahasiswa
        it[Krs.id_matkul] = idMatkul
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

  fun getKrsForMahasiswa(idMahasiswa: Int): List<Pair<MatkulDTO, Int?>> {
    return transaction {
      (Krs innerJoin Matakuliah).selectAll().where { Krs.id_mhs eq idMahasiswa }.map {
        val matkul = MatkulDTO(
          id_matkul = it[Matakuliah.id_matkul],
          id_dosen = it[Matakuliah.id_dosen],
          kode_matkul = it[Matakuliah.kode_matkul],
          nama_matkul = it[Matakuliah.nama_matkul],
          sks = it[Matakuliah.sks]
        )
        val nilai = it[Krs.nilai]
        matkul to nilai
      }
    }
  }

  fun getMahasiswaInMatkul(dosenId: Int): List<Pair<MahasiswaPublicDTO, MatkulDTO>> {
    return transaction {
      (Krs innerJoin Matakuliah innerJoin Mahasiswa)
      .selectAll()
      .where { Matakuliah.id_dosen eq dosenId }
      .map {
        val mhs = MahasiswaPublicDTO(
          id_mhs = it[Mahasiswa.id_mhs],
          nim = it[Mahasiswa.nim],
          nama = it[Mahasiswa.nama],
          alamat = it[Mahasiswa.alamat]
        )
        val matkul = MatkulDTO(
          id_matkul = it[Matakuliah.id_matkul],
          id_dosen = it[Matakuliah.id_dosen],
          kode_matkul = it[Matakuliah.kode_matkul],
          nama_matkul = it[Matakuliah.nama_matkul],
          sks = it[Matakuliah.sks]
        )
        mhs to matkul
      }
    }
  }

  fun getMatkulDetailForMahasiswa( mahasiswaId: Int, matkulId: Int): Triple<MatkulDTO, DosenPublicDTO, Int?>? {
    return transaction {
      (Krs innerJoin Matakuliah innerJoin Dosen)
      .selectAll()
      .where { (Krs.id_mhs eq mahasiswaId) and (Krs.id_matkul eq matkulId) }
      .map {
        val matkul = MatkulDTO(
          id_matkul = it[Matakuliah.id_matkul],
          id_dosen = it[Matakuliah.id_dosen],
          kode_matkul = it[Matakuliah.kode_matkul],
          nama_matkul = it[Matakuliah.nama_matkul],
          sks = it[Matakuliah.sks]
        )
        val dosen = DosenPublicDTO(
          id_dosen = it[Dosen.id_dosen],
          nama = it[Dosen.nama],
          nidn = it[Dosen.nidn],
          alamat = it[Dosen.alamat]
        )
        val nilai = it[Krs.nilai]
        Triple(matkul, dosen, nilai)
      }.singleOrNull()
    }
  }

  fun getNilai(mahasiswaId: Int, matkul: Int): Int? {
    return transaction {
      Krs.selectAll()
        .where { (Krs.id_matkul eq matkul) and (Krs.id_mhs eq mahasiswaId) }
        .map { it[Krs.nilai] }.singleOrNull()
    }
  }

  fun deleteKrs(id: Int): Boolean {
    return transaction { Krs.deleteWhere { Krs.id_krs eq id } > 0 }
  }

  fun updateKrs(idMahasiswa: Int, nilai: Int, idMatkul: Int): Boolean {
    return transaction {
      Krs.update({ (Krs.id_mhs eq idMahasiswa) and (Krs.id_matkul eq idMatkul) }) {
        it[Krs.nilai] = nilai
        it[Krs.status] = if (nilai >= 60) "Lulus" else "Tidak Lulus"
      } > 0
    }
  }
}
