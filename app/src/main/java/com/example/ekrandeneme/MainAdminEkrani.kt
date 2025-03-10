package com.example.ekrandeneme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityMainAdminEkraniBinding

class MainAdminEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminEkraniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
} 