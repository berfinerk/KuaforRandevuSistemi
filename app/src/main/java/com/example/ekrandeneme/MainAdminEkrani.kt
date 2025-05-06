package com.example.ekrandeneme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekrandeneme.database.DatabaseHelper
import com.example.ekrandeneme.databinding.ActivityMainAdminEkraniBinding

class MainAdminEkrani : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminEkraniBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanı yardımcısını başlat
        dbHelper = DatabaseHelper(this)
        dbHelper.openDatabase()

        setupClickListeners()
        loadDashboardData()
    }

    private fun setupClickListeners() {
        // Kullanıcı Yönetimi
        binding.cardUserManagement.setOnClickListener {
            // TODO: Kullanıcı yönetimi aktivitesini başlat
            Toast.makeText(this, "Kullanıcı Yönetimi yakında aktif olacak", Toast.LENGTH_SHORT).show()
        }

        // Randevu Yönetimi
        binding.cardAppointmentManagement.setOnClickListener {
            // TODO: Randevu yönetimi aktivitesini başlat
            Toast.makeText(this, "Randevu Yönetimi yakında aktif olacak", Toast.LENGTH_SHORT).show()
        }

        // İşletme Yönetimi
        binding.cardBusinessManagement.setOnClickListener {
            // TODO: İşletme yönetimi aktivitesini başlat
            Toast.makeText(this, "İşletme Yönetimi yakında aktif olacak", Toast.LENGTH_SHORT).show()
        }

        // İstatistikler
        binding.cardStatistics.setOnClickListener {
            // TODO: İstatistikler aktivitesini başlat
            Toast.makeText(this, "İstatistikler yakında aktif olacak", Toast.LENGTH_SHORT).show()
        }

        // Sistem Ayarları
        binding.cardSettings.setOnClickListener {
            // TODO: Sistem ayarları aktivitesini başlat
            Toast.makeText(this, "Sistem Ayarları yakında aktif olacak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDashboardData() {
        try {
            // Toplam kullanıcı sayısını al
            val totalUsers = dbHelper.getTotalUsers()
            
            // Toplam işletme sayısını al
            val totalBusinesses = dbHelper.getTotalBusinesses()
            
            // Toplam randevu sayısını al
            val totalAppointments = dbHelper.getTotalAppointments()
            
            // Günlük randevu sayısını al
            val dailyAppointments = dbHelper.getDailyAppointments()
            
            // İstatistikleri güncelle
            updateDashboardStats(totalUsers, totalBusinesses, totalAppointments, dailyAppointments)
            
        } catch (e: Exception) {
            Toast.makeText(this, "Veriler yüklenirken bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun updateDashboardStats(
        totalUsers: Int,
        totalBusinesses: Int,
        totalAppointments: Int,
        dailyAppointments: Int
    ) {
        // TODO: İstatistikleri göster
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
} 