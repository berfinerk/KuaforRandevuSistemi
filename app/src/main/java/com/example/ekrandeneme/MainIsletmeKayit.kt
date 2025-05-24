package com.example.ekrandeneme

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainIsletmeKayitBinding

class MainIsletmeKayit : AppCompatActivity() {
    private lateinit var binding: ActivityMainIsletmeKayitBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale()
        super.onCreate(savedInstanceState)
        binding = ActivityMainIsletmeKayitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        binding.btnKayitOl.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordAgain = binding.editTextPasswordAgain.text.toString()
            val address = binding.editTextAddress.text.toString()
            val phone = binding.editTextPhone.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != passwordAgain) {
                Toast.makeText(this, getString(R.string.passwords_not_match), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Telefon kontrolü
            if (!phone.matches(Regex("^0[0-9]{10}$"))) {
                Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // E-posta kontrolü
            val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            if (!emailPattern.matches(email)) {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Şifre kontrolü
            if (password.length < 8 || password.length > 32) {
                Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!password.matches(Regex(".*\\d.*")) || !password.matches(Regex(".*[!@#${'$'}%^&*()_+=|<>?{}\\[\\]~-].*"))) {
                Toast.makeText(this, getString(R.string.password_requirements), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // İşletme tipini belirle
            val salonType = if (binding.radioButtonKuafor.isChecked) "KUAFOR" else "GUZELLIK_MERKEZI"

            try {
                // Önce kullanıcıyı ekle
                val userId = dbHelper.addUser(email, password, name, "BUSINESS")
                if (userId > 0) {
                    // Sonra salonu ekle
                    val salonId = dbHelper.addSalon(name, address, phone, email, salonType)
                    if (salonId > 0) {
                        // ÖRNEK HİZMETLERİ EKLE (sadece kuaför için)
                        if (salonType == "KUAFOR") {
                            dbHelper.addService(salonId.toString(), getString(R.string.manicure), "200₺")
                            dbHelper.addService(salonId.toString(), getString(R.string.pedicure), "250₺")
                            dbHelper.addService(salonId.toString(), getString(R.string.haircut), "300₺")
                        } else if (salonType == "GUZELLIK_MERKEZI") {
                            dbHelper.addService(salonId.toString(), getString(R.string.skin_care), "500₺")
                            dbHelper.addService(salonId.toString(), getString(R.string.eyebrow_design), "150₺")
                        }
                        Toast.makeText(this, getString(R.string.success_register), Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.salon_register_fail), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.user_register_fail), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.fail_register, e.message), Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
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

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
} 