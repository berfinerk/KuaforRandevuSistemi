package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityProKuaforBinding

class ProKuaforActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProKuaforBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProKuaforBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Pro Kuaför"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Profesyonel ekibimizle en son trend saç stillerini uyguluyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.9f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "190 TL",
            "Saç Boyama" to "450 TL",
            "Saç Bakımı" to "230 TL",
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
                    val intent = Intent(this@ProKuaforActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Pro Kuaför")
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