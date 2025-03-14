package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekrandeneme.adapters.SalonAdapter
=======
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainKuaforListesiBinding
import com.google.android.material.chip.Chip

class MainKuaforListesi : AppCompatActivity() {
    private lateinit var binding: ActivityMainKuaforListesiBinding
    private lateinit var dbHelper: DatabaseHelper
<<<<<<< HEAD
    private lateinit var salonAdapter: SalonAdapter
=======
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKuaforListesiBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase() // Veritabanını açıyoruz
        
        setupRecyclerView()
        setupSearchView()
        setupFilterChips()
        loadSalons()
    }

    private fun setupRecyclerView() {
        salonAdapter = SalonAdapter()
        binding.recyclerViewSalons.apply {
            layoutManager = LinearLayoutManager(this@MainKuaforListesi)
            adapter = salonAdapter
        }

        salonAdapter.setOnItemClickListener { salon ->
            val intent = Intent(this, MainKuaforDetay::class.java).apply {
                putExtra("salonId", salon["id"])
                putExtra("salonName", salon["name"])
                putExtra("salonAddress", salon["address"])
                putExtra("salonPhone", salon["phone"])
                putExtra("salonRating", salon["rating"])
            }
            startActivity(intent)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                salonAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setupFilterChips() {
        binding.chipPuanYuksek.setOnClickListener {
            salonAdapter.sortByRating(false)
            resetChipsExcept(binding.chipPuanYuksek)
        }

        binding.chipPuanDusuk.setOnClickListener {
            salonAdapter.sortByRating(true)
            resetChipsExcept(binding.chipPuanDusuk)
        }

        binding.chipYeniEklenen.setOnClickListener {
            salonAdapter.sortByDate()
            resetChipsExcept(binding.chipYeniEklenen)
        }
    }

    private fun resetChipsExcept(selectedChip: Chip) {
        val chips = listOf(binding.chipPuanYuksek, binding.chipPuanDusuk, binding.chipYeniEklenen)
        chips.forEach { chip ->
            chip.isChecked = chip == selectedChip
        }
    }

    private fun loadSalons() {
        val salons = dbHelper.getSalonsByType("KUAFOR")
        Log.d("MainKuaforListesi", "Yüklenen kuaför sayısı: ${salons.size}")
        salons.forEach { salon ->
            Log.d("MainKuaforListesi", "Kuaför: ${salon["name"]}, Adres: ${salon["address"]}")
        }
        salonAdapter.setData(salons)
    }

=======
        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        // Salonları listele
        showSalons()
    }

    private fun showSalons() {
        try {
            // Salonları getir
            val salons = dbHelper.getSalonsByType("KUAFOR")

            // Salon listesi container'ını temizle
            binding.salonListContainer.removeAllViews()

            if (salons.isEmpty()) {
                // Salon bulunamadı mesajı göster
                val noSalonText = TextView(this).apply {
                    text = "Henüz kuaför salonu bulunmamaktadır."
                    textSize = 16f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(16, 16, 16, 16)
                }
                binding.salonListContainer.addView(noSalonText)
                return
            }

            // Her salon için bir kart oluştur
            salons.forEach { salon ->
                val salonCard = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(16, 16, 16, 16)
                    setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 16)
                    }
                }

                // Salon adı
                val nameText = TextView(this).apply {
                    text = salon["name"]
                    textSize = 18f
                    setTextColor(android.graphics.Color.BLACK)
                    setTypeface(null, android.graphics.Typeface.BOLD)
                }
                salonCard.addView(nameText)

                // Salon adresi
                val addressText = TextView(this).apply {
                    text = salon["address"]
                    textSize = 14f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                salonCard.addView(addressText)

                // Salon telefonu
                val phoneText = TextView(this).apply {
                    text = salon["phone"]
                    textSize = 14f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                salonCard.addView(phoneText)

                // Salon puanı
                val ratingText = TextView(this).apply {
                    text = "Puan: ${salon["rating"]}"
                    textSize = 14f
                    setTextColor(android.graphics.Color.BLACK)
                    setPadding(0, 4, 0, 4)
                }
                salonCard.addView(ratingText)

                // Kartı tıklanabilir yap
                salonCard.setOnClickListener {
                    // TODO: Salon detay sayfasına yönlendir
                    Toast.makeText(this, "${salon["name"]} salonuna tıklandı", Toast.LENGTH_SHORT).show()
                }

                // Kartı listeye ekle
                binding.salonListContainer.addView(salonCard)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Salonlar listelenirken bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}