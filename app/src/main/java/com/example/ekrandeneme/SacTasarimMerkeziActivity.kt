package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivitySacTasarimMerkeziBinding

class SacTasarimMerkeziActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySacTasarimMerkeziBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySacTasarimMerkeziBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Saç Tasarım Merkezi"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Profesyonel saç tasarımı ve bakım hizmetleri sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.7f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "180 TL",
            "Saç Boyama" to "420 TL",
            "Saç Bakımı" to "220 TL",
            "Fön" to "110 TL",
            "Saç Yıkama" to "55 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@SacTasarimMerkeziActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Saç Tasarım Merkezi")
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