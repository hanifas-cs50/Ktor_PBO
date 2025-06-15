package com.example.utils

import java.time.LocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NimGenerator {
  suspend fun generateNim(): String = newSuspendedTransaction {
      val year = LocalDate.now().year % 100
      val code = "01"

      val lastId = Mahasiswa.selectAll().map { it[Mahasiswa.id_mhs] }.maxOrNull() ?: 0
      val studentNumber = lastId.toString().padStart(3, '0')

      "$year$code$studentNumber"
  }
}
