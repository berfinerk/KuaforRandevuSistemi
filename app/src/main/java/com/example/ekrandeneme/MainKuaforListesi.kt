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
            "Güzel Saçlar Kuaför",
            "Modern Kesim Kuaför",
            "Elit Kuaför",
            "Saç Tasarım Merkezi",
            "Pro Kuaför"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, kuaforler)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedKuafor = kuaforler[position]
            val intent = when (selectedKuafor) {
                "Güzel Saçlar Kuaför" -> Intent(this, GuzelSaclarKuaforActivity::class.java)
                "Modern Kesim Kuaför" -> Intent(this, ModernKesimActivity::class.java)
                "Elit Kuaför" -> Intent(this, ElitKuaforActivity::class.java)
                "Saç Tasarım Merkezi" -> Intent(this, SacTasarimMerkeziActivity::class.java)
                "Pro Kuaför" -> Intent(this, ProKuaforActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }
    }
}