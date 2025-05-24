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
        setAppLocale()
        super.onCreate(savedInstanceState)
        binding = ActivityIsletmeDetayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isletmeId = intent.getStringExtra("isletmeId")
        if (isletmeId.isNullOrBlank()) {
            android.widget.Toast.makeText(this, getString(R.string.business_not_found), android.widget.Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salon = dbHelper.getSalonById(isletmeId)
        if (salon.isEmpty()) {
            android.widget.Toast.makeText(this, getString(R.string.business_info_not_found), android.widget.Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val hizmetler = dbHelper.getServicesForSalon(isletmeId)
        val profilYazisi = try { dbHelper.getSalonProfileText(isletmeId) } catch (e: Exception) { "" }
        val calisanlar = try { dbHelper.getEmployeesForSalon(isletmeId) } catch (e: Exception) { emptyList() }
        dbHelper.close()

        binding.textViewIsletmeAdi.text = salon["name"] ?: getString(R.string.business)
        binding.textViewAdres.text = salon["address"] ?: ""
        binding.textViewAciklama.text = getString(R.string.professional_services)
        binding.ratingBar.rating = DatabaseHelper(this).apply { openDatabase() }.getSalonAverageRating(isletmeId)
        binding.textViewProfilYazisi.text = if (profilYazisi.isNotBlank()) profilYazisi else getString(R.string.profile_text_not_added)

        if (hizmetler.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = getString(R.string.service_not_found)
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
                btnRandevuAl.text = getString(R.string.book_appointment)
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
            builder.setTitle(getString(R.string.employees))
            val calisanListesi = if (calisanlar.isEmpty()) {
                getString(R.string.employee_not_found)
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
            builder.setPositiveButton(getString(R.string.close), null)
            builder.show()
        }
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