package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainBinding
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding
import com.example.ekrandeneme.databinding.ActivityMainKullaniciEkraniBinding

class MainKullaniciEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainKullaniciEkraniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKullaniciEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.kuaforBtn.setOnClickListener{
            intent= Intent(this,MainKuaforListesi::class.java)
            startActivity(intent)
        }
        binding.guzellikBtn.setOnClickListener{
            intent= Intent(this,MainGuzellikMerkeziListesi::class.java)
            startActivity(intent)
        }

        // Çıkış yap butonu ekle
        binding.btnCikis.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.btnGecmisRandevular.setOnClickListener {
            val intent = Intent(this, GecmisRandevularActivity::class.java)
            startActivity(intent)
        }
    }
}