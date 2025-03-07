package com.example.ekrandeneme

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainBinding
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding
import com.example.ekrandeneme.databinding.ActivityMainParolaYenilemeBinding

class MainParolaYenileme : AppCompatActivity() {
    private lateinit var binding: ActivityMainParolaYenilemeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainParolaYenilemeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}