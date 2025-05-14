package com.example.ekrandeneme

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class GecmisRandevularActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gecmis_randevular)

        val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
        val userEmail = sharedPref.getString("email", "") ?: ""
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val randevular = mutableListOf<Map<String, String>>()
        val cursor = dbHelper.database?.query(
            "appointments",
            null,
            "user_email = ?",
            arrayOf(userEmail),
            null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val randevu = mutableMapOf<String, String>()
                randevu["id"] = it.getString(it.getColumnIndexOrThrow("id"))
                randevu["hizmet"] = it.getString(it.getColumnIndexOrThrow("hizmet"))
                randevu["tarih"] = it.getString(it.getColumnIndexOrThrow("tarih"))
                randevu["saat"] = it.getString(it.getColumnIndexOrThrow("saat"))
                randevu["status"] = it.getString(it.getColumnIndexOrThrow("status"))
                randevu["salon_id"] = it.getString(it.getColumnIndexOrThrow("salon_id"))
                val employeeIdIdx = it.getColumnIndex("employee_id")
                if (employeeIdIdx != -1) {
                    randevu["employee_id"] = it.getString(employeeIdIdx)
                }
                randevular.add(randevu)
            }
        }
        dbHelper.close()

        // --- YENİ REJECTED RANDEVULARI BİLDİR ---
        val notifiedIds = sharedPref.getStringSet("notifiedRejectedIds", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        val newRejected = randevular.filter { it["status"] == "REJECTED" && it["id"] != null && !notifiedIds.contains(it["id"]) }
        if (newRejected.isNotEmpty()) {
            val dbHelper = DatabaseHelper(this)
            dbHelper.openDatabase()
            val message = newRejected.joinToString("\n\n") {
                val hizmet = it["hizmet"] ?: ""
                val tarih = it["tarih"] ?: ""
                val saat = it["saat"] ?: ""
                val employeeId = it["employee_id"]
                var calisanBilgi = "Çalışan: Belirtilmemiş"
                if (!employeeId.isNullOrBlank()) {
                    val cursor = dbHelper.database?.query(
                        "employees",
                        arrayOf("name", "role"),
                        "id = ?",
                        arrayOf(employeeId),
                        null, null, null
                    )
                    cursor?.use { c ->
                        if (c.moveToFirst()) {
                            val ad = c.getString(c.getColumnIndexOrThrow("name"))
                            val rol = c.getString(c.getColumnIndexOrThrow("role"))
                            calisanBilgi = "Çalışan: $ad" + if (!rol.isNullOrBlank()) " - $rol" else ""
                        }
                    }
                }
                "$tarih $saat tarihinde, $hizmet için $calisanBilgi randevunuz onaylanamadı.\nLütfen başka bir saat veya çalışan seçin."
            }
            dbHelper.close()
            android.app.AlertDialog.Builder(this)
                .setTitle("Randevu Talebiniz Reddedildi")
                .setMessage(message)
                .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                .show()
            // Bildirilenleri kaydet
            newRejected.forEach { it["id"]?.let { id -> notifiedIds.add(id) } }
            sharedPref.edit().putStringSet("notifiedRejectedIds", notifiedIds).apply()
        }

        val layout = findViewById<LinearLayout>(R.id.randevularLayout)
        val btnBekleyen = findViewById<Button>(R.id.btnBekleyen)
        val btnGecmis = findViewById<Button>(R.id.btnGecmis)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val bugun = Calendar.getInstance().time

        fun gosterRandevular(tip: String) {
            layout.removeAllViews()
            val list = when (tip) {
                "bekleyen" -> randevular.filter {
                    try {
                        val randevuTarihi = sdf.parse(it["tarih"] ?: "")
                        val status = it["status"] ?: ""
                        (status == "PENDING" || status == "APPROVED") && randevuTarihi != null && !randevuTarihi.before(bugun)
                    } catch (e: Exception) { false }
                }
                "gecmis" -> randevular.filter {
                    try {
                        val randevuTarihi = sdf.parse(it["tarih"] ?: "")
                        randevuTarihi != null && randevuTarihi.before(bugun)
                    } catch (e: Exception) { false }
                }
                else -> emptyList()
            }
            if (list.isEmpty()) {
                val emptyText = TextView(this)
                emptyText.text = if (tip == "bekleyen") "Bekleyen randevunuz yok." else "Geçmiş randevunuz yok."
                layout.addView(emptyText)
            } else {
                val dbHelper = DatabaseHelper(this)
                dbHelper.openDatabase()
                list.forEach { randevu ->
                    val employeeId = randevu["employee_id"]
                    var calisanBilgi = "Çalışan: Belirtilmemiş"
                    if (!employeeId.isNullOrBlank()) {
                        val cursor = dbHelper.database?.query(
                            "employees",
                            arrayOf("name", "role"),
                            "id = ?",
                            arrayOf(employeeId),
                            null, null, null
                        )
                        cursor?.use {
                            if (it.moveToFirst()) {
                                val ad = it.getString(it.getColumnIndexOrThrow("name"))
                                val rol = it.getString(it.getColumnIndexOrThrow("role"))
                                calisanBilgi = "Çalışan: $ad" + if (!rol.isNullOrBlank()) " - $rol" else ""
                            }
                        }
                    }
                    val textView = TextView(this)
                    textView.text = "Hizmet: ${randevu["hizmet"]}\nTarih: ${randevu["tarih"]}  Saat: ${randevu["saat"]}  Durum: ${randevu["status"]}\n$calisanBilgi"
                    textView.textSize = 16f
                    textView.setPadding(8, 8, 8, 8)
                    layout.addView(textView)
                }
                dbHelper.close()
            }
        }

        btnBekleyen.setOnClickListener {
            gosterRandevular("bekleyen")
        }
        btnGecmis.setOnClickListener {
            gosterRandevular("gecmis")
        }
        // Varsayılan olarak bekleyenleri göster
        gosterRandevular("bekleyen")
    }
} 