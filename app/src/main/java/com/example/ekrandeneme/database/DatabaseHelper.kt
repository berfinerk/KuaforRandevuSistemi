package com.example.ekrandeneme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        private const val TABLE_CART = "cart"

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
                $COLUMN_RATING REAL DEFAULT 0,
                $COLUMN_SALON_TYPE TEXT NOT NULL,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createSalonsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SALONS")
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

    fun addSalon(name: String, address: String, phone: String, salonType: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_SALON_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE, phone)
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
                salon["type"] = it.getString(it.getColumnIndexOrThrow(COLUMN_SALON_TYPE))
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