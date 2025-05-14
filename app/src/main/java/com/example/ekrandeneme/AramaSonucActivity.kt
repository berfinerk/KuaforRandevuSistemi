package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityAramaSonucBinding

class AramaSonucActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAramaSonucBinding
    private lateinit var adapter: SalonAdapter
    private var salonList: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAramaSonucBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SalonAdapter { salon ->
            val intent = Intent(this, IsletmeDetayActivity::class.java)
            intent.putExtra("isletmeId", salon["id"])
            startActivity(intent)
        }
        binding.recyclerViewAramaSonuc.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAramaSonuc.adapter = adapter

        // Eğer ana ekrandan bir arama sorgusu geldiyse onu göster
        val initialQuery = intent.getStringExtra("query") ?: ""
        if (initialQuery.isNotBlank()) {
            binding.searchView.setQuery(initialQuery, false)
            searchAndShowResults(initialQuery)
        }

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchAndShowResults(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    adapter.submitList(emptyList())
                } else {
                    searchAndShowResults(newText)
                }
                return true
            }
        })
    }

    private fun searchAndShowResults(query: String) {
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        salonList = dbHelper.searchSalonsByNameOrService(query)
        dbHelper.close()
        adapter.submitList(salonList)
    }

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