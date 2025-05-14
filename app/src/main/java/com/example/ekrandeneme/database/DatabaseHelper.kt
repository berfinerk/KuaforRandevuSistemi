package com.example.ekrandeneme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    var database: SQLiteDatabase? = null

    companion object {
        private const val DATABASE_NAME = "KuaforDB"
        private const val DATABASE_VERSION = 5

        // Tablo isimleri
        private const val TABLE_USERS = "users"
        private const val TABLE_SALONS = "salons"
        private const val TABLE_SERVICES = "services"
        private const val TABLE_APPOINTMENTS = "appointments"
        private const val TABLE_RATINGS = "ratings"
        private const val TABLE_CART = "cart"
        private const val TABLE_EMPLOYEES = "employees"

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
    }

    fun openDatabase() {
        database = this.writableDatabase
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Users tablosu
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

        // Salons tablosu
        val createSalonsTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_SALONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SALON_NAME TEXT NOT NULL,
                $COLUMN_ADDRESS TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL,
                email TEXT NOT NULL,
                $COLUMN_RATING REAL DEFAULT 0,
                $COLUMN_SALON_TYPE TEXT NOT NULL,
                profile_text TEXT,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        // Appointments tablosu (employee_id eklendi)
        val createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_APPOINTMENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                salon_id INTEGER NOT NULL,
                user_email TEXT NOT NULL,
                hizmet TEXT NOT NULL,
                tarih TEXT NOT NULL,
                saat TEXT NOT NULL,
                price TEXT,
                employee_id TEXT,
                status TEXT NOT NULL DEFAULT 'PENDING',
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        // Service_Employees tablosu (hizmet-çalışan eşleştirme)
        val createServiceEmployeesTable = """
            CREATE TABLE IF NOT EXISTS service_employees (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                service_id INTEGER NOT NULL,
                employee_id INTEGER NOT NULL
            )
        """.trimIndent()

        // Services tablosu
        val createServicesTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_SERVICES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                salon_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                price TEXT NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        // Employees tablosu
        val createEmployeesTable = """
            CREATE TABLE IF NOT EXISTS employees (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                salon_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                role TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createSalonsTable)
        db.execSQL(createAppointmentsTable)
        db.execSQL(createServicesTable)
        db.execSQL(createEmployeesTable)
        db.execSQL(createServiceEmployeesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_APPOINTMENTS ADD COLUMN price TEXT;")
        }
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE $TABLE_APPOINTMENTS ADD COLUMN employee_id TEXT;")
            db.execSQL("CREATE TABLE IF NOT EXISTS service_employees (id INTEGER PRIMARY KEY AUTOINCREMENT, service_id INTEGER NOT NULL, employee_id INTEGER NOT NULL);")
        }
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SALONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPLOYEES")
        db.execSQL("DROP TABLE IF EXISTS service_employees")
        onCreate(db)
    }

    fun addUser(email: String, password: String, name: String, userType: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NAME, name)
            put(COLUMN_USER_TYPE, userType)
        }
        return database?.insert(TABLE_USERS, null, values) ?: -1
    }

    fun addSalon(name: String, address: String, phone: String, email: String, salonType: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_SALON_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
            put("email", email)
            put(COLUMN_SALON_TYPE, salonType)
        }
        return database?.insert(TABLE_SALONS, null, values) ?: -1
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
        cursor?.use {
            if (it.moveToFirst()) {
                userType = it.getString(it.getColumnIndexOrThrow(COLUMN_USER_TYPE))
            }
        }
        return userType
    }

    fun getAllSalons(): List<Map<String, String>> {
        val salons = mutableListOf<Map<String, String>>()
        val cursor = database?.query(
            TABLE_SALONS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val salon = mutableMapOf<String, String>()
                salon["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                salon["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_NAME))
                salon["address"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                salon["phone"] = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE))
                salon["email"] = it.getString(it.getColumnIndexOrThrow("email"))
                salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
                salons.add(salon)
            }
        }
        return salons
    }

    fun addAppointment(salonId: String, userEmail: String, hizmet: String, tarih: String, saat: String, price: String): Long {
        val values = ContentValues().apply {
            put("salon_id", salonId)
            put("user_email", userEmail)
            put("hizmet", hizmet)
            put("tarih", tarih)
            put("saat", saat)
            put("price", price)
            put("status", "PENDING")
        }
        return database?.insert(TABLE_APPOINTMENTS, null, values) ?: -1
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: String): Int {
        val values = ContentValues().apply {
            put("status", newStatus)
        }
        val updated = database?.update(
            TABLE_APPOINTMENTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(appointmentId)
        ) ?: 0
        return updated
    }

    fun getAppointmentsForSalon(salonName: String): List<Map<String, String>> {
        // Salonun id'sini bul
        val cursorSalon = database?.query(
            TABLE_SALONS,
            arrayOf(COLUMN_ID),
            "$COLUMN_SALON_NAME = ?",
            arrayOf(salonName),
            null, null, null
        )
        var salonId: String? = null
        cursorSalon?.use {
            if (it.moveToFirst()) {
                salonId = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
            }
        }
        if (salonId == null) return emptyList()
        val appointments = mutableListOf<Map<String, String>>()
        val cursor = database?.query(
            TABLE_APPOINTMENTS,
            null,
            "salon_id = ?",
            arrayOf(salonId),
            null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val appt = mutableMapOf<String, String>()
                appt["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                appt["user_email"] = it.getString(it.getColumnIndexOrThrow("user_email"))
                appt["hizmet"] = it.getString(it.getColumnIndexOrThrow("hizmet"))
                appt["tarih"] = it.getString(it.getColumnIndexOrThrow("tarih"))
                appt["saat"] = it.getString(it.getColumnIndexOrThrow("saat"))
                appt["status"] = it.getString(it.getColumnIndexOrThrow("status"))
                val empIdx = it.getColumnIndex("employee_id")
                if (empIdx != -1) {
                    appt["employee_id"] = it.getString(empIdx)
                }
                appointments.add(appt)
            }
        }
        return appointments
    }

    fun getUserName(email: String): String {
        if (database == null || !database!!.isOpen) {
            openDatabase()
        }
        val cursor = database?.query(
            TABLE_USERS,
            arrayOf(COLUMN_NAME),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        var name = "Bilinmeyen Kullanıcı"
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
            }
        }
        return name
    }

    fun getSalonById(id: String): Map<String, String> {
        val cursor = database?.query(
            TABLE_SALONS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null, null, null
        )
        val salon = mutableMapOf<String, String>()
        cursor?.use {
            if (it.moveToFirst()) {
                salon["id"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ID))
                salon["name"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_NAME))
                salon["address"] = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                salon["phone"] = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE))
                salon["email"] = it.getString(it.getColumnIndexOrThrow("email"))
                salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
                salon["rating"] = it.getString(it.getColumnIndexOrThrow(COLUMN_RATING))
            }
        }
        return salon
    }

    fun getServicesForSalon(salonId: String): List<Pair<String, String>> {
        val services = mutableListOf<Pair<String, String>>()
        val cursor = database?.query(
            TABLE_SERVICES,
            null,
            "salon_id = ?",
            arrayOf(salonId),
            null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val price = it.getString(it.getColumnIndexOrThrow("price"))
                services.add(name to price)
            }
        }
        return services
    }

    fun addService(salonId: String, name: String, price: String): Long {
        val values = ContentValues().apply {
            put("salon_id", salonId)
            put("name", name)
            put("price", price)
        }
        return database?.insert(TABLE_SERVICES, null, values) ?: -1
    }

    fun addEmployee(salonId: String, name: String, role: String): Long {
        val values = ContentValues().apply {
            put("salon_id", salonId)
            put("name", name)
            put("role", role)
        }
        return database?.insert(TABLE_EMPLOYEES, null, values) ?: -1
    }

    fun deleteEmployee(employeeId: String): Int {
        return database?.delete(TABLE_EMPLOYEES, "id = ?", arrayOf(employeeId)) ?: 0
    }

    fun getEmployeesForSalon(salonId: String): List<Map<String, String>> {
        val employees = mutableListOf<Map<String, String>>()
        val cursor = database?.query(
            TABLE_EMPLOYEES,
            null,
            "salon_id = ?",
            arrayOf(salonId),
            null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val emp = mutableMapOf<String, String>()
                emp["id"] = it.getString(it.getColumnIndexOrThrow("id"))
                emp["name"] = it.getString(it.getColumnIndexOrThrow("name"))
                emp["role"] = it.getString(it.getColumnIndexOrThrow("role"))
                employees.add(emp)
            }
        }
        return employees
    }

    fun updateSalonProfileText(salonId: String, profileText: String): Int {
        val values = ContentValues().apply {
            put("profile_text", profileText)
        }
        return database?.update(TABLE_SALONS, values, "$COLUMN_ID = ?", arrayOf(salonId)) ?: 0
    }

    fun getSalonProfileText(salonId: String): String {
        var text = ""
        val cursor = database?.query(
            TABLE_SALONS,
            arrayOf("profile_text"),
            "$COLUMN_ID = ?",
            arrayOf(salonId),
            null, null, null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                text = it.getString(it.getColumnIndexOrThrow("profile_text")) ?: ""
            }
        }
        return text
    }

    // Belirli bir çalışan, tarih ve saat için çakışan ONAYLANMIŞ randevu var mı?
    fun isAppointmentSlotTaken(employeeId: String, tarih: String, saat: String): Boolean {
        val cursor = database?.rawQuery(
            "SELECT COUNT(*) FROM appointments WHERE employee_id = ? AND tarih = ? AND saat = ? AND status = 'APPROVED'",
            arrayOf(employeeId, tarih, saat)
        )
        var result = false
        cursor?.use {
            if (it.moveToFirst()) {
                val count = it.getInt(0)
                result = count > 0
            }
        }
        return result
    }

    override fun close() {
        database?.close()
        super.close()
    }
} 