package com.example.ekrandeneme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityGuzelSaclarKuaforBinding

class GuzelSaclarKuaforActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuzelSaclarKuaforBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuzelSaclarKuaforBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Güzel Saçlar Kuaför"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Profesyonel saç bakımı ve şekillendirme hizmetleri sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.8f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Saç Kesimi" to "170 TL",
            "Saç Boyama" to "410 TL",
            "Saç Bakımı" to "210 TL",
            "Fön" to "105 TL",
            "Saç Yıkama" to "52 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                setBackgroundColor(Color.WHITE)
                setPadding(0, 8, 0, 8)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    val intent = Intent(this@GuzelSaclarKuaforActivity, RandevuAlActivity::class.java)
                    intent.putExtra("isletmeAdi", "Güzel Saçlar Kuaför")
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