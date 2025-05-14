package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.ekrandeneme.database.DatabaseHelper

class MainGuzellikMerkeziListesi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_guzellik_merkezi_listesi)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listViewGuzellikMerkezleri)
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        val salonlar = dbHelper.getAllSalons()
        dbHelper.close()
        val guzellikMerkezleri = salonlar.filter { it["type"] == "GUZELLIK_MERKEZI" }
        val isimVeAdresListesi = guzellikMerkezleri.map { "${it["name"]} - ${it["address"]}" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, isimVeAdresListesi)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedMerkez = guzellikMerkezleri[position]
            val intent = Intent(this, IsletmeDetayActivity::class.java)
            intent.putExtra("isletmeId", selectedMerkez["id"])
            startActivity(intent)
        }
    }
}