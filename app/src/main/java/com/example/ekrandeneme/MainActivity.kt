package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

<<<<<<< HEAD
=======
        // Test için örnek veri ekle
        try {
            // Test müşteri kullanıcısı
            val customerId = dbHelper.addUser("test@test.com", "123456", "Test Kullanıcı", "CUSTOMER")
            
            // Test işletme kullanıcısı
            val businessId = dbHelper.addUser("isletme@test.com", "123456", "Test İşletme", "BUSINESS")
            
            // Test salonları
            if (businessId > 0) {
                dbHelper.addSalon("Güzel Saçlar Kuaför", "Atatürk Cad. No:123", "05551234567", "KUAFOR", businessId)
                dbHelper.addSalon("Beauty Center", "İnönü Cad. No:456", "05557654321", "GUZELLIK_MERKEZI", businessId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
        // Giriş yap butonu tıklama olayı
        binding.btnGirisYap.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta ve şifrenizi girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

<<<<<<< HEAD
            // Kullanıcı adı ve şifre kontrolü
            if (dbHelper.checkUser(email, password)) {
                val userType = dbHelper.getUserType(email)
                
                // E-posta adresini SharedPreferences'a kaydet
                val sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE)
                sharedPreferences.edit().apply {
                    putString("email", email)
                    apply()
=======
            try {
                if (dbHelper.checkUser(email, password)) {
                    val userType = dbHelper.getUserType(email)
                    Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                    
                    // Kullanıcı tipine göre yönlendirme
                    when (userType) {
                        "CUSTOMER" -> startActivity(Intent(this, MainKullaniciEkrani::class.java))
                        "BUSINESS" -> startActivity(Intent(this, MainIsletmeEkrani::class.java))
                        "ADMIN" -> startActivity(Intent(this, MainAdminEkrani::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Geçersiz e-posta veya şifre", Toast.LENGTH_SHORT).show()
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
                }
                
                Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                
                // Kullanıcı tipine göre yönlendirme
                when (userType) {
                    "CUSTOMER" -> startActivity(Intent(this, MainKullaniciEkrani::class.java))
                    "BUSINESS" -> startActivity(Intent(this, MainIsletmeEkrani::class.java))
                    "ADMIN" -> startActivity(Intent(this, MainAdminEkrani::class.java))
                    else -> {
                        Toast.makeText(this, "Kullanıcı tipi tanımlanamadı", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                finish()
            } else {
                Toast.makeText(this, "Geçersiz şifre", Toast.LENGTH_SHORT).show()
            }
        }

        // Kayıt ol butonu tıklama olayı
        binding.btnKayitOl.setOnClickListener {
            startActivity(Intent(this, MainKayitOl::class.java))
        }

        // Şifremi unuttum butonu tıklama olayı
        binding.btnSifremiUnuttum.setOnClickListener {
            startActivity(Intent(this, MainSifremiUnuttum::class.java))
        }

        // İşletme girişi butonu tıklama olayı
        binding.btnIsletmeGirisi.setOnClickListener {
            startActivity(Intent(this, MainIsletmeGiris::class.java))
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}