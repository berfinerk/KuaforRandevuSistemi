package com.example.ekrandeneme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainSifremiUnuttumBinding

class MainSifremiUnuttum : AppCompatActivity() {
    private lateinit var binding: ActivityMainSifremiUnuttumBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSifremiUnuttumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.btnKodGonder.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            if (email.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta adresinizi girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!emailPattern.matches(email)) {
                Toast.makeText(this, "Geçerli bir e-posta adresi girin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dbHelper.openDatabase()
            val exists = dbHelper.isUserExists(email)
            dbHelper.close()
            if (!exists) {
                Toast.makeText(this, "Bu e-posta ile kayıtlı kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Mailinize 6 haneli kod gönderildi! (Herhangi bir kodu girebilirsiniz)", Toast.LENGTH_LONG).show()
            binding.editTextKod.visibility = android.view.View.VISIBLE
            binding.editTextYeniSifre.visibility = android.view.View.VISIBLE
            binding.btnSifreYenile.visibility = android.view.View.VISIBLE
        }

        binding.btnSifreYenile.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val kod = binding.editTextKod.text.toString().trim()
            val yeniSifre = binding.editTextYeniSifre.text.toString()

            if (kod.isEmpty()) {
                Toast.makeText(this, "Mailinize gönderilen 6 haneli kodu girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (kod.length != 6) {
                Toast.makeText(this, "Kod 6 haneli olmalı!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (yeniSifre.isEmpty()) {
                Toast.makeText(this, "Yeni şifreyi girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (yeniSifre.length < 8 || yeniSifre.length > 32 ||
                !yeniSifre.matches(Regex(".*\\d.*")) ||
                !yeniSifre.matches(Regex(".*[!@#${'$'}%^&*()_+=|<>?{}\\[\\]~-].*"))) {
                Toast.makeText(this, "Şifre 8-32 karakter, en az bir sayı ve bir sembol içermeli!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dbHelper.openDatabase()
            val updated = dbHelper.updateUserPassword(email, yeniSifre)
            dbHelper.close()
            if (updated > 0) {
                Toast.makeText(this, "Şifreniz başarıyla değiştirildi!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Bu e-posta ile kayıtlı kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
} 