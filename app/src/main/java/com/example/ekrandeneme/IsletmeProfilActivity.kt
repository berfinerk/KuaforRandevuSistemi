package com.example.ekrandeneme

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper

class IsletmeProfilActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var salonId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isletme_profil)

        val isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salon = dbHelper.getAllSalons().find { it["name"] == isletmeAdi }
        salonId = salon?.get("id") ?: ""
        if (salonId.isBlank()) {
            Toast.makeText(this, "İşletme bulunamadı!", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val profilYazisi = dbHelper.getSalonProfileText(salonId)
        dbHelper.close()

        val editTextProfilYazisi = findViewById<EditText>(R.id.editTextProfilYazisi)
        val btnProfilKaydet = findViewById<Button>(R.id.btnProfilKaydet)
        val editTextCalisanAdi = findViewById<EditText>(R.id.editTextCalisanAdi)
        val editTextCalisanRol = findViewById<EditText>(R.id.editTextCalisanRol)
        val btnCalisanEkle = findViewById<Button>(R.id.btnCalisanEkle)
        val calisanlarLayout = findViewById<LinearLayout>(R.id.calisanlarLayout)

        editTextProfilYazisi.setText(profilYazisi)

        fun calisanlariGoster() {
            try {
                dbHelper.openDatabase()
                val calisanlar = dbHelper.getEmployeesForSalon(salonId)
                dbHelper.close()
                calisanlarLayout.removeAllViews()
                if (calisanlar.isEmpty()) {
                    val emptyText = TextView(this)
                    emptyText.text = "Çalışan yok."
                    calisanlarLayout.addView(emptyText)
                } else {
                    calisanlar.forEach { calisan ->
                        val row = LinearLayout(this)
                        row.orientation = LinearLayout.HORIZONTAL
                        val textView = TextView(this)
                        textView.text = calisan["name"] + (if (!calisan["role"].isNullOrBlank()) " - ${calisan["role"]}" else "")
                        textView.textSize = 16f
                        textView.setPadding(8, 8, 8, 8)
                        val btnSil = Button(this)
                        btnSil.text = "Sil"
                        btnSil.setOnClickListener {
                            try {
                                dbHelper.openDatabase()
                                dbHelper.deleteEmployee(calisan["id"]!!)
                                dbHelper.close()
                                calisanlariGoster()
                            } catch (e: Exception) {
                                Toast.makeText(this, "Çalışan silinirken hata oluştu!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        row.addView(textView)
                        row.addView(btnSil)
                        calisanlarLayout.addView(row)
                    }
                }
            } catch (e: Exception) {
                calisanlarLayout.removeAllViews()
                val errorText = TextView(this)
                errorText.text = "Çalışanlar yüklenemedi! (Tablo eksik veya veritabanı hatası)"
                calisanlarLayout.addView(errorText)
            }
        }

        btnProfilKaydet.setOnClickListener {
            val yeniYazi = editTextProfilYazisi.text.toString().trim()
            dbHelper.openDatabase()
            dbHelper.updateSalonProfileText(salonId, yeniYazi)
            dbHelper.close()
            Toast.makeText(this, "Profil yazısı güncellendi", Toast.LENGTH_SHORT).show()
        }

        btnCalisanEkle.setOnClickListener {
            val ad = editTextCalisanAdi.text.toString().trim()
            val rol = editTextCalisanRol.text.toString().trim()
            if (ad.isEmpty()) {
                Toast.makeText(this, "Çalışan adı girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                dbHelper.openDatabase()
                dbHelper.addEmployee(salonId, ad, rol)
                dbHelper.close()
                editTextCalisanAdi.text.clear()
                editTextCalisanRol.text.clear()
                calisanlariGoster()
            } catch (e: Exception) {
                Toast.makeText(this, "Çalışan eklenemedi! (Tablo eksik veya veritabanı hatası)", Toast.LENGTH_SHORT).show()
            }
        }

        calisanlariGoster()
    }
} 