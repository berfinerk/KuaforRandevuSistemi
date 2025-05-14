package com.example.ekrandeneme

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.databinding.ActivityRandevuAlBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.ekrandeneme.database.DatabaseHelper

class RandevuAlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandevuAlBinding
    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: String = ""
    private var isletmeId: String = ""
    private var isletmeAdi: String = ""
    private var hizmetAdi: String = ""
    private var hizmetUcreti: String = ""
    private var selectedEmployeeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandevuAlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent'ten gelen verileri al
        isletmeId = intent.getStringExtra("isletmeId") ?: ""
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

        // Çalışanları doldur
        val dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        // Önce hizmetin id'sini bul
        val serviceCursor = dbHelper.database?.query(
            "services",
            arrayOf("id"),
            "salon_id = ? AND name = ?",
            arrayOf(isletmeId, hizmetAdi),
            null, null, null
        )
        var serviceId: String? = null
        serviceCursor?.use {
            if (it.moveToFirst()) {
                serviceId = it.getString(it.getColumnIndexOrThrow("id"))
            }
        }
        val calisanlar = mutableListOf<Pair<String, String>>() // id, ad-rol
        if (serviceId != null) {
            val cursor = dbHelper.database?.rawQuery(
                "SELECT e.id, e.name, e.role FROM employees e INNER JOIN service_employees se ON e.id = se.employee_id WHERE se.service_id = ?",
                arrayOf(serviceId)
            )
            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getString(0)
                    val ad = it.getString(1)
                    val rol = it.getString(2)
                    calisanlar.add(id to (ad + if (!rol.isNullOrBlank()) " - $rol" else ""))
                }
            }
        }
        dbHelper.close()
        val calisanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calisanlar.map { it.second })
        calisanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCalisan.adapter = calisanAdapter
        if (calisanlar.isNotEmpty()) {
            selectedEmployeeId = calisanlar[0].first
        }
        binding.spinnerCalisan.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedEmployeeId = calisanlar.getOrNull(position)?.first ?: ""
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                selectedEmployeeId = ""
            }
        }

        // Randevu Al butonu
        binding.buttonRandevuAl.setOnClickListener {
            if (selectedTime.isNotEmpty() && selectedEmployeeId.isNotEmpty()) {
                val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                // Kullanıcı emailini SharedPreferences'tan al
                val sharedPref = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE)
                val userEmail = sharedPref.getString("email", "") ?: ""
                val dbHelper = DatabaseHelper(this)
                dbHelper.openDatabase()
                // ÇAKIŞMA KONTROLÜ
                val slotTaken = dbHelper.isAppointmentSlotTaken(selectedEmployeeId, dateStr, selectedTime)
                if (slotTaken) {
                    Toast.makeText(this, "Bu çalışan bu tarih ve saatte zaten randevulu! Lütfen başka bir saat veya çalışan seçin.", Toast.LENGTH_LONG).show()
                    dbHelper.close()
                    return@setOnClickListener
                }
                val values = android.content.ContentValues().apply {
                    put("salon_id", isletmeId)
                    put("user_email", userEmail)
                    put("hizmet", hizmetAdi)
                    put("tarih", dateStr)
                    put("saat", selectedTime)
                    put("price", hizmetUcreti)
                    put("employee_id", selectedEmployeeId)
                    put("status", "PENDING")
                }
                val result = dbHelper.database?.insert("appointments", null, values) ?: -1
                dbHelper.close()
                if (result > 0) {
                    Toast.makeText(this, "Randevunuz oluşturuldu!\nTarih: $dateStr\nSaat: $selectedTime", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Randevu oluşturulamadı!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lütfen bir saat ve çalışan seçin", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 