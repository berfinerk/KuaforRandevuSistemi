package com.example.ekrandeneme

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekrandeneme.adapter.AppointmentAdapter
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainIsletmeEkraniBinding
import com.example.ekrandeneme.databinding.ItemAppointmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout

class MainIsletmeEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainIsletmeEkraniBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appointmentAdapter: AppointmentAdapter
    private var selectedSalonId: Long? = null
    private var appointments: List<Map<String, Any>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainIsletmeEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", null)
        
        if (userEmail == null) {
            Toast.makeText(this, "Oturum açmanız gerekiyor", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupSalonTabs(userEmail)
        setupRecyclerView()
        setupStatusFilter()
    }

    private fun setupSalonTabs(userEmail: String) {
        Log.d("MainIsletmeEkrani", "Salonlar yükleniyor... Email: $userEmail")
        val salons = dbHelper.getSalonsByOwnerEmail(userEmail)
        
        Log.d("MainIsletmeEkrani", "Yüklenen salon sayısı: ${salons.size}")
        salons.forEach { salon ->
            Log.d("MainIsletmeEkrani", "Salon: ${salon["name"]}, ID: ${salon["id"]}, Tip: ${salon["type"]}")
        }
        
        if (salons.isEmpty()) {
            Toast.makeText(this, "Henüz kayıtlı salonunuz bulunmamaktadır", Toast.LENGTH_SHORT).show()
            return
        }

        salons.forEach { salon: Map<String, Any> ->
            val tab = binding.tabLayoutSalons.newTab().apply {
                text = salon["name"] as String
                tag = salon["id"] as Long
            }
            binding.tabLayoutSalons.addTab(tab)
        }

        binding.tabLayoutSalons.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedSalonId = tab?.tag as? Long
                Log.d("MainIsletmeEkrani", "Seçilen salon ID: $selectedSalonId")
                loadAppointments()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Select first salon by default
        selectedSalonId = salons.firstOrNull()?.get("id") as? Long
        loadAppointments()
    }

    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(
            appointments = emptyList(),
            onConfirmClick = { appointmentId -> updateAppointmentStatus(appointmentId, "CONFIRMED") },
            onCompleteClick = { appointmentId -> updateAppointmentStatus(appointmentId, "COMPLETED") },
            onCancelClick = { appointmentId -> updateAppointmentStatus(appointmentId, "CANCELLED") }
        )

        binding.recyclerViewAppointments.apply {
            layoutManager = LinearLayoutManager(this@MainIsletmeEkrani)
            adapter = appointmentAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupStatusFilter() {
        binding.chipGroupStatus.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipPending -> filterAppointments("PENDING")
                R.id.chipConfirmed -> filterAppointments("CONFIRMED")
                R.id.chipCompleted -> filterAppointments("COMPLETED")
                R.id.chipCancelled -> filterAppointments("CANCELLED")
                -1 -> showAllAppointments() // No chip selected
            }
        }
    }

    private fun loadAppointments() {
        selectedSalonId?.let { salonId ->
            try {
                Log.d("MainIsletmeEkrani", "Randevular yükleniyor... Salon ID: $salonId")
                appointments = dbHelper.getAppointmentsBySalonId(salonId.toString())
                Log.d("MainIsletmeEkrani", "Yüklenen randevu sayısı: ${appointments.size}")
                
                if (appointments.isEmpty()) {
                    // Randevu yoksa kullanıcıya bilgi ver
                    Toast.makeText(this, "Henüz randevu bulunmamaktadır", Toast.LENGTH_SHORT).show()
                }
                
                appointments.forEach { appointment ->
                    Log.d("MainIsletmeEkrani", "Randevu: " +
                            "ID=${appointment["appointment_id"]}, " +
                            "Müşteri=${appointment["customer_name"]}, " +
                            "Hizmet=${appointment["service_name"]}, " +
                            "Tarih=${appointment["date"]}, " +
                            "Saat=${appointment["time"]}, " +
                            "Durum=${appointment["status"]}")
                }
                
                runOnUiThread {
                    showAllAppointments()
                }
            } catch (e: Exception) {
                Log.e("MainIsletmeEkrani", "Randevular yüklenirken hata", e)
                runOnUiThread {
                    Toast.makeText(this, "Randevular yüklenirken hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } ?: run {
            Log.e("MainIsletmeEkrani", "Seçili salon ID null!")
            runOnUiThread {
                Toast.makeText(this, "Lütfen bir salon seçin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAllAppointments() {
        Log.d("MainIsletmeEkrani", "Tüm randevular gösteriliyor. Toplam: ${appointments.size}")
        appointmentAdapter.updateAppointments(appointments)
        binding.recyclerViewAppointments.scrollToPosition(0)
    }

    private fun filterAppointments(status: String) {
        val filteredAppointments = appointments.filter { it["status"] == status }
        Log.d("MainIsletmeEkrani", "$status durumundaki randevu sayısı: ${filteredAppointments.size}")
        appointmentAdapter.updateAppointments(filteredAppointments)
    }

    private fun updateAppointmentStatus(appointmentId: Long, newStatus: String) {
        try {
            val result = dbHelper.updateAppointmentStatus(appointmentId, newStatus)
            if (result > 0) {
                loadAppointments() // Refresh the list
                val message = when (newStatus) {
                    "CONFIRMED" -> "Randevu onaylandı"
                    "COMPLETED" -> "Randevu tamamlandı"
                    "CANCELLED" -> "Randevu iptal edildi"
                    else -> "Randevu durumu güncellendi"
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Randevu güncellenemedi", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MainIsletmeEkrani", "Error updating appointment status", e)
            Toast.makeText(this, "Randevu güncellenirken hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
} 