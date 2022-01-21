package com.novian.sqlite.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.novian.sqlite.db.DatabaseContract.QuoteColumns.Companion.TABLE_QUOTE
import com.novian.sqlite.db.DatabaseContract.QuoteColumns.Companion._ID

class QuoteHelper(context: Context) {
    companion object {
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: QuoteHelper? = null
        private lateinit var database: SQLiteDatabase
        fun getInstance(context: Context): QuoteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: QuoteHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    // Untuk membuka koneksi database
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    // Untuk menutup koneksi database
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    // Untuk memanggil semua data pada tabel quote
    fun queryAll(): Cursor {
        return database.query(
            TABLE_QUOTE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    // Untuk memanggil 1 data berdasarkan ID yang dipilih
    fun queryById(id: String): Cursor {
        return database.query(
            TABLE_QUOTE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    // Untuk menyimpan data
    fun insert(values: ContentValues?): Long {
        return database.insert(
            TABLE_QUOTE, null, values)
    }

    // Untuk mengubah data
    fun update(id: String, values: ContentValues?): Int {
        return database.update(
            TABLE_QUOTE, values, "$_ID = ?", arrayOf(id))
    }

    // Untuk menghapus data berdasarkan ID yang dipilih
    fun deleteById(id: String): Int {
        return database.delete(
            TABLE_QUOTE, "$_ID = '$id'", null)
    }
}