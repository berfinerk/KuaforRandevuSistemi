package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainIsletmeGirisBinding

class MainIsletmeGiris : AppCompatActivity() {
    private lateinit var binding: ActivityMainIsletmeGirisBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainIsletmeGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        // Test için örnek işletme verisi ekle
        try {
            // Önce kullanıcıyı ekle
            val userId = dbHelper.addUser("isletme@test.com", "123456", "Test İşletme", "BUSINESS")
            if (userId > 0) {
                // Sonra salonu ekle
                dbHelper.addSalon("Test Kuaför", "Test Mahallesi No:1", "05551234567", "KUAFOR", userId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Giriş yap butonu tıklama olayı
        binding.btnGirisYap.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta ve şifrenizi girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                if (dbHelper.checkUser(email, password)) {
                    // Kullanıcı tipini kontrol et
                    val userType = dbHelper.getUserType(email)
                    if (userType == "BUSINESS") {
                        Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainIsletmeEkrani::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Bu hesap bir işletme hesabı değil", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Geçersiz e-posta veya şifre", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Giriş sırasında bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        // Kayıt ol butonu tıklama olayı
        binding.btnKayitOl.setOnClickListener {
            startActivity(Intent(this, MainIsletmeKayit::class.java))
        }

        // Şifremi unuttum butonu tıklama olayı
        binding.btnSifremiUnuttum.setOnClickListener {
            startActivity(Intent(this, MainSifremiUnuttum::class.java))
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
} 