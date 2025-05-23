package com.example.ekrandeneme

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainBinding
import java.util.Locale

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


        // Test için örnek veri ekle
        // try {
        //     dbHelper.addUser("test@test.com", "123456", "Test Kullanıcı", "CUSTOMER")
        //     dbHelper.addSalon("Güzel Saçlar Kuaför", "Atatürk Cad. No:123", "05551234567", "kuafor1@gmail.com", "KUAFOR")
        //     dbHelper.addSalon("Beauty Center", "İnönü Cad. No:456", "05557654321", "guzellik1@gmail.com", "GUZELLIK_MERKEZI")
        // } catch (e: Exception) {
        //     e.printStackTrace()
        // }

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
                    val userType = dbHelper.getUserType(email)
                    if (userType == "CUSTOMER") {
                        // Email bilgisini kaydet
                        val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
                        sharedPref.edit().putString("email", email).apply()

                        Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainKullaniciEkrani::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Bu giriş sadece kullanıcılar içindir.", Toast.LENGTH_SHORT).show()
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

        // Üye Olmadan devam et butonu tıklama olayı
        binding.btnGirisYap2.setOnClickListener {
            val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
            val lang = sharedPref.getString("lang", "tr") // dili sakla
            sharedPref.edit().clear().apply()
            if (lang != null) {
                sharedPref.edit().putString("lang", lang).apply()
            }
            startActivity(Intent(this, MainKullaniciEkrani::class.java))
        }

        // Dil seçici Spinner
        val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
        val currentLang = sharedPref.getString("lang", "tr") ?: "tr"
        val spinner = binding.spinnerLanguage
        spinner.setSelection(if (currentLang == "tr") 0 else 1)
        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val lang = if (position == 0) "tr" else "en"
                if (lang != currentLang) {
                    setLocale(lang)
                    sharedPref.edit().putString("lang", lang).apply()
                    recreate()
                }
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}