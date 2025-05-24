package com.example.ekrandeneme

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainSifremiUnuttumBinding

class MainSifremiUnuttum : AppCompatActivity() {
    private lateinit var binding: ActivityMainSifremiUnuttumBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale()
        super.onCreate(savedInstanceState)
        binding = ActivityMainSifremiUnuttumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.btnKodGonder.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            if (email.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_email_address), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!emailPattern.matches(email)) {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dbHelper.openDatabase()
            val userExists = dbHelper.getUserName(email) != getString(R.string.unknown_user)
            dbHelper.close()
            if (!userExists) {
                Toast.makeText(this, getString(R.string.fail_password_reset), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, getString(R.string.code_sent), Toast.LENGTH_LONG).show()
            binding.editTextKodLayout.visibility = android.view.View.VISIBLE
            binding.editTextKod.visibility = android.view.View.VISIBLE
            binding.editTextYeniSifreLayout.visibility = android.view.View.VISIBLE
            binding.editTextYeniSifre.visibility = android.view.View.VISIBLE
            binding.btnSifreYenile.visibility = android.view.View.VISIBLE
        }

        binding.btnSifreYenile.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val kod = binding.editTextKod.text.toString().trim()
            val yeniSifre = binding.editTextYeniSifre.text.toString()

            if (kod.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_code), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (kod.length != 6) {
                Toast.makeText(this, getString(R.string.code_length), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (yeniSifre.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_new_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (yeniSifre.length < 8 || yeniSifre.length > 32 ||
                !yeniSifre.matches(Regex(".*\\d.*")) ||
                !yeniSifre.matches(Regex(".*[!@#${'$'}%^&*()_+=|<>?{}\\[\\]~-].*"))) {
                Toast.makeText(this, getString(R.string.password_requirements_full), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dbHelper.openDatabase()
            val updated = dbHelper.updateUserPassword(email, yeniSifre)
            dbHelper.close()
            if (updated > 0) {
                Toast.makeText(this, getString(R.string.success_password_reset), Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.fail_password_reset), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
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