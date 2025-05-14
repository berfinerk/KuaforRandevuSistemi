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

        // Giriş yapan işletmenin adı intent ile alınır
        val isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        android.widget.Toast.makeText(this, "Panelde işletme adı: $isletmeAdi", android.widget.Toast.LENGTH_LONG).show()

        // Randevular butonu tıklama olayı
        binding.btnRandevular.setOnClickListener {
            val intent = Intent(this, IsletmeRandevularActivity::class.java)
            intent.putExtra("isletmeAdi", isletmeAdi)
            startActivity(intent)
        }

        // Hizmetler butonu tıklama olayı
        binding.btnHizmetler.setOnClickListener {
            val intent = Intent(this, IsletmeHizmetlerActivity::class.java)
            intent.putExtra("isletmeAdi", isletmeAdi)
            startActivity(intent)
        }

        // Profil butonu tıklama olayı
        binding.btnProfil.setOnClickListener {
            val intent = Intent(this, IsletmeProfilActivity::class.java)
            intent.putExtra("isletmeAdi", isletmeAdi)
            startActivity(intent)
        }

        // Geri Dönüşler butonu tıklama olayı
        binding.btnGeriDonusler.setOnClickListener {
            val intent = Intent(this, GeriDonuslerActivity::class.java)
            intent.putExtra("isletmeAdi", isletmeAdi)
            startActivity(intent)
        }

        // Çıkış yap butonu tıklama olayı
        binding.btnCikis.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
} 