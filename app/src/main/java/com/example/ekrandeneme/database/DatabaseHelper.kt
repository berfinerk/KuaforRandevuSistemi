package com.example.ekrandeneme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var database: SQLiteDatabase? = null

    companion object {
        private const val DATABASE_NAME = "KuaforDB"
        private const val DATABASE_VERSION = 1

        // Tablo isimleri
        private const val TABLE_USERS = "users"
        private const val TABLE_SALONS = "salons"
        private const val TABLE_SERVICES = "services"
        private const val TABLE_APPOINTMENTS = "appointments"
        private const val TABLE_RATINGS = "ratings"

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
                $COLUMN_RATING REAL DEFAULT 0,
                $COLUMN_SALON_TYPE TEXT NOT NULL,
                $COLUMN_OWNER_ID INTEGER NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY($COLUMN_OWNER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)
            )
        """.trimIndent()

        // Services tablosu
        val createServicesTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_SERVICES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SERVICE_NAME TEXT NOT NULL,
                $COLUMN_SERVICE_DURATION INTEGER NOT NULL,
                $COLUMN_SERVICE_PRICE REAL NOT NULL,
                $COLUMN_SALON_ID INTEGER NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY($COLUMN_SALON_ID) REFERENCES $TABLE_SALONS($COLUMN_ID)
            )
        """.trimIndent()

        // Appointments tablosu
        val createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_APPOINTMENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_SERVICE_ID INTEGER NOT NULL,
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
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createSalonsTable)
        db.execSQL(createServicesTable)
        db.execSQL(createAppointmentsTable)
        db.execSQL(createRatingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RATINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SALONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
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

    fun addSalon(name: String, address: String, phone: String, salonType: String, ownerId: Long): Long {
        val values = ContentValues().apply {
            put(COLUMN_SALON_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
            put(COLUMN_SALON_TYPE, salonType)
            put(COLUMN_OWNER_ID, ownerId)
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
                salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
                salons.add(salon)
            }
        }
        return salons
    }

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
        }
        return salons
    }

    override fun close() {
        database?.close()
        super.close()
    }
} 