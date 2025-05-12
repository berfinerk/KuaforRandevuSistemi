package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityModernGuzellikBinding

class ModernGuzellikActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModernGuzellikBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModernGuzellikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Modern Güzellik Merkezi"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Modern teknikler ve son teknoloji cihazlarla profesyonel güzellik hizmetleri sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.7f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Cilt Bakımı" to "320 TL",
            "Makyaj" to "230 TL",
            "Manikür/Pedikür" to "180 TL",
            "Kaş/Kirpik Laminasyonu" to "280 TL",
            "Kalıcı Makyaj" to "1400 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@ModernGuzellikActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Modern Güzellik Merkezi")
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