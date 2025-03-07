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
        binding =ActivityMainKullaniciEkraniBinding.inflate(layoutInflater)
        val kullanıcısayfası = binding.root
        setContentView(kullanıcısayfası)

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



    }
}