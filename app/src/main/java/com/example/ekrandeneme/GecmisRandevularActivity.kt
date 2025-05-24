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
        setAppLocale()
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
                var calisanBilgi = getString(R.string.employee_not_specified)
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
                            calisanBilgi = getString(R.string.employee_colon) + ad + if (!rol.isNullOrBlank()) " - $rol" else ""
                        }
                    }
                }
                getString(R.string.appointment_rejected_message, tarih, saat, hizmet, calisanBilgi)
            }
            dbHelper.close()
            android.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.appointment_rejected_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
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
                emptyText.text = if (tip == "bekleyen") getString(R.string.no_pending_appointment) else getString(R.string.no_past_appointment)
                layout.addView(emptyText)
            } else {
                val dbHelper = DatabaseHelper(this)
                dbHelper.openDatabase()
                list.forEach { randevu ->
                    val employeeId = randevu["employee_id"]
                    var calisanBilgi = getString(R.string.employee_not_specified)
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
                                calisanBilgi = getString(R.string.employee_colon) + ad + if (!rol.isNullOrBlank()) " - $rol" else ""
                            }
                        }
                    }
                    val textView = TextView(this)
                    textView.text = getString(R.string.appointment_info, randevu["hizmet"], randevu["tarih"], randevu["saat"], randevu["status"], calisanBilgi)
                    textView.textSize = 16f
                    textView.setPadding(8, 8, 8, 8)
                    layout.addView(textView)

                    // PUANLAMA ARAYÜZÜ
                    val randevuId = randevu["id"] ?: ""
                    val status = randevu["status"] ?: ""
                    if (status == "APPROVED" && !employeeId.isNullOrBlank()) {
                        val alreadyRated = dbHelper.hasRatingForAppointment(randevuId)
                        if (!alreadyRated) {
                            val btnPuanla = Button(this)
                            btnPuanla.text = getString(R.string.rate_and_comment)
                            btnPuanla.setOnClickListener {
                                val dialogView = layoutInflater.inflate(R.layout.dialog_rating, null)
                                val ratingBar = dialogView.findViewById<android.widget.RatingBar>(R.id.dialogRatingBar)
                                val yorumEdit = dialogView.findViewById<android.widget.EditText>(R.id.dialogEditTextYorum)
                                val dialog = android.app.AlertDialog.Builder(this)
                                    .setTitle(getString(R.string.rate_and_comment_title))
                                    .setView(dialogView)
                                    .setPositiveButton(getString(R.string.save), null)
                                    .setNegativeButton(getString(R.string.cancel), null)
                                    .create()
                                dialog.setOnShowListener {
                                    val btn = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                                    btn.setOnClickListener {
                                        val puan = ratingBar.rating.toInt()
                                        val yorum = yorumEdit.text.toString().trim()
                                        if (puan in 1..5) {
                                            val dbHelperDialog = com.example.ekrandeneme.database.DatabaseHelper(this)
                                            dbHelperDialog.openDatabase()
                                            val result = dbHelperDialog.addRating(employeeId, userEmail, randevuId, puan, yorum)
                                            dbHelperDialog.close()
                                            if (result > 0) {
                                                android.widget.Toast.makeText(this, getString(R.string.rating_saved), android.widget.Toast.LENGTH_SHORT).show()
                                                dialog.dismiss()
                                                gosterRandevular(tip) // Ekranı yenile
                                            } else {
                                                android.widget.Toast.makeText(this, getString(R.string.rating_save_failed), android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            android.widget.Toast.makeText(this, getString(R.string.rating_range_error), android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                dialog.show()
                            }
                            layout.addView(btnPuanla)
                        } else {
                            // Puan ve yorumu göster
                            val cursor = dbHelper.database?.rawQuery(
                                "SELECT rating, comment FROM ratings WHERE randevu_id = ?",
                                arrayOf(randevuId)
                            )
                            cursor?.use {
                                if (it.moveToFirst()) {
                                    val puan = it.getInt(it.getColumnIndexOrThrow("rating"))
                                    val yorum = it.getString(it.getColumnIndexOrThrow("comment"))
                                    val puanText = TextView(this)
                                    puanText.text = getString(R.string.your_rating, puan)
                                    puanText.setPadding(8, 0, 8, 8)
                                    layout.addView(puanText)
                                    if (!yorum.isNullOrBlank()) {
                                        val yorumText = TextView(this)
                                        yorumText.text = getString(R.string.your_comment, yorum)
                                        yorumText.setPadding(8, 0, 8, 8)
                                        layout.addView(yorumText)
                                    }
                                }
                            }
                        }
                    }
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

    private fun setAppLocale() {
        val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
        val lang = sharedPref.getString("lang", "tr") ?: "tr"
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
} 