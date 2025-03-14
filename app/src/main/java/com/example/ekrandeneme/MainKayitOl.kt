package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val name = binding.editTextName.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                Log.d("MainKayitOl", "Kullanıcı kaydı deneniyor: Email=$email, Name=$name")
                
                // E-posta kontrolü
                if (dbHelper.checkUserExists(email)) {
                    Log.d("MainKayitOl", "Bu e-posta zaten kullanımda: $email")
                    Toast.makeText(this, "Bu e-posta adresi zaten kullanılıyor.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // Kullanıcı ekleme
                val userId = dbHelper.addUser(email, password, name, "CUSTOMER")
                Log.d("MainKayitOl", "Kullanıcı kaydı sonucu: ID=$userId")

                if (userId > 0) {
                    Toast.makeText(this, "Kayıt başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Kayıt işlemi başarısız oldu. Lütfen tekrar deneyin.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("MainKayitOl", "Kayıt hatası: ${e.message}")
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