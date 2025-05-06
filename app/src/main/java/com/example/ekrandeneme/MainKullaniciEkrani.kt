package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityMainKullaniciEkraniBinding

class MainKullaniciEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainKullaniciEkraniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKullaniciEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kuaför butonu tıklama olayı
        binding.kuaforBtn.setOnClickListener {
            try {
                Log.d("MainKullaniciEkrani", "Kuaför listesi açılıyor")
                val intent = Intent(this@MainKullaniciEkrani, MainKuaforListesi::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainKullaniciEkrani", "Kuaför listesi açılırken hata: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Kuaför listesi açılırken bir hata oluştu", Toast.LENGTH_LONG).show()
            }
        }

        // Güzellik Merkezi butonu tıklama olayı
        binding.guzellikBtn.setOnClickListener {
            try {
                Log.d("MainKullaniciEkrani", "Güzellik merkezi listesi açılıyor")
                val intent = Intent(this@MainKullaniciEkrani, MainGuzellikMerkeziListesi::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainKullaniciEkrani", "Güzellik merkezi listesi açılırken hata: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Güzellik merkezi listesi açılırken bir hata oluştu", Toast.LENGTH_LONG).show()
            }
        }

        // Arama görünümü için listener
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainKullaniciEkrani, "Arama özelliği yakında eklenecek!", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}