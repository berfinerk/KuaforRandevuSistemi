package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityElitKuaforBinding

class ElitKuaforActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElitKuaforBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElitKuaforBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Elit Kuaför"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Lüks ve kaliteli hizmet anlayışıyla sizlere hizmet veriyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.9f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "250 TL",
            "Saç Boyama" to "500 TL",
            "Saç Bakımı" to "300 TL",
            "Fön" to "150 TL",
            "Saç Yıkama" to "80 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@ElitKuaforActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Elit Kuaför")
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