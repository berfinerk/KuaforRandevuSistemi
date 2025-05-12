package com.example.ekrandeneme

<<<<<<< HEAD
=======
import android.content.Intent
>>>>>>> 4690fb7 (salonlar listelendi)
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
<<<<<<< HEAD
import com.example.ekrandeneme.databinding.ActivityMainGuzellikMerkeziListesiBinding
import com.example.ekrandeneme.databinding.ActivityMainKuaforListesiBinding

class MainGuzellikMerkeziListesi : AppCompatActivity() {
    private lateinit var binding: ActivityMainGuzellikMerkeziListesiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGuzellikMerkeziListesiBinding.inflate(layoutInflater)
        setContentView(binding.root)
=======
import android.widget.ArrayAdapter
import android.widget.ListView

class MainGuzellikMerkeziListesi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_guzellik_merkezi_listesi)
>>>>>>> 4690fb7 (salonlar listelendi)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
<<<<<<< HEAD
=======

        val listView = findViewById<ListView>(R.id.listViewGuzellikMerkezleri)
        val guzellikMerkezleri = arrayOf(
            "Güzellik Merkezi 1",
            "Güzellik Merkezi 2",
            "Güzellik Merkezi 3"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, guzellikMerkezleri)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedMerkez = guzellikMerkezleri[position]
            val intent = when (selectedMerkez) {
                "Güzellik Merkezi 1" -> Intent(this, GuzellikMerkezi1Activity::class.java)
                "Güzellik Merkezi 2" -> Intent(this, GuzellikMerkezi2Activity::class.java)
                "Güzellik Merkezi 3" -> Intent(this, GuzellikMerkezi3Activity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }
>>>>>>> 4690fb7 (salonlar listelendi)
    }
}