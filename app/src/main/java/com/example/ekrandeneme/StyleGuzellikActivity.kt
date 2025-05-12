package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityStyleGuzellikBinding

class StyleGuzellikActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStyleGuzellikBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStyleGuzellikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Style Güzellik Merkezi"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Trend güzellik uygulamaları ve özel bakım hizmetleri ile sizlere özel çözümler sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.6f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Cilt Bakımı" to "300 TL",
            "Makyaj" to "220 TL",
            "Manikür/Pedikür" to "170 TL",
            "Kaş/Kirpik Laminasyonu" to "260 TL",
            "Kalıcı Makyaj" to "1300 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@StyleGuzellikActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Style Güzellik Merkezi")
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