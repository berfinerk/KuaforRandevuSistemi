package com.example.ekrandeneme

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper

class IsletmeHizmetlerActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var salonId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isletme_hizmetler)

        val isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salon = dbHelper.getAllSalons().find { it["name"] == isletmeAdi }
        salonId = salon?.get("id") ?: ""
        dbHelper.close()

        val editTextHizmetAdi = findViewById<EditText>(R.id.editTextHizmetAdi)
        val editTextHizmetFiyat = findViewById<EditText>(R.id.editTextHizmetFiyat)
        val btnHizmetEkle = findViewById<Button>(R.id.btnHizmetEkle)
        val hizmetlerLayout = findViewById<LinearLayout>(R.id.hizmetlerLayout)
        val calisanSecimLayout = findViewById<LinearLayout>(R.id.calisanSecimLayout)

        fun hizmetleriGoster() {
            dbHelper.openDatabase()
            val hizmetler = if (salonId.isNotEmpty()) dbHelper.getServicesForSalon(salonId) else emptyList()
            dbHelper.close()
            hizmetlerLayout.removeAllViews()
            if (hizmetler.isEmpty()) {
                val emptyText = TextView(this)
                emptyText.text = getString(R.string.service_not_found)
                hizmetlerLayout.addView(emptyText)
            } else {
                hizmetler.forEach { (hizmet, ucret) ->
                    val row = LinearLayout(this)
                    row.orientation = LinearLayout.HORIZONTAL
                    val textView = TextView(this)
                    textView.text = getString(R.string.service_and_fee, hizmet, ucret)
                    textView.textSize = 16f
                    textView.setPadding(8, 8, 8, 8)
                    val btnSil = Button(this)
                    btnSil.text = getString(R.string.delete)
                    btnSil.setOnClickListener {
                        dbHelper.openDatabase()
                        dbHelper.database?.delete(
                            "services",
                            "salon_id = ? AND name = ?",
                            arrayOf(salonId, hizmet)
                        )
                        dbHelper.close()
                        hizmetleriGoster()
                    }
                    val btnDuzenle = Button(this)
                    btnDuzenle.text = getString(R.string.edit)
                    btnDuzenle.setOnClickListener {
                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle(getString(R.string.edit_service))
                        val layout = LinearLayout(this)
                        layout.orientation = LinearLayout.VERTICAL
                        val inputAd = EditText(this)
                        inputAd.setText(hizmet)
                        val inputFiyat = EditText(this)
                        inputFiyat.setText(ucret)
                        layout.addView(inputAd)
                        layout.addView(inputFiyat)
                        dialog.setView(layout)
                        dialog.setPositiveButton(getString(R.string.save)) { _, _ ->
                            dbHelper.openDatabase()
                            val values = android.content.ContentValues().apply {
                                put("name", inputAd.text.toString())
                                put("price", inputFiyat.text.toString())
                            }
                            dbHelper.database?.update(
                                "services",
                                values,
                                "salon_id = ? AND name = ?",
                                arrayOf(salonId, hizmet)
                            )
                            dbHelper.close()
                            hizmetleriGoster()
                        }
                        dialog.setNegativeButton(getString(R.string.cancel), null)
                        dialog.show()
                    }
                    row.addView(textView)
                    row.addView(btnDuzenle)
                    row.addView(btnSil)
                    hizmetlerLayout.addView(row)
                }
            }
        }

        fun calisanSecimleriniGoster() {
            calisanSecimLayout.removeAllViews()
            dbHelper.openDatabase()
            val calisanlar = dbHelper.getEmployeesForSalon(salonId)
            dbHelper.close()
            calisanlar.forEach { calisan ->
                val checkBox = CheckBox(this)
                checkBox.text = calisan["name"] + (if (!calisan["role"].isNullOrBlank()) " - ${calisan["role"]}" else "")
                checkBox.tag = calisan["id"]
                calisanSecimLayout.addView(checkBox)
            }
        }

        btnHizmetEkle.setOnClickListener {
            val ad = editTextHizmetAdi.text.toString().trim()
            val fiyat = editTextHizmetFiyat.text.toString().trim()
            if (ad.isEmpty() || fiyat.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_service_name_and_fee), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val seciliCalisanIdler = mutableListOf<String>()
            for (i in 0 until calisanSecimLayout.childCount) {
                val cb = calisanSecimLayout.getChildAt(i)
                if (cb is CheckBox && cb.isChecked) {
                    seciliCalisanIdler.add(cb.tag as String)
                }
            }
            if (seciliCalisanIdler.isEmpty()) {
                Toast.makeText(this, getString(R.string.select_at_least_one_employee), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dbHelper.openDatabase()
            val serviceId = dbHelper.database?.insert("services", null, android.content.ContentValues().apply {
                put("salon_id", salonId)
                put("name", ad)
                put("price", fiyat)
            }) ?: -1
            if (serviceId > 0) {
                seciliCalisanIdler.forEach { calisanId ->
                    dbHelper.database?.insert("service_employees", null, android.content.ContentValues().apply {
                        put("service_id", serviceId)
                        put("employee_id", calisanId)
                    })
                }
                Toast.makeText(this, getString(R.string.service_and_employees_saved), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.service_add_failed), Toast.LENGTH_SHORT).show()
            }
            dbHelper.close()
            editTextHizmetAdi.text.clear()
            editTextHizmetFiyat.text.clear()
            calisanSecimleriniGoster()
            hizmetleriGoster()
        }

        hizmetleriGoster()
        calisanSecimleriniGoster()
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