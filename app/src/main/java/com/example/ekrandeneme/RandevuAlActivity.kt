package com.example.ekrandeneme

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityRandevuAlBinding
import java.text.SimpleDateFormat
import java.util.*

class RandevuAlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandevuAlBinding
    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: String = ""
    private var isletmeAdi: String = ""
    private var hizmetAdi: String = ""
    private var hizmetUcreti: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandevuAlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent'ten gelen verileri al
        isletmeAdi = intent.getStringExtra("isletmeAdi") ?: ""
        hizmetAdi = intent.getStringExtra("hizmetAdi") ?: ""
        hizmetUcreti = intent.getStringExtra("hizmetUcreti") ?: ""

        // Hizmet adını göster
        binding.textViewHizmetAdi.text = "$hizmetAdi - $hizmetUcreti"

        // Tarih seçici
        binding.datePicker.setOnDateChangedListener { _, year, month, day ->
            selectedDate.set(year, month, day)
        }

        // Saat seçici
        val saatler = arrayOf("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, saatler)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSaat.adapter = adapter

        binding.spinnerSaat.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedTime = saatler[position]
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                selectedTime = ""
            }
        }

        // Randevu Al butonu
        binding.buttonRandevuAl.setOnClickListener {
            if (selectedTime.isNotEmpty()) {
                val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                Toast.makeText(this, "Randevunuz oluşturuldu!\nTarih: $dateStr\nSaat: $selectedTime", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Lütfen bir saat seçin", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 