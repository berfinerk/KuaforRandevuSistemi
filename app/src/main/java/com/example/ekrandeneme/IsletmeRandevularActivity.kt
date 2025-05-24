package com.example.ekrandeneme

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

class IsletmeRandevularActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isletme_randevular)

        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        val salon = dbHelper.getAllSalons().find { it["name"] == isletmeAdi }
        val salonId = salon?.get("id") ?: ""
        val randevular = if (salonId.isNotEmpty()) {
            val list = mutableListOf<Map<String, String>>()
            val cursor = dbHelper.database?.query(
                "appointments",
                null,
                "salon_id = ?",
                arrayOf(salonId),
                null, null, null
            )
            cursor?.use {
                while (it.moveToNext()) {
                    val randevu = mutableMapOf<String, String>()
                    randevu["id"] = it.getString(it.getColumnIndexOrThrow("id"))
                    randevu["user_email"] = it.getString(it.getColumnIndexOrThrow("user_email"))
                    randevu["hizmet"] = it.getString(it.getColumnIndexOrThrow("hizmet"))
                    randevu["tarih"] = it.getString(it.getColumnIndexOrThrow("tarih"))
                    randevu["saat"] = it.getString(it.getColumnIndexOrThrow("saat"))
                    randevu["status"] = it.getString(it.getColumnIndexOrThrow("status"))
                    randevu["salon_id"] = salonId
                    // Fiyatı services tablosundan çek
                    val hizmetAdi = randevu["hizmet"] ?: ""
                    val fiyat = dbHelper.getServicesForSalon(salonId).find { it.first == hizmetAdi }?.second ?: "0"
                    randevu["price"] = fiyat
                    // Çalışan id'sini ekle
                    val empIdx = it.getColumnIndex("employee_id")
                    if (empIdx != -1) {
                        randevu["employee_id"] = it.getString(empIdx)
                    }
                    list.add(randevu)
                }
            }
            list
        } else emptyList()
        dbHelper.close()

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
            var toplamFiyat = 0.0
            if (list.isEmpty()) {
            val emptyText = TextView(this)
                emptyText.text = if (tip == "bekleyen") "Bekleyen randevu yok." else "Geçmiş randevu yok."
            layout.addView(emptyText)
        } else {
                val dbHelper = DatabaseHelper(this)
                dbHelper.openDatabase()
                list.forEach { randevu ->
                val randevuLayout = LinearLayout(this)
                randevuLayout.orientation = LinearLayout.VERTICAL
                randevuLayout.setPadding(16, 16, 16, 16)
                val userName = dbHelper.getUserName(randevu["user_email"] ?: "")
                    val fiyatStr = randevu["price"] ?: "0"
                    val fiyatNum = fiyatStr.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
                    toplamFiyat += fiyatNum
                    // Çalışan bilgisi
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
                val info = TextView(this)
                    info.text = "Kullanıcı: $userName\nHizmet: ${randevu["hizmet"]}\nTarih: ${randevu["tarih"]}  Saat: ${randevu["saat"]}\nFiyat: $fiyatStr\n$calisanBilgi\nDurum: ${randevu["status"]}"
                randevuLayout.addView(info)
                if (randevu["status"] == "PENDING") {
                    val btnOnayla = Button(this)
                    btnOnayla.text = "Onayla"
                    btnOnayla.setOnClickListener {
                        dbHelper.openDatabase()
                        dbHelper.updateAppointmentStatus(randevu["id"]!!, "APPROVED")
                        dbHelper.close()
                        Toast.makeText(this, "Randevu onaylandı", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                    val btnReddet = Button(this)
                    btnReddet.text = "Reddet"
                    btnReddet.setOnClickListener {
                        dbHelper.openDatabase()
                        dbHelper.updateAppointmentStatus(randevu["id"]!!, "REJECTED")
                        dbHelper.close()
                        Toast.makeText(this, "Randevu reddedildi", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                    randevuLayout.addView(btnOnayla)
                    randevuLayout.addView(btnReddet)
                }
                layout.addView(randevuLayout)
        }
        dbHelper.close()
                val toplamText = TextView(this)
                toplamText.text = "Toplam Fiyat: ${"%.2f".format(toplamFiyat)} TL"
                toplamText.textSize = 18f
                toplamText.setPadding(16, 32, 16, 16)
                layout.addView(toplamText)
            }
        }
        btnBekleyen.setOnClickListener { gosterRandevular("bekleyen") }
        btnGecmis.setOnClickListener { gosterRandevular("gecmis") }
        gosterRandevular("bekleyen")
    }
} 