package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityRenkVeKesimBinding

class RenkVeKesimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRenkVeKesimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenkVeKesimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Renk ve Kesim Kuaför"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Modern kesim teknikleri ve renk uygulamaları ile sizlere özel hizmet sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.6f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "160 TL",
            "Saç Boyama" to "400 TL",
            "Saç Bakımı" to "200 TL",
            "Fön" to "100 TL",
            "Saç Yıkama" to "50 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@RenkVeKesimActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Renk ve Kesim Kuaför")
                    intent.putExtra("hizmetAdi", hizmet)
                    intent.putExtra("hizmetUcreti", ucret)
                    startActivity(intent)
                }
            }
            
            val hizmetTextView = TextView(this).apply {
                text = hizmet
                setPadding(16, 8, 16, 8)
                textSize = 16f
            }
            
            val ucretTextView = TextView(this).apply {
                text = ucret
                setPadding(16, 8, 16, 8)
                textSize = 16f
            }
            
            row.addView(hizmetTextView)
            row.addView(ucretTextView)
            
            binding.tableLayoutHizmetler.addView(row)
        }
    }
} 