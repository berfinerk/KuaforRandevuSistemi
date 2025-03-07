package com.example.ekrandeneme

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainGuzellikMerkeziListesiBinding
import com.example.ekrandeneme.databinding.ActivityMainKuaforListesiBinding

class MainGuzellikMerkeziListesi : AppCompatActivity() {
    private lateinit var binding: ActivityMainGuzellikMerkeziListesiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGuzellikMerkeziListesiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}