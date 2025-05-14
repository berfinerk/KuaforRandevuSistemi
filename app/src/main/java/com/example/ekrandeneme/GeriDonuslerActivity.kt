package com.example.ekrandeneme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ekrandeneme.database.DatabaseHelper

class GeriDonuslerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        setContentView(recyclerView)

        val isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salon = dbHelper.getAllSalons().find { it["name"] == isletmeAdi }
        val salonId = salon?.get("id") ?: ""
        val calisanlar = dbHelper.getEmployeesForSalon(salonId)
        dbHelper.close()
        recyclerView.adapter = CalisanAdapter(calisanlar, this)
    }

    class CalisanAdapter(val calisanlar: List<Map<String, String>>, val context: AppCompatActivity) : RecyclerView.Adapter<CalisanAdapter.CalisanViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalisanViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return CalisanViewHolder(view)
        }
        override fun getItemCount(): Int = calisanlar.size
        override fun onBindViewHolder(holder: CalisanViewHolder, position: Int) {
            val calisan = calisanlar[position]
            val ad = calisan["name"] ?: "?"
            val role = calisan["role"] ?: ""
            val empId = calisan["id"] ?: return
            val dbHelper = DatabaseHelper(context)
            dbHelper.openDatabase()
            val avg = dbHelper.getEmployeeAverageRating(empId)
            val detayliYorumlar = dbHelper.getEmployeeDetailedComments(empId)
            dbHelper.close()
            val text1 = holder.itemView.findViewById<TextView>(android.R.id.text1)
            text1.text = "$ad${if (role.isNotBlank()) " - $role" else ""}  |  Ortalama: ${"%.1f".format(avg)} ★"
            val text2 = holder.itemView.findViewById<TextView>(android.R.id.text2)
            if (detayliYorumlar.isEmpty()) {
                text2.text = "Henüz yorum yok."
            } else {
                text2.text = detayliYorumlar.joinToString("\n\n") { yorumMap ->
                    val puan = yorumMap["rating"]?.toIntOrNull() ?: 0
                    val yorum = yorumMap["comment"] ?: ""
                    val hizmet = yorumMap["hizmet"] ?: "?"
                    val tarih = yorumMap["tarih"] ?: "?"
                    val saat = yorumMap["saat"] ?: "?"
                    "${"★".repeat(puan)}${if (puan < 5) "☆".repeat(5-puan) else ""}  $yorum\nHizmet: $hizmet\nTarih: $tarih  Saat: $saat"
                }
            }
        }
        class CalisanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
} 