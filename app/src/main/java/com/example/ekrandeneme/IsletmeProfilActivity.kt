package com.example.ekrandeneme

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper

class IsletmeProfilActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var salonId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isletme_profil)

        val isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salon = dbHelper.getAllSalons().find { it["name"] == isletmeAdi }
        salonId = salon?.get("id") ?: ""
        if (salonId.isBlank()) {
            Toast.makeText(this, getString(R.string.business_not_found), Toast.LENGTH_LONG).show()
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
                    emptyText.text = getString(R.string.employee_not_found)
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
                        btnSil.text = getString(R.string.delete)
                        btnSil.setOnClickListener {
                            try {
                                dbHelper.openDatabase()
                                dbHelper.deleteEmployee(calisan["id"]!!)
                                dbHelper.close()
                                calisanlariGoster()
                            } catch (e: Exception) {
                                Toast.makeText(this, getString(R.string.employee_delete_error), Toast.LENGTH_SHORT).show()
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
                errorText.text = getString(R.string.employees_load_error)
                calisanlarLayout.addView(errorText)
            }
        }

        btnProfilKaydet.setOnClickListener {
            val yeniYazi = editTextProfilYazisi.text.toString().trim()
            dbHelper.openDatabase()
            dbHelper.updateSalonProfileText(salonId, yeniYazi)
            dbHelper.close()
            Toast.makeText(this, getString(R.string.profile_text_updated), Toast.LENGTH_SHORT).show()
        }

        btnCalisanEkle.setOnClickListener {
            val ad = editTextCalisanAdi.text.toString().trim()
            val rol = editTextCalisanRol.text.toString().trim()
            if (ad.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_employee_name), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, getString(R.string.employee_add_error), Toast.LENGTH_SHORT).show()
            }
        }

        val btnGeriDonusler = Button(this)
        btnGeriDonusler.text = getString(R.string.feedbacks)
        btnGeriDonusler.setOnClickListener {
            dbHelper.openDatabase()
            val calisanlar = dbHelper.getEmployeesForSalon(salonId)
            dbHelper.close()
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.employee_ratings_and_comments))
            val scroll = ScrollView(this)
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL
            calisanlar.forEach { calisan ->
                val ad = calisan["name"] ?: "?"
                val role = calisan["role"] ?: ""
                val empId = calisan["id"] ?: return@forEach
                dbHelper.openDatabase()
                val avg = dbHelper.getEmployeeAverageRating(empId)
                val yorumlar = dbHelper.getEmployeeComments(empId)
                dbHelper.close()
                val calisanText = TextView(this)
                calisanText.text = "$ad${if (role.isNotBlank()) " - $role" else ""}  |  ${getString(R.string.average)}: ${"%.1f".format(avg)} ★"
                calisanText.setPadding(8, 16, 8, 4)
                layout.addView(calisanText)
                if (yorumlar.isEmpty()) {
                    val y = TextView(this)
                    y.text = getString(R.string.no_comments_yet)
                    y.setPadding(16, 0, 8, 8)
                    layout.addView(y)
                } else {
                    yorumlar.forEach { (puan, yorum) ->
                        val y = TextView(this)
                        y.text = "${"★".repeat(puan)}${if (puan < 5) "☆".repeat(5-puan) else ""}  $yorum"
                        y.setPadding(16, 0, 8, 8)
                        layout.addView(y)
                    }
                }
            }
            scroll.addView(layout)
            builder.setView(scroll)
            builder.setPositiveButton(getString(R.string.close), null)
            builder.show()
        }

        val anaLayout = findViewById<LinearLayout>(R.id.calisanlarLayout).parent as? LinearLayout
        anaLayout?.addView(btnGeriDonusler)

        calisanlariGoster()
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