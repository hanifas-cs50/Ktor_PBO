import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object Database {
  fun init() {
    Database.connect("jdbc:sqlite:data.db", driver = "org.sqlite.JDBC")
    transaction {
      SchemaUtils.create(Admin, Mahasiswa, Dosen, Matakuliah, Krs)
    }
  }
}

object Admin : Table() {
  val id_admin = integer("id_admin").autoIncrement()
  val username = varchar("username", 25).uniqueIndex()
  val nama = varchar("nama", 50)
  val password = varchar("password", 60)
  override val primaryKey = PrimaryKey(id_admin)
}

object Mahasiswa : Table() {
  val id_mhs = integer("id_mhs").autoIncrement()
  val nim = varchar("nim", 25).uniqueIndex()
  val nama = text("nama")
  val alamat = text("alamat")
  val password = varchar("password", 60)
  override val primaryKey = PrimaryKey(id_mhs)
}

object Dosen : Table() {
  val id_dosen = integer("id_dosen").autoIncrement()
  val nidn = varchar("nidn", 25).uniqueIndex()
  val nama = text("nama")
  val alamat = text("alamat")
  val password = varchar("password", 60)
  override val primaryKey = PrimaryKey(id_dosen)
}

object Matakuliah : Table() {
  val id_matkul = integer("id_matkul").autoIncrement()
  val id_dosen = integer("id_dosen").references(Dosen.id_dosen)
  val kode_matkul = varchar("kode_matkul", 50).uniqueIndex()
  val nama_matkul = text("nama_matkul")
  val sks = integer("sks").check { it greater 0 }
  override val primaryKey = PrimaryKey(id_matkul)
}

object Dosen_Bimbing : Table() {
  val id_dosenbimbing = integer("id_dosenbimbing").autoIncrement()
  val id_dosen = integer("id_dosen").references(Dosen.id_dosen)
  val id_mhs = integer("id_mhs").references(Mahasiswa.id_mhs)
  override val primaryKey = PrimaryKey(id_dosenbimbing)
}

object Krs : Table() {
  val id_krs = integer("id_krs").autoIncrement()
  val id_mhs = integer("id_mhs").references(Mahasiswa.id_mhs)
  val id_matkul = integer("id_matkul").references(Matakuliah.id_matkul)
  val status = varchar("status", 20)
              .check { it inList listOf("Lulus", "Tidak Lulus", "Belum Dinilai") }
              .default("Belum Dinilai")
  val nilai = varchar("nilai", 2).nullable()
  override val primaryKey = PrimaryKey(id_krs)
}
