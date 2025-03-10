package com.example.ekrandeneme

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (private val context: Context):

SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {



    companion object{
        private const val DATABASE_NAME ="UserDatabase.db" //Veritabanı adı
        private const val DATABASE_VERSION =1  //veritabanı sürümü
        private const val TABLE_NAME ="data"  //tablo adı
        private const val COLUMN_ID ="id"     // ID sütunu (genellikle Primary Key olur)
        private const val COLUMN_USERNAME ="username" // Kullanıcı adı sütunu
        private const val COLUMN_PASSWORD ="password"  //Şifre sütunu

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("+
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ //otomatik artan ID
                "$COLUMN_USERNAME TEXT, "+ //kullanıcı adı sütunu
                "$COLUMN_PASSWORD TEXT)") //sifre sütunu
        db?.execSQL(createTableQuery) //tabloyu oluştur
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       val dropTableQuery= "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery) //tabloyu sil
        onCreate(db) //yeni tablo oluştur
    }
    fun instertUser(username: String ,password: String): Long{
        val values = ContentValues().apply {
            put(COLUMN_USERNAME,username)//kullanıcı adı ekle
            put(COLUMN_PASSWORD,password) //şifre ekle
        }
        val db =writableDatabase
        return db.insert(TABLE_NAME,null,values) //yeni kullanıcı ekle
    }

    fun readUser(username: String,password: String): Boolean {
        val read = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?" //kullanıcı adı ve şifre kontrolu
        val selectionArgs = arrayOf(username,password)
        val cursor = read.query(TABLE_NAME,null,selection,selectionArgs,null,null,null) //rea yerine db yazmıştı


        val userExists= cursor.count > 0 //kullanıcı varsa value doner
        cursor.close()
        return userExists

    }

}