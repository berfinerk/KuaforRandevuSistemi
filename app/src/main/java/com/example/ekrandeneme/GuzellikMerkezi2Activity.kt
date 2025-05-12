package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityGuzellikMerkezi2Binding

class GuzellikMerkezi2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityGuzellikMerkezi2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuzellikMerkezi2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Güzellik Merkezi 2"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Modern teknikler ve uzman kadromuzla güzellik hizmetleri sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.6f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Cilt Bakımı" to "260 TL",
            "Makyaj" to "190 TL",
            "Manikür/Pedikür" to "140 TL",
            "Kaş/Kirpik Laminasyonu" to "240 TL",
            "Kalıcı Makyaj" to "1150 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@GuzellikMerkezi2Activity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Güzellik Merkezi 2")
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