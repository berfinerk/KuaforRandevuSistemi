package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.ListView

class MainKuaforListesi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kuafor_listesi)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listViewKuaforler)
        val kuaforler = arrayOf(
            "Güzel Saçlar Kuaför - Atatürk Cad. No:123",
            "Modern Kesim - İnönü Cad. No:45",
            "Elit Kuaför - Cumhuriyet Cad. No:78",
            "Saç Tasarım Merkezi - Bağdat Cad. No:90",
            "Pro Kuaför - Bahçelievler Cad. No:34"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, kuaforler)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedKuafor = kuaforler[position]
            val intent = Intent(this, IsletmeDetayActivity::class.java)
            intent.putExtra("isletmeId", selectedKuafor["id"])
            startActivity(intent)
        }
    }
}