package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainBinding
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding

class MainKayitOl : AppCompatActivity() {
    private lateinit var binding: ActivityMainKayitOlBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKayitOlBinding.inflate(layoutInflater)
        val ilksayfa = binding.root
        setContentView(ilksayfa)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnKayitKullaniciKayitOl.setOnClickListener{
            intent= Intent(this,MainKullaniciEkrani::class.java)
            startActivity(intent)
        }

    }
}