package com.example.ekrandeneme

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainGuzellikMerkeziListesiBinding

class MainGuzellikMerkeziListesi : AppCompatActivity() {
    private lateinit var binding: ActivityMainGuzellikMerkeziListesiBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGuzellikMerkeziListesiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        // Salonları listele
        showSalons()
    }

    private fun showSalons() {
        try {
            // Salonları getir
            val salons = dbHelper.getSalonsByType("GUZELLIK_MERKEZI")

            // Salon listesi container'ını temizle
            binding.salonListContainer.removeAllViews()

            if (salons.isEmpty()) {
                // Salon bulunamadı mesajı göster
                val noSalonText = TextView(this).apply {
                    text = "Henüz güzellik merkezi bulunmamaktadır."
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