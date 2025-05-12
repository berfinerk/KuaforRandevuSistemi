package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityModernKesimBinding

class ModernKesimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModernKesimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModernKesimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Modern Kesim"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Modern ve şık kesimler için doğru adres."

        // Puanı ayarla
        binding.ratingBar.rating = 4.8f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "200 TL",
            "Saç Boyama" to "450 TL",
            "Saç Bakımı" to "250 TL",
            "Fön" to "120 TL",
            "Saç Yıkama" to "60 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@ModernKesimActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Modern Kesim")
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