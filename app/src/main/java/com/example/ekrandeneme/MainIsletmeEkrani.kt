package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityMainIsletmeEkraniBinding

class MainIsletmeEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainIsletmeEkraniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainIsletmeEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Randevular butonu tıklama olayı
        binding.btnRandevular.setOnClickListener {
            // TODO: Randevular ekranına yönlendir
        }

        // Hizmetler butonu tıklama olayı
        binding.btnHizmetler.setOnClickListener {
            // TODO: Hizmetler ekranına yönlendir
        }

        // Profil butonu tıklama olayı
        binding.btnProfil.setOnClickListener {
            // TODO: Profil ekranına yönlendir
        }

        // Çıkış yap butonu tıklama olayı
        binding.btnCikis.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
} 