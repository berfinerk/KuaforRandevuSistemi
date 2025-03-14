package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityMainGuzellikMerkeziDetayBinding

class MainGuzellikMerkeziDetay : AppCompatActivity() {
    private lateinit var binding: ActivityMainGuzellikMerkeziDetayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGuzellikMerkeziDetayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent'ten gelen verileri al
        val salonId = intent.getStringExtra("salonId")
        val salonName = intent.getStringExtra("salonName")
        val salonAddress = intent.getStringExtra("salonAddress")
        val salonPhone = intent.getStringExtra("salonPhone")
        val salonRating = intent.getStringExtra("salonRating")

        // Verileri görüntüle
        binding.textViewSalonName.text = salonName
        binding.textViewAddress.text = salonAddress
        binding.textViewPhone.text = salonPhone
        binding.ratingBar.rating = salonRating?.toFloatOrNull() ?: 0f

        // Randevu al butonuna tıklama
        binding.buttonMakeAppointment.setOnClickListener {
            val intent = Intent(this, MainRandevuAl::class.java)
            intent.putExtra("salon_id", salonId)
            intent.putExtra("salon_name", salonName)
            startActivity(intent)
        }
    }
} 