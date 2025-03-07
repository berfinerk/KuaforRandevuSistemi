package com.example.ekrandeneme

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding
import com.example.ekrandeneme.databinding.ActivityMainKuaforListesiBinding

class MainKuaforListesi : AppCompatActivity() {
    private lateinit var binding: ActivityMainKuaforListesiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKuaforListesiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}