package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityGuzellikMerkezi3Binding

class GuzellikMerkezi3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityGuzellikMerkezi3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuzellikMerkezi3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Güzellik Merkezi 3"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Uzman kadromuzla profesyonel güzellik ve bakım hizmetleri sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.5f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Cilt Bakımı" to "240 TL",
            "Makyaj" to "180 TL",
            "Manikür/Pedikür" to "130 TL",
            "Kaş/Kirpik Laminasyonu" to "230 TL",
            "Kalıcı Makyaj" to "1100 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@GuzellikMerkezi3Activity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Güzellik Merkezi 3")
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