package com.example.ekrandeneme

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainRandevuAlBinding
import java.text.SimpleDateFormat
import java.util.*

class MainRandevuAl : AppCompatActivity() {
    private lateinit var binding: ActivityMainRandevuAlBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedService: Map<String, Any>? = null
    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private var salonId: String? = null
    private var services: List<Map<String, Any>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRandevuAlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE)

        // Salon bilgilerini al
        salonId = intent.getStringExtra("salon_id")
        val salonName = intent.getStringExtra("salon_name")
        
        Log.d("MainRandevuAl", "Gelen salon_id: $salonId")
        Log.d("MainRandevuAl", "Gelen salon_name: $salonName")
        
        binding.textViewSalonName.text = salonName

        // Hizmetleri yükle
        loadServices()

        // Spinner için adapter oluştur
        setupSpinner()

        // Tarih seçimi
        binding.buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        // Saat seçimi
        binding.buttonSelectTime.setOnClickListener {
            showTimePicker()
        }

        // Randevu onaylama
        binding.buttonConfirm.setOnClickListener {
            confirmAppointment()
        }
    }

    private fun loadServices() {
        salonId?.let { id ->
            Log.d("MainRandevuAl", "Hizmetler yükleniyor... Salon ID: $id")
            try {
                services = dbHelper.getServicesBySalonId(id)
                Log.d("MainRandevuAl", "Yüklenen hizmet sayısı: ${services.size}")
                services.forEach { service ->
                    Log.d("MainRandevuAl", "Hizmet: ${service["name"]}, Fiyat: ${service["price"]}, Süre: ${service["duration"]}")
                }
                if (services.isNotEmpty()) {
                    setupSpinner()
                } else {
                    Log.e("MainRandevuAl", "Hizmet listesi boş!")
                    Toast.makeText(this, "Bu salon için hizmet bulunamadı", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainRandevuAl", "Hizmetler yüklenirken hata: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Hizmetler yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Log.e("MainRandevuAl", "Salon ID null!")
            Toast.makeText(this, "Salon bilgisi alınamadı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
        if (services.isEmpty()) {
            Log.e("MainRandevuAl", "Hizmet listesi boş!")
            Toast.makeText(this, "Bu salon için hizmet bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        val serviceNames = services.map { it["name"] as String }
        Log.d("MainRandevuAl", "Spinner'a eklenecek hizmetler: $serviceNames")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerServices.adapter = adapter

        binding.spinnerServices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedService = services[position]
                updateServiceDetails()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Bir şey seçilmediğinde yapılacak işlem yok
            }
        }
    }

    private fun updateServiceDetails() {
        selectedService?.let { service ->
            val price = service["price"] as Double
            val duration = service["duration"] as Int
            val details = "Fiyat: ${price}₺\nSüre: ${duration} dakika"
            binding.textViewServiceDetails.text = details
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            binding.textViewSelectedDate.text = "Seçilen Tarih: $selectedDate"
        }, year, month, day).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
            show()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.textViewSelectedTime.text = "Seçilen Saat: $selectedTime"
        }, hour, minute, true).show()
    }

    private fun confirmAppointment() {
        try {
            // Seçili hizmet kontrolü
            val selectedService = selectedService
            if (selectedService == null) {
                Toast.makeText(this, "Lütfen bir hizmet seçin", Toast.LENGTH_SHORT).show()
                Log.e("MainRandevuAl", "Hizmet seçilmedi")
                return
            }

            // Tarih ve saat kontrolü
            if (selectedDate.isNullOrEmpty() || selectedTime.isNullOrEmpty()) {
                Toast.makeText(this, "Lütfen tarih ve saat seçin", Toast.LENGTH_SHORT).show()
                Log.e("MainRandevuAl", "Tarih veya saat seçilmedi")
                return
            }

            // Kullanıcı oturum kontrolü
            val userEmail = sharedPreferences.getString("email", null)
            
            if (userEmail == null) {
                Toast.makeText(this, "Oturum açmanız gerekiyor", Toast.LENGTH_SHORT).show()
                Log.e("MainRandevuAl", "Kullanıcı oturumu bulunamadı")
                return
            }

            Log.d("MainRandevuAl", "Randevu oluşturuluyor:")
            Log.d("MainRandevuAl", "Kullanıcı Email: $userEmail")
            Log.d("MainRandevuAl", "Seçili Hizmet ID: ${selectedService["id"]}")
            Log.d("MainRandevuAl", "Seçili Tarih: $selectedDate")
            Log.d("MainRandevuAl", "Seçili Saat: $selectedTime")

            // Kullanıcı ID'sini al
            val userId = dbHelper.getUserIdByEmail(userEmail)
            if (userId == null) {
                Toast.makeText(this, "Kullanıcı bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
                Log.e("MainRandevuAl", "Kullanıcı ID bulunamadı. Email: $userEmail")
                return
            }

            Log.d("MainRandevuAl", "Kullanıcı ID: $userId")

            // Randevuyu ekle
            val appointmentId = dbHelper.addAppointment(userId, selectedService["id"] as Long, selectedDate!!, selectedTime!!)
            
            if (appointmentId > 0) {
                Log.d("MainRandevuAl", "Randevu başarıyla eklendi. ID: $appointmentId")
                Toast.makeText(this, "Randevunuz başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.e("MainRandevuAl", "Randevu eklenemedi")
                Toast.makeText(this, "Randevu oluşturulurken bir hata oluştu", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MainRandevuAl", "Randevu oluşturulurken hata: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}