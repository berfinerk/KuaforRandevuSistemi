package com.example.ekrandeneme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainIsletmeKayitBinding

class MainIsletmeKayit : AppCompatActivity() {
    private lateinit var binding: ActivityMainIsletmeKayitBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
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
                            dbHelper.addService(salonId.toString(), "Manikür", "200₺")
                            dbHelper.addService(salonId.toString(), "Pedikür", "250₺")
                            dbHelper.addService(salonId.toString(), "Saç Kesimi", "300₺")
                        } else if (salonType == "GUZELLIK_MERKEZI") {
                            dbHelper.addService(salonId.toString(), "Cilt Bakımı", "500₺")
                            dbHelper.addService(salonId.toString(), "Kaş Tasarımı", "150₺")
                        }
                        Toast.makeText(this, "Kayıt başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Salon kaydı başarısız oldu.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Kullanıcı kaydı başarısız oldu. Bu e-posta adresi zaten kullanılıyor olabilir.", Toast.LENGTH_LONG).show()
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