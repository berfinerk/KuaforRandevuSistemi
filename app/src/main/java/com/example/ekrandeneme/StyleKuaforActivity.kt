package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityStyleKuaforBinding

class StyleKuaforActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStyleKuaforBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStyleKuaforBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Style Kuaför"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Trend saç stilleri ve modern kesim teknikleri ile sizlere özel hizmet sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.5f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "150 TL",
            "Saç Boyama" to "380 TL",
            "Saç Bakımı" to "190 TL",
            "Fön" to "90 TL",
            "Saç Yıkama" to "45 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@StyleKuaforActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Style Kuaför")
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