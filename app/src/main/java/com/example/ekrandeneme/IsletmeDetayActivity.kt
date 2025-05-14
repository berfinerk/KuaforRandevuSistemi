package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityIsletmeDetayBinding

class IsletmeDetayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIsletmeDetayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIsletmeDetayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isletmeId = intent.getStringExtra("isletmeId")
        if (isletmeId.isNullOrBlank()) {
            android.widget.Toast.makeText(this, "İşletme bulunamadı!", android.widget.Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salon = dbHelper.getSalonById(isletmeId)
        if (salon.isEmpty()) {
            android.widget.Toast.makeText(this, "İşletme bilgisi bulunamadı!", android.widget.Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val hizmetler = dbHelper.getServicesForSalon(isletmeId)
        val profilYazisi = try { dbHelper.getSalonProfileText(isletmeId) } catch (e: Exception) { "" }
        val calisanlar = try { dbHelper.getEmployeesForSalon(isletmeId) } catch (e: Exception) { emptyList() }
        dbHelper.close()

        binding.textViewIsletmeAdi.text = salon["name"] ?: "İşletme"
        binding.textViewAdres.text = salon["address"] ?: ""
        binding.textViewAciklama.text = "Profesyonel hizmetlerimizle sizlerleyiz."
        binding.ratingBar.rating = DatabaseHelper(this).apply { openDatabase() }.getSalonAverageRating(isletmeId)
        binding.textViewProfilYazisi.text = if (profilYazisi.isNotBlank()) profilYazisi else "Profil yazısı eklenmemiş."

        if (hizmetler.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = "Bu işletmeye ait hizmet bulunamadı."
            binding.tableLayoutHizmetler.addView(emptyText)
        } else {
            hizmetler.forEach { (hizmet, ucret) ->
                val row = TableRow(this)
                val hizmetTextView = TextView(this)
                hizmetTextView.text = hizmet
                hizmetTextView.setPadding(16, 8, 16, 8)
                val ucretTextView = TextView(this)
                ucretTextView.text = ucret
                ucretTextView.setPadding(16, 8, 16, 8)
                val btnRandevuAl = Button(this)
                btnRandevuAl.text = "Randevu Al"
                btnRandevuAl.setOnClickListener {
                    val intent = Intent(this, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeId", isletmeId)
                    intent.putExtra("isletmeAdi", salon["name"])
                    intent.putExtra("hizmetAdi", hizmet)
                    intent.putExtra("hizmetUcreti", ucret)
                    startActivity(intent)
                }
                row.addView(hizmetTextView)
                row.addView(ucretTextView)
                row.addView(btnRandevuAl)
                binding.tableLayoutHizmetler.addView(row)
            }
        }

        binding.btnCalisanlariGor.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Çalışanlar")
            val calisanListesi = if (calisanlar.isEmpty()) {
                "Çalışan yok."
            } else {
                val dbHelper = DatabaseHelper(this)
                dbHelper.openDatabase()
                val list = calisanlar.joinToString("\n") { calisan ->
                    val ad = calisan["name"] ?: "?"
                    val rol = calisan["role"] ?: ""
                    val empId = calisan["id"] ?: ""
                    val avg = dbHelper.getEmployeeAverageRating(empId)
                    "$ad${if (rol.isNotBlank()) " - $rol" else ""}  |  Ortalama: ${"%.1f".format(avg)} ★"
                }
                dbHelper.close()
                list
            }
            builder.setMessage(calisanListesi)
            builder.setPositiveButton("Kapat", null)
            builder.show()
        }
    }
} 