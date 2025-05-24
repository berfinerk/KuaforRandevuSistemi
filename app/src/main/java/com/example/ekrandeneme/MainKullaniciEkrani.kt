package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ekrandeneme.databinding.ActivityMainBinding
import com.example.ekrandeneme.databinding.ActivityMainKayitOlBinding
import com.example.ekrandeneme.databinding.ActivityMainKullaniciEkraniBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MainKullaniciEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainKullaniciEkraniBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalonAdapter
    private var salonList: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale()
        super.onCreate(savedInstanceState)
        binding = ActivityMainKullaniciEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView ve Adapter
        recyclerView = binding.recyclerViewAramaSonuc
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SalonAdapter { salon ->
            val intent = Intent(this, IsletmeDetayActivity::class.java)
            intent.putExtra("isletmeId", salon["id"])
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Arama kutusuna tıklanınca yeni ekrana geç
        binding.searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, AramaSonucActivity::class.java)
                startActivity(intent)
                binding.searchView.clearFocus()
            }
        }
        binding.searchView.setOnClickListener {
            val intent = Intent(this, AramaSonucActivity::class.java)
            startActivity(intent)
        }
        // Arama kutusunu pasif yap (sadece tıklama için)
        binding.searchView.isFocusable = false
        binding.searchView.isIconified = true

        binding.kuaforBtn.setOnClickListener{
            intent= Intent(this,MainKuaforListesi::class.java)
            startActivity(intent)
        }
        binding.guzellikBtn.setOnClickListener{
            intent= Intent(this,MainGuzellikMerkeziListesi::class.java)
            startActivity(intent)
        }

        // Çıkış yap butonu ekle
        binding.btnCikis.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.btnGecmisRandevular.setOnClickListener {
            val intent = Intent(this, GecmisRandevularActivity::class.java)
            startActivity(intent)
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

    private fun searchAndShowResults(query: String) {
        val dbHelper = com.example.ekrandeneme.database.DatabaseHelper(this)
        dbHelper.openDatabase()
        salonList = dbHelper.searchSalonsByNameOrService(query)
        dbHelper.close()
        adapter.submitList(salonList)
        recyclerView.visibility = if (salonList.isEmpty()) View.GONE else View.VISIBLE
    }

    // Basit bir RecyclerView Adapter
    class SalonAdapter(val onClick: (Map<String, String>) -> Unit) : RecyclerView.Adapter<SalonAdapter.SalonViewHolder>() {
        private var items: List<Map<String, String>> = emptyList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalonViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return SalonViewHolder(view)
        }
        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: SalonViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
            holder.itemView.setOnClickListener { onClick(item) }
        }
        fun submitList(list: List<Map<String, String>>) {
            items = list
            notifyDataSetChanged()
        }
        class SalonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Map<String, String>) {
                val text1 = itemView.findViewById<TextView>(android.R.id.text1)
                val text2 = itemView.findViewById<TextView>(android.R.id.text2)
                text1.text = item["name"] ?: "Salon"
                // Ortalama puanı çek
                val context = itemView.context
                val dbHelper = com.example.ekrandeneme.database.DatabaseHelper(context)
                dbHelper.openDatabase()
                val avg = dbHelper.getSalonAverageRating(item["id"] ?: "")
                dbHelper.close()
                val yildiz = if (avg > 0f) {
                    val full = avg.toInt()
                    val half = if (avg - full >= 0.5f) 1 else 0
                    "★".repeat(full) + (if (half == 1) "½" else "") + "☆".repeat(5 - full - half)
                } else {
                    "Henüz puan yok"
                }
                text2.text = (item["address"] ?: "") + (if (!item["type"].isNullOrBlank()) " - ${item["type"]}" else "") + "\nPuan: $yildiz"
            }
        }
    }
}