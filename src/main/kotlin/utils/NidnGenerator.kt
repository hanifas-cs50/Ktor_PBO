package com.example.utils

import java.time.LocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NidnGenerator {
  suspend fun generateNidn(): String = newSuspendedTransaction {
    val year = LocalDate.now().year % 100 // e.g., 2025 → 25
    val code = "05"

    val lastId = Dosen.selectAll().map { it[Dosen.id_dosen] }.maxOrNull() ?: 0
    val dosenNumber = lastId.toString().padStart(3, '0')

    "$year$code$dosenNumber"
  }
}
