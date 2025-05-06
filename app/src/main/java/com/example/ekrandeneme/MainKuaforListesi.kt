package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekrandeneme.adapters.SalonAdapter
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainKuaforListesiBinding
import com.google.android.material.chip.Chip

class MainKuaforListesi : AppCompatActivity() {
    private lateinit var binding: ActivityMainKuaforListesiBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var salonAdapter: SalonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKuaforListesiBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}