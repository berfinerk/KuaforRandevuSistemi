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
        setAppLocale()
        super.onCreate(savedInstanceState)
        binding = ActivityGuzellikMerkeziBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşletme adını ayarla
        binding.textViewIsletmeAdi.text = getString(R.string.beauty_center)

        // İşletme açıklamasını ayarla
        binding.textViewAciklama.text = getString(R.string.beauty_center_description)

        // Puanı ayarla
        binding.ratingBar.rating = 4.7f

        // Hizmetleri ekle
        val hizmetler = listOf(
            getString(R.string.skin_care) to "500 TL",
            getString(R.string.manicure) to "200 TL",
            getString(R.string.pedicure) to "250 TL",
            getString(R.string.eyebrow_design) to "150 TL",
            getString(R.string.makeup) to "400 TL"
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
            btnRandevuAl.text = getString(R.string.book_appointment)
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

    private fun setAppLocale() {
        val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
        val lang = sharedPref.getString("lang", "tr") ?: "tr"
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
} 