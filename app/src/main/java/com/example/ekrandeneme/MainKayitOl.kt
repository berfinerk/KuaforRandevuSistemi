package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding

class MainKayitOl : AppCompatActivity() {
    private lateinit var binding: ActivityMainKayitOlBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKayitOlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        binding.btnKayitOl.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordAgain = binding.editTextPasswordAgain.text.toString()
            val name = binding.editTextName.text.toString()
            val phone = binding.editTextPhone.text.toString()

            if (email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != passwordAgain) {
                Toast.makeText(this, "Şifreler aynı değil!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Telefon kontrolü
            if (!phone.matches(Regex("^0[0-9]{10}$"))) {
                Toast.makeText(this, "Telefon numarası 0 ile başlamalı ve 11 haneli olmalı!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // E-posta kontrolü
            val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            if (!emailPattern.matches(email)) {
                Toast.makeText(this, "Geçerli bir e-posta adresi girin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Şifre kontrolü
            if (password.length < 8 || password.length > 32) {
                Toast.makeText(this, "Şifre 8-32 karakter olmalı!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!password.matches(Regex(".*\\d.*")) || !password.matches(Regex(".*[!@#${'$'}%^&*()_+=|<>?{}\\[\\]~-].*"))) {
                Toast.makeText(this, "Şifre en az bir sayı ve bir sembol içermeli!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val userId = dbHelper.addUser(email, password, name, "CUSTOMER")
                if (userId > 0) {
                    Toast.makeText(this, "Kayıt başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Kayıt başarısız. Bu e-posta adresi zaten kullanılıyor olabilir.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Kayıt sırasında bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}