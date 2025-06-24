package utils

import java.time.LocalDate
import org.jetbrains.exposed.sql.*

object NidnGenerator {
  fun generateNidn(): String {
    val year = LocalDate.now().year % 100 // e.g., 2025 → 25
    val code = "05"

    val lastId = Dosen.selectAll().map { it[Dosen.id_dosen] }.maxOrNull() ?: 0

    val studentNumber = lastId.toString().padStart(3, '0')

    return "$year$code$studentNumber" // e.g., "2505001"
  }
}
