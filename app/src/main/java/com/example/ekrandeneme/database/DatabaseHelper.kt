package com.example.ekrandeneme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
<<<<<<< HEAD
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
=======
import java.util.*
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var database: SQLiteDatabase? = null

    companion object {
        private const val DATABASE_NAME = "KuaforRandevuDB"
        private const val DATABASE_VERSION = 1

        // Tablo isimleri
        private const val TABLE_USERS = "users"
        private const val TABLE_SALONS = "salons"
        private const val TABLE_SERVICES = "services"
        private const val TABLE_APPOINTMENTS = "appointments"
<<<<<<< HEAD
=======
        private const val TABLE_RATINGS = "ratings"
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd

        // Ortak sütunlar
        private const val COLUMN_ID = "id"
        private const val COLUMN_CREATED_AT = "created_at"

        // Users tablosu sütunları
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_USER_TYPE = "user_type"

        // Salons tablosu sütunları
        private const val COLUMN_SALON_NAME = "salon_name"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_SALON_TYPE = "salon_type"
        private const val COLUMN_OWNER_ID = "owner_id"
<<<<<<< HEAD
=======

        // Services tablosu sütunları
        private const val COLUMN_SERVICE_NAME = "service_name"
        private const val COLUMN_SERVICE_DURATION = "service_duration"
        private const val COLUMN_SERVICE_PRICE = "service_price"
        private const val COLUMN_SALON_ID = "salon_id"

        // Appointments tablosu sütunları
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_SERVICE_ID = "service_id"
        private const val COLUMN_APPOINTMENT_DATE = "appointment_date"
        private const val COLUMN_APPOINTMENT_TIME = "appointment_time"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_EXPIRY_TIME = "expiry_time"
    }
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd

        // Services tablosu sütunları
        private const val COLUMN_SERVICE_NAME = "service_name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DURATION = "duration"
        private const val COLUMN_SALON_ID = "salon_id"

        // Appointments tablosu sütunları
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_SERVICE_ID = "service_id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Users table
        val createUsersTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_USER_TYPE TEXT NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        // Salons table
        val createSalonsTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_SALONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SALON_NAME TEXT NOT NULL,
                $COLUMN_ADDRESS TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL,
                $COLUMN_RATING REAL DEFAULT 0,
                $COLUMN_SALON_TYPE TEXT NOT NULL,
                $COLUMN_OWNER_ID INTEGER NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
<<<<<<< HEAD
                FOREIGN KEY ($COLUMN_OWNER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)
            )
        """.trimIndent()

        // Services table
=======
                FOREIGN KEY($COLUMN_OWNER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)
            )
        """.trimIndent()

        // Services tablosu
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
        val createServicesTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_SERVICES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SERVICE_NAME TEXT NOT NULL,
<<<<<<< HEAD
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_DURATION INTEGER NOT NULL,
                $COLUMN_SALON_ID INTEGER NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY ($COLUMN_SALON_ID) REFERENCES $TABLE_SALONS($COLUMN_ID)
            )
        """.trimIndent()

        // Appointments table
=======
                $COLUMN_SERVICE_DURATION INTEGER NOT NULL,
                $COLUMN_SERVICE_PRICE REAL NOT NULL,
                $COLUMN_SALON_ID INTEGER NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY($COLUMN_SALON_ID) REFERENCES $TABLE_SALONS($COLUMN_ID)
            )
        """.trimIndent()

        // Appointments tablosu
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
        val createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_APPOINTMENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_SERVICE_ID INTEGER NOT NULL,
<<<<<<< HEAD
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_TIME TEXT NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID),
                FOREIGN KEY ($COLUMN_SERVICE_ID) REFERENCES $TABLE_SERVICES($COLUMN_ID)
=======
                $COLUMN_APPOINTMENT_DATE DATE NOT NULL,
                $COLUMN_APPOINTMENT_TIME TIME NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL,
                $COLUMN_EXPIRY_TIME DATETIME NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID),
                FOREIGN KEY($COLUMN_SERVICE_ID) REFERENCES $TABLE_SERVICES($COLUMN_ID)
            )
        """.trimIndent()

        // Ratings tablosu
        val createRatingsTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_RATINGS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_SALON_ID INTEGER NOT NULL,
                $COLUMN_RATING REAL NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID),
                FOREIGN KEY($COLUMN_SALON_ID) REFERENCES $TABLE_SALONS($COLUMN_ID)
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createSalonsTable)
        db.execSQL(createServicesTable)
        db.execSQL(createAppointmentsTable)
<<<<<<< HEAD

        // Add test data
        addTestData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
=======
        db.execSQL(createRatingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RATINGS")
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SALONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun addTestData(db: SQLiteDatabase) {
        // Add admin user
        val adminValues = ContentValues().apply {
            put(COLUMN_EMAIL, "admin@admin.com")
            put(COLUMN_PASSWORD, "admin123")
            put(COLUMN_NAME, "Admin")
            put(COLUMN_USER_TYPE, "ADMIN")
        }
        db.insert(TABLE_USERS, null, adminValues)

        // Add test business user
        val businessValues = ContentValues().apply {
            put(COLUMN_EMAIL, "business@test.com")
            put(COLUMN_PASSWORD, "123456")
            put(COLUMN_NAME, "Test İşletme")
            put(COLUMN_USER_TYPE, "BUSINESS")
        }
        val businessId = db.insert(TABLE_USERS, null, businessValues)

        // Add test customer user
        val customerValues = ContentValues().apply {
            put(COLUMN_EMAIL, "asd")
            put(COLUMN_PASSWORD, "asd")
            put(COLUMN_NAME, "Test Müşteri")
            put(COLUMN_USER_TYPE, "CUSTOMER")
        }
        val customerId = db.insert(TABLE_USERS, null, customerValues)

        // Kuaförler
        val kuaforler = listOf(
            Triple("Elit Kuaför", "Bağdat Caddesi No:45", "0532 111 2233"),
            Triple("Modern Saç Tasarım", "İstiklal Caddesi No:78", "0533 222 3344"),
            Triple("Stil Kuaför", "Nişantaşı Mahallesi No:12", "0534 333 4455")
        )

        // Kuaför Hizmetleri
        val kuaforHizmetleri = listOf(
            Triple("Saç Kesimi", 150.0, 30),
            Triple("Saç Boyama", 400.0, 120),
            Triple("Perma", 500.0, 180),
            Triple("Düzleştirme", 600.0, 180),
            Triple("Saç Bakımı", 200.0, 60),
            Triple("Saç Maskesi", 150.0, 45),
            Triple("Şekillendirme", 100.0, 30),
            Triple("Saç Uzatma", 1500.0, 240)
        )

        // Her kuaför için hizmetleri ekle
        kuaforler.forEach { (name, address, phone) ->
            val salonValues = ContentValues().apply {
                put(COLUMN_SALON_NAME, name)
                put(COLUMN_ADDRESS, address)
                put(COLUMN_PHONE, phone)
                put(COLUMN_SALON_TYPE, "KUAFOR")
                put(COLUMN_OWNER_ID, businessId)
                put(COLUMN_RATING, (3.5 + (Math.random() * 1.5)))
            }
            val salonId = db.insert(TABLE_SALONS, null, salonValues)

            // Hizmetleri ekle
            kuaforHizmetleri.forEach { (serviceName, price, duration) ->
                val serviceValues = ContentValues().apply {
                    put(COLUMN_SERVICE_NAME, serviceName)
                    put(COLUMN_PRICE, price)
                    put(COLUMN_DURATION, duration)
                    put(COLUMN_SALON_ID, salonId)
                }
                db.insert(TABLE_SERVICES, null, serviceValues)
            }
        }

        // Güzellik Merkezleri
        val guzellikMerkezleri = listOf(
            Triple("Beauty Center", "Kadıköy Merkez No:67", "0535 444 5566"),
            Triple("Estetik & Güzellik", "Şişli Ana Cadde No:89", "0536 555 6677"),
            Triple("Lotus Beauty", "Beşiktaş Sahil No:34", "0537 666 7788")
        )

        // Güzellik Merkezi Hizmetleri
        val guzellikHizmetleri = listOf(
            Triple("Cilt Bakımı", 300.0, 60),
            Triple("Yüz Temizliği", 250.0, 45),
            Triple("Peeling", 200.0, 30),
            Triple("Lazer Epilasyon", 800.0, 90),
            Triple("Manikür", 150.0, 45),
            Triple("Pedikür", 180.0, 45),
            Triple("Kaş Alımı", 80.0, 20),
            Triple("Makyaj", 300.0, 60),
            Triple("Medikal Cilt Tedavisi", 1000.0, 90),
            Triple("Masaj", 400.0, 60),
            Triple("Vücut Bakımı", 500.0, 90)
        )

        // Her güzellik merkezi için hizmetleri ekle
        guzellikMerkezleri.forEach { (name, address, phone) ->
            val salonValues = ContentValues().apply {
                put(COLUMN_SALON_NAME, name)
                put(COLUMN_ADDRESS, address)
                put(COLUMN_PHONE, phone)
                put(COLUMN_SALON_TYPE, "GUZELLIK_MERKEZI")
                put(COLUMN_OWNER_ID, businessId)
                put(COLUMN_RATING, (3.5 + (Math.random() * 1.5)))
            }
            val salonId = db.insert(TABLE_SALONS, null, salonValues)

            // Hizmetleri ekle
            guzellikHizmetleri.forEach { (serviceName, price, duration) ->
                val serviceValues = ContentValues().apply {
                    put(COLUMN_SERVICE_NAME, serviceName)
                    put(COLUMN_PRICE, price)
                    put(COLUMN_DURATION, duration)
                    put(COLUMN_SALON_ID, salonId)
                }
                db.insert(TABLE_SERVICES, null, serviceValues)
            }
        }
    }

    fun openDatabase() {
        try {
            database = this.writableDatabase
            Log.d("DatabaseHelper", "Veritabanı başarıyla açıldı")
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Veritabanı açılırken hata: ${e.message}")
            e.printStackTrace()
        }
    }

    // Test randevularını silmek için ayrı bir fonksiyon
    fun clearTestAppointments() {
        try {
            // Sadece test müşterisinin randevularını sil
            val testUserId = getUserIdByEmail("asd")
            if (testUserId != null) {
                val result = database?.delete(
                    TABLE_APPOINTMENTS,
                    "$COLUMN_USER_ID = ?",
                    arrayOf(testUserId.toString())
                )
                Log.d("DatabaseHelper", "Test randevuları silindi. Silinen kayıt sayısı: $result")
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Test randevuları silinirken hata oluştu", e)
        }
    }

    fun addUser(email: String, password: String, name: String, userType: String): Long {
        Log.d("DatabaseHelper", "Yeni kullanıcı ekleniyor:")
        Log.d("DatabaseHelper", "Email: $email")
        Log.d("DatabaseHelper", "İsim: $name")
        Log.d("DatabaseHelper", "Kullanıcı Tipi: $userType")
        
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NAME, name)
            put(COLUMN_USER_TYPE, userType)
        }
<<<<<<< HEAD
        
        val userId = database?.insert(TABLE_USERS, null, values) ?: -1
        Log.d("DatabaseHelper", "Eklenen kullanıcı ID: $userId")
        return userId
=======
        return database?.insert(TABLE_USERS, null, values) ?: -1
    }

    fun addSalon(name: String, address: String, phone: String, salonType: String, ownerId: Long): Long {
        val values = ContentValues().apply {
            put(COLUMN_SALON_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
            put(COLUMN_SALON_TYPE, salonType)
            put(COLUMN_OWNER_ID, ownerId)
        }
        return database?.insert(TABLE_SALONS, null, values) ?: -1
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
    }

    fun checkUser(email: String, password: String): Boolean {
        val cursor = database?.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password),
            null,
            null,
            null
        )
        val exists = cursor?.count ?: 0 > 0
        cursor?.close()
        return exists
    }

    fun getUserType(email: String): String {
        val cursor = database?.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_TYPE),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        var userType = ""
        if (cursor?.moveToFirst() == true) {
            userType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_TYPE))
        }
        cursor?.close()
        return userType
    }

    fun checkUserExists(email: String): Boolean {
        val cursor = database?.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        val exists = cursor?.count ?: 0 > 0
        cursor?.close()
        return exists
    }

    fun getTotalUsers(): Int {
        val cursor = database?.query(
            TABLE_USERS,
            arrayOf("COUNT(*) as count"),
            null,
            null,
            null,
            null,
            null
        )
        
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        return count
    }

    fun getTotalBusinesses(): Int {
        val cursor = database?.query(
            TABLE_USERS,
            arrayOf("COUNT(*) as count"),
            "$COLUMN_USER_TYPE = ?",
            arrayOf("BUSINESS"),
            null,
            null,
            null
        )
        
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        return count
    }

    fun getTotalAppointments(): Int {
        val cursor = database?.query(
            TABLE_APPOINTMENTS,
            arrayOf("COUNT(*) as count"),
            null,
            null,
            null,
            null,
            null
        )
        
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        return count
    }

    fun getDailyAppointments(): Int {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
        
        val cursor = database?.query(
            TABLE_APPOINTMENTS,
            arrayOf("COUNT(*) as count"),
            "$COLUMN_DATE = ?",
            arrayOf(today),
            null,
            null,
            null
        )
        
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        return count
    }

    fun getSalonsByType(salonType: String): List<Map<String, String>> {
        val salons = mutableListOf<Map<String, String>>()
        try {
            val db = this.readableDatabase
            val cursor = db.query(
                TABLE_SALONS,
                null,
                "$COLUMN_SALON_TYPE = ?",
                arrayOf(salonType),
                null,
                null,
                "$COLUMN_CREATED_AT DESC"
            )

            Log.d("DatabaseHelper", "Salon tipi sorgusu: $salonType")
            
            cursor?.use {
                while (it.moveToNext()) {
                    val salon = mutableMapOf<String, String>()
                    salon["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                    salon["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_NAME))
                    salon["address"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                    salon["phone"] = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE))
                    salon["rating"] = it.getString(it.getColumnIndexOrThrow(COLUMN_RATING))
                    salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
                    salon["created_at"] = it.getString(it.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                    salons.add(salon)
                    
                    Log.d("DatabaseHelper", "Salon bulundu: ${salon["name"]}")
                }
            }
            
            Log.d("DatabaseHelper", "Toplam bulunan salon sayısı: ${salons.size}")
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Salon listesi alınırken hata: ${e.message}")
            e.printStackTrace()
        }
        return salons
    }

<<<<<<< HEAD
    fun addSalon(name: String, address: String, phone: String, salonType: String, ownerId: Long): Long {
        val values = ContentValues().apply {
            put(COLUMN_SALON_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
            put(COLUMN_SALON_TYPE, salonType)
            put(COLUMN_OWNER_ID, ownerId)
            put(COLUMN_RATING, 0.0) // Yeni salon için başlangıç puanı
        }
        return database?.insert(TABLE_SALONS, null, values) ?: -1
    }

    fun getServicesBySalonId(salonId: String): List<Map<String, Any>> {
        val services = mutableListOf<Map<String, Any>>()
        try {
            val cursor = database?.query(
                TABLE_SERVICES,
                null,
                "$COLUMN_SALON_ID = ?",
                arrayOf(salonId),
                null,
                null,
                null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val service = mutableMapOf<String, Any>()
                    service["id"] = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                    service["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_NAME))
                    service["price"] = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE))
                    service["duration"] = it.getInt(it.getColumnIndexOrThrow(COLUMN_DURATION))
                    services.add(service)
                }
            }
            Log.d("DatabaseHelper", "Bulunan hizmet sayısı: ${services.size}")
            services.forEach { service ->
                Log.d("DatabaseHelper", "Hizmet: ${service["name"]}, Fiyat: ${service["price"]}, Süre: ${service["duration"]}")
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Hizmetler alınırken hata: ${e.message}")
            e.printStackTrace()
        }
        return services
    }

    fun getAppointmentsBySalonId(salonId: String): List<Map<String, Any>> {
        Log.d("DatabaseHelper", "Salon için randevular aranıyor... Salon ID: $salonId")
        val appointments = mutableListOf<Map<String, Any>>()
        
        val query = """
            SELECT 
                a.id as appointment_id,
                a.date,
                a.time,
                a.status,
                u.name as customer_name,
                s.service_name,
                s.price,
                s.duration,
                s.salon_id
            FROM appointments a
            INNER JOIN users u ON a.user_id = u.id
            INNER JOIN services s ON a.service_id = s.id
            WHERE s.salon_id = ?
            ORDER BY a.date DESC, a.time DESC
        """.trimIndent()

        try {
            val cursor = readableDatabase.rawQuery(query, arrayOf(salonId))
            
            Log.d("DatabaseHelper", "Sorgu sonucu satır sayısı: ${cursor?.count ?: 0}")
            
            cursor?.use { c ->
                while (c.moveToNext()) {
                    val appointment = mapOf(
                        "appointment_id" to c.getLong(c.getColumnIndexOrThrow("appointment_id")),
                        "date" to c.getString(c.getColumnIndexOrThrow("date")),
                        "time" to c.getString(c.getColumnIndexOrThrow("time")),
                        "status" to c.getString(c.getColumnIndexOrThrow("status")),
                        "customer_name" to c.getString(c.getColumnIndexOrThrow("customer_name")),
                        "service_name" to c.getString(c.getColumnIndexOrThrow("service_name")),
                        "price" to c.getDouble(c.getColumnIndexOrThrow("price")),
                        "duration" to c.getInt(c.getColumnIndexOrThrow("duration")),
                        "salon_id" to c.getLong(c.getColumnIndexOrThrow("salon_id"))
                    )
                    appointments.add(appointment)
                    Log.d("DatabaseHelper", "Randevu bulundu: $appointment")
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Randevular alınırken hata oluştu", e)
        }

        Log.d("DatabaseHelper", "Toplam ${appointments.size} randevu bulundu")
        return appointments
    }

    fun getUserIdByEmail(email: String): Long? {
        var userId: Long? = null
        try {
            Log.d("DatabaseHelper", "Email için kullanıcı ID'si aranıyor: $email")
            
            val cursor = database?.query(
                "users",
                arrayOf("id"),
                "email = ?",
                arrayOf(email),
                null,
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    userId = it.getLong(it.getColumnIndexOrThrow("id"))
                    Log.d("DatabaseHelper", "Kullanıcı ID'si bulundu: $userId")
                } else {
                    Log.e("DatabaseHelper", "Email için kullanıcı bulunamadı: $email")
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Kullanıcı ID'si alınırken hata: ${e.message}")
            e.printStackTrace()
        }
        return userId
    }

    fun getSalonsByOwnerId(ownerId: Long): List<Map<String, String>> {
        val salons = mutableListOf<Map<String, String>>()
        try {
            val cursor = database?.query(
                TABLE_SALONS,
                null,
                "$COLUMN_OWNER_ID = ?",
                arrayOf(ownerId.toString()),
                null,
                null,
                "$COLUMN_CREATED_AT DESC"
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val salon = mutableMapOf<String, String>()
                    salon["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                    salon["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_NAME))
                    salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
                    salons.add(salon)
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "İşletme salonları alınırken hata: ${e.message}")
            e.printStackTrace()
=======
    fun addAppointment(userId: Long, serviceId: Long, date: String, time: String): Long {
        // Randevu çakışması kontrolü
        if (isTimeSlotTaken(serviceId, date, time)) {
            return -1
        }

        // Randevu son kullanma tarihini 1 saat sonrası olarak ayarla
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val expiryTime = calendar.timeInMillis

        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_SERVICE_ID, serviceId)
            put(COLUMN_APPOINTMENT_DATE, date)
            put(COLUMN_APPOINTMENT_TIME, time)
            put(COLUMN_STATUS, "PENDING")
            put(COLUMN_EXPIRY_TIME, expiryTime)
        }
        return database?.insert(TABLE_APPOINTMENTS, null, values) ?: -1
    }

    fun isTimeSlotTaken(serviceId: Long, date: String, time: String): Boolean {
        val cursor = database?.query(
            TABLE_APPOINTMENTS,
            arrayOf(COLUMN_ID),
            "$COLUMN_SERVICE_ID = ? AND $COLUMN_APPOINTMENT_DATE = ? AND $COLUMN_APPOINTMENT_TIME = ? AND $COLUMN_STATUS != 'CANCELLED'",
            arrayOf(serviceId.toString(), date, time),
            null,
            null,
            null
        )
        val isTaken = cursor?.count ?: 0 > 0
        cursor?.close()
        return isTaken
    }

    fun updateAppointmentStatus(appointmentId: Long, status: String): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_STATUS, status)
        }
        val result = database?.update(
            TABLE_APPOINTMENTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(appointmentId.toString())
        ) ?: 0
        return result > 0
    }

    fun getAppointmentsByUserId(userId: Long): List<Map<String, String>> {
        val appointments = mutableListOf<Map<String, String>>()
        val cursor = database?.query(
            TABLE_APPOINTMENTS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "$COLUMN_APPOINTMENT_DATE ASC, $COLUMN_APPOINTMENT_TIME ASC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val appointment = mutableMapOf<String, String>()
                appointment["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                appointment["service_id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_ID))
                appointment["date"] = it.getString(it.getColumnIndexOrThrow(COLUMN_APPOINTMENT_DATE))
                appointment["time"] = it.getString(it.getColumnIndexOrThrow(COLUMN_APPOINTMENT_TIME))
                appointment["status"] = it.getString(it.getColumnIndexOrThrow(COLUMN_STATUS))
                appointments.add(appointment)
            }
        }
        return appointments
    }

    fun getAppointmentsBySalonId(salonId: Long): List<Map<String, String>> {
        val appointments = mutableListOf<Map<String, String>>()
        val query = """
            SELECT a.*, s.service_name, u.name as user_name
            FROM $TABLE_APPOINTMENTS a
            JOIN $TABLE_SERVICES s ON a.$COLUMN_SERVICE_ID = s.$COLUMN_ID
            JOIN $TABLE_USERS u ON a.$COLUMN_USER_ID = u.$COLUMN_ID
            WHERE s.$COLUMN_SALON_ID = ?
            ORDER BY a.$COLUMN_APPOINTMENT_DATE ASC, a.$COLUMN_APPOINTMENT_TIME ASC
        """.trimIndent()
        
        val cursor = database?.rawQuery(query, arrayOf(salonId.toString()))

        cursor?.use {
            while (it.moveToNext()) {
                val appointment = mutableMapOf<String, String>()
                appointment["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                appointment["service_name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_NAME))
                appointment["user_name"] = it.getString(it.getColumnIndexOrThrow("user_name"))
                appointment["date"] = it.getString(it.getColumnIndexOrThrow(COLUMN_APPOINTMENT_DATE))
                appointment["time"] = it.getString(it.getColumnIndexOrThrow(COLUMN_APPOINTMENT_TIME))
                appointment["status"] = it.getString(it.getColumnIndexOrThrow(COLUMN_STATUS))
                appointments.add(appointment)
            }
        }
        return appointments
    }

    fun getSalonsByType(salonType: String): List<Map<String, String>> {
        val salons = mutableListOf<Map<String, String>>()
        val cursor = database?.query(
            TABLE_SALONS,
            null,
            "$COLUMN_SALON_TYPE = ?",
            arrayOf(salonType),
            null,
            null,
            "$COLUMN_SALON_NAME ASC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val salon = mutableMapOf<String, String>()
                salon["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                salon["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_NAME))
                salon["address"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                salon["phone"] = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE))
                salon["rating"] = it.getString(it.getColumnIndexOrThrow(COLUMN_RATING))
                salons.add(salon)
            }
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
        }
        return salons
    }

<<<<<<< HEAD
    fun getSalonsByOwnerEmail(email: String): List<Map<String, Any>> {
        val salons = mutableListOf<Map<String, Any>>()
        try {
            Log.d("DatabaseHelper", "İşletme salonları aranıyor. Email: $email")
            
            val userId = getUserIdByEmail(email)
            Log.d("DatabaseHelper", "Bulunan işletme ID: $userId")
            
            if (userId == null) {
                Log.e("DatabaseHelper", "İşletme kullanıcısı bulunamadı: $email")
                return emptyList()
            }

            val cursor = database?.query(
                TABLE_SALONS,
                null,
                "$COLUMN_OWNER_ID = ?",
                arrayOf(userId.toString()),
                null,
                null,
                "$COLUMN_CREATED_AT DESC"
            )

            Log.d("DatabaseHelper", "Salon sorgusu yapılıyor. Owner ID: $userId")
            
            cursor?.use {
                Log.d("DatabaseHelper", "Bulunan salon sayısı: ${it.count}")
                while (it.moveToNext()) {
                    val salon = mutableMapOf<String, Any>()
                    salon["id"] = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                    salon["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_NAME))
                    salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
                    salons.add(salon)
                    
                    Log.d("DatabaseHelper", "Salon bulundu: ID=${salon["id"]}, İsim=${salon["name"]}")
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "İşletme salonları alınırken hata: ${e.message}")
            e.printStackTrace()
        }
        return salons
    }

    fun addAppointment(userId: Long, serviceId: Long, date: String, time: String): Long {
        var appointmentId = -1L
        try {
            Log.d("DatabaseHelper", "Randevu ekleniyor:")
            Log.d("DatabaseHelper", "Kullanıcı ID: $userId")
            Log.d("DatabaseHelper", "Hizmet ID: $serviceId")
            Log.d("DatabaseHelper", "Tarih: $date")
            Log.d("DatabaseHelper", "Saat: $time")

            val values = ContentValues().apply {
                put("user_id", userId)
                put("service_id", serviceId)
                put("date", date)
                put("time", time)
                put("status", "PENDING")
            }

            appointmentId = database?.insert("appointments", null, values) ?: -1L

            if (appointmentId > 0) {
                Log.d("DatabaseHelper", "Randevu başarıyla eklendi. ID: $appointmentId")
            } else {
                Log.e("DatabaseHelper", "Randevu eklenemedi")
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Randevu eklenirken hata: ${e.message}")
            e.printStackTrace()
        }
        return appointmentId
    }

    fun updateAppointmentStatus(appointmentId: Long, newStatus: String): Int {
        try {
            val values = ContentValues().apply {
                put(COLUMN_STATUS, newStatus)
            }
            
            Log.d("DatabaseHelper", "Randevu durumu güncelleniyor: ID=$appointmentId, " +
                    "Yeni Durum=$newStatus")
            
            return database?.update(
                TABLE_APPOINTMENTS,
                values,
                "$COLUMN_ID = ?",
                arrayOf(appointmentId.toString())
            ) ?: 0
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Randevu durumu güncellenirken hata: ${e.message}")
            e.printStackTrace()
            return 0
        }
    }

=======
>>>>>>> 66b73c9375607998507e14140f90c42c8bd3d6bd
    override fun close() {
        database?.close()
        super.close()
    }
} 