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

        binding.btnSifreYenile.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            
            if (email.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta adresinizi girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Şifre yenileme işlemi burada yapılacak
            Toast.makeText(this, "Şifre yenileme bağlantısı e-posta adresinize gönderildi", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
} 