package com.example.ekrandeneme

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val ilksayfa = binding.root
        setContentView(ilksayfa)
        binding.btnGirisYap.setOnClickListener {
            val kllncsyfs=Intent(applicationContext,MainKullaniciEkrani::class.java)
            startActivity(kllncsyfs)
        }
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGirisKayitOl.setOnClickListener{
            intent= Intent(this,MainKayitOl::class.java)
            startActivity(intent)
        }

        binding.btnGirisYap.setOnClickListener{
            intent= Intent(this,MainKullaniciEkrani::class.java)
            startActivity(intent)
        }

        val textView = findViewById<TextView>(R.id.uyeOlmaDevam)
        textView.setOnClickListener {
            // Yeni Activity'ye yönlendirme
            val intent = Intent(this, MainKullaniciEkrani::class.java)
            startActivity(intent)
        }

        val textView2 = findViewById<TextView>(R.id.parolaYenile)
        textView2.setOnClickListener {
            // Yeni Activity'ye yönlendirme
            val intent = Intent(this, MainParolaYenileme::class.java)
            startActivity(intent)
        }



    }

}