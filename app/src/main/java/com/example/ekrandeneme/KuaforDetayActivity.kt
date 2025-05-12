package com.example.ekrandeneme

import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityKuaforDetayBinding

class KuaforDetayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKuaforDetayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuaforDetayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kuaför bilgilerini al
        val kuaforAdi = intent.getStringExtra("kuaforAdi") ?: ""

        // Kuaför bilgilerini göster
        binding.textViewKuaforAdi.text = kuaforAdi
        binding.textViewAciklama.text = "Profesyonel hizmet anlayışıyla müşterilerimize en iyi hizmeti sunuyoruz."
        binding.ratingBar.rating = 4.5f

        // Her kuaför için özel fiyat listesi
        val hizmetler = when (kuaforAdi) {
            "Güzel Saçlar Kuaför - Atatürk Cad. No:123" -> listOf(
                "Saç Kesimi" to "150₺",
                "Saç Boyama" to "300₺",
                "Saç Şekillendirme (Fön, Maşa)" to "200₺",
                "Bölgesel Ağda" to "100₺",
                "Tüm Vücut Ağda" to "300₺",
                "Makyaj" to "250₺",
                "Manikür / Pedikür" to "200₺"
            )
            "Modern Kesim - İnönü Cad. No:45" -> listOf(
                "Saç Kesimi" to "180₺",
                "Saç Boyama" to "350₺",
                "Saç Şekillendirme (Fön, Maşa)" to "220₺",
                "Bölgesel Ağda" to "120₺",
                "Tüm Vücut Ağda" to "350₺",
                "Makyaj" to "280₺",
                "Manikür / Pedikür" to "220₺"
            )
            "Elit Kuaför - Cumhuriyet Cad. No:78" -> listOf(
                "Saç Kesimi" to "200₺",
                "Saç Boyama" to "400₺",
                "Saç Şekillendirme (Fön, Maşa)" to "250₺",
                "Bölgesel Ağda" to "150₺",
                "Tüm Vücut Ağda" to "400₺",
                "Makyaj" to "300₺",
                "Manikür / Pedikür" to "250₺"
            )
            "Saç Tasarım Merkezi - Bağdat Cad. No:90" -> listOf(
                "Saç Kesimi" to "170₺",
                "Saç Boyama" to "320₺",
                "Saç Şekillendirme (Fön, Maşa)" to "230₺",
                "Bölgesel Ağda" to "110₺",
                "Tüm Vücut Ağda" to "330₺",
                "Makyaj" to "270₺",
                "Manikür / Pedikür" to "230₺"
            )
            "Pro Kuaför - Bahçelievler Cad. No:34" -> listOf(
                "Saç Kesimi" to "160₺",
                "Saç Boyama" to "310₺",
                "Saç Şekillendirme (Fön, Maşa)" to "210₺",
                "Bölgesel Ağda" to "105₺",
                "Tüm Vücut Ağda" to "320₺",
                "Makyaj" to "260₺",
                "Manikür / Pedikür" to "210₺"
            )
            else -> listOf(
                "Saç Kesimi" to "150₺",
                "Saç Boyama" to "300₺",
                "Saç Şekillendirme (Fön, Maşa)" to "200₺",
                "Bölgesel Ağda" to "100₺",
                "Tüm Vücut Ağda" to "300₺",
                "Makyaj" to "250₺",
                "Manikür / Pedikür" to "200₺"
            )
        }

        // Hizmetleri tabloya ekle
        hizmetler.forEach { (hizmet, ucret) ->
            val row = TableRow(this).apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            }

            // Hizmet adı
            val hizmetTextView = TextView(this).apply {
                text = hizmet
                setPadding(8, 8, 8, 8)
                layoutParams = TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            // Ücret
            val ucretTextView = TextView(this).apply {
                text = ucret
                setPadding(8, 8, 8, 8)
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            }

            row.addView(hizmetTextView)
            row.addView(ucretTextView)
            binding.tableLayoutHizmetler.addView(row)
        }
    }
} 