package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityElitGuzellikBinding

class ElitGuzellikActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElitGuzellikBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElitGuzellikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Elit Güzellik Merkezi"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Profesyonel ekibimizle cilt bakımı, makyaj ve daha birçok güzellik hizmeti sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.8f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Cilt Bakımı" to "350 TL",
            "Makyaj" to "250 TL",
            "Manikür/Pedikür" to "200 TL",
            "Kaş/Kirpik Laminasyonu" to "300 TL",
            "Kalıcı Makyaj" to "1500 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@ElitGuzellikActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Elit Güzellik Merkezi")
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