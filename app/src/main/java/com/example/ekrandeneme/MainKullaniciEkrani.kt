package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
=======
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainBinding
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
import com.example.ekrandeneme.databinding.ActivityMainKullaniciEkraniBinding

class MainKullaniciEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainKullaniciEkraniBinding
    private lateinit var dbHelper: DatabaseHelper

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

<<<<<<< HEAD
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
=======
        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        // Kuaför butonu tıklama olayı
        binding.kuaforBtn.setOnClickListener {
            startActivity(Intent(this, MainKuaforListesi::class.java))
        }

        // Güzellik Merkezi butonu tıklama olayı
        binding.guzellikBtn.setOnClickListener {
            startActivity(Intent(this, MainGuzellikMerkeziListesi::class.java))
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
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

    private fun showSalons(salonType: String) {
        try {
            // Salonları getir
            val salons = dbHelper.getSalonsByType(salonType)

            // Salon listesi container'ını temizle
            binding.salonListContainer.removeAllViews()

            if (salons.isEmpty()) {
                // Salon bulunamadı mesajı göster
                val noSalonText = TextView(this).apply {
                    text = "Bu kategoride henüz salon bulunmamaktadır."
                    textSize = 16f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(16, 16, 16, 16)
                }
                binding.salonListContainer.addView(noSalonText)
                return
            }

            // Her salon için bir kart oluştur
            salons.forEach { salon ->
                val salonCard = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(16, 16, 16, 16)
                    setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 16)
                    }
                }

                // Salon adı
                val nameText = TextView(this).apply {
                    text = salon["name"]
                    textSize = 18f
                    setTextColor(android.graphics.Color.BLACK)
                    setTypeface(null, android.graphics.Typeface.BOLD)
                }
                salonCard.addView(nameText)

                // Salon adresi
                val addressText = TextView(this).apply {
                    text = salon["address"]
                    textSize = 14f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                salonCard.addView(addressText)

                // Salon telefonu
                val phoneText = TextView(this).apply {
                    text = salon["phone"]
                    textSize = 14f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                salonCard.addView(phoneText)

                // Salon puanı
                val ratingText = TextView(this).apply {
                    text = "Puan: ${salon["rating"]}"
                    textSize = 14f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                salonCard.addView(ratingText)

                // Kartı tıklanabilir yap
                salonCard.setOnClickListener {
                    // TODO: Salon detay sayfasına yönlendir
                    Toast.makeText(this, "${salon["name"]} salonuna tıklandı", Toast.LENGTH_SHORT).show()
                }

                // Kartı listeye ekle
                binding.salonListContainer.addView(salonCard)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Salonlar listelenirken bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}