package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityGuzellikMerkeziBinding

class GuzellikMerkeziActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuzellikMerkeziBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuzellikMerkeziBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = "Güzellik Merkezi"

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = "Profesyonel ekibimizle cilt bakımı, manikür, pedikür ve daha birçok hizmet sunuyoruz."

        // Puanı ayarla
        binding.ratingBar.rating = 4.7f

        // Hizmetleri ekle
        val hizmetler = listOf(
            "Cilt Bakımı" to "500 TL",
            "Manikür" to "200 TL",
            "Pedikür" to "250 TL",
            "Kaş Tasarımı" to "150 TL",
            "Makyaj" to "400 TL"
        )

        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this)
            
            val hizmetTextView = TextView(this)
            hizmetTextView.text = hizmet
            hizmetTextView.setPadding(16, 8, 16, 8)
            
            val ucretTextView = TextView(this)
            ucretTextView.text = ucret
            ucretTextView.setPadding(16, 8, 16, 8)
            
            val btnRandevuAl = Button(this)
            btnRandevuAl.text = "Randevu Al"
            btnRandevuAl.setOnClickListener {
                val intent = Intent(this, RandevuAlActivity::class.java)
                intent.putExtra("isletmeAdi", "Güzellik Merkezi")
                intent.putExtra("hizmetAdi", hizmet)
                intent.putExtra("hizmetUcreti", ucret)
                startActivity(intent)
            }
            
            row.addView(hizmetTextView)
            row.addView(ucretTextView)
            row.addView(btnRandevuAl)
            
            binding.tableLayoutHizmetler.addView(row)
        }
    }
} 