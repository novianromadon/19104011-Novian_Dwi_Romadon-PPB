package com.novian.sqlite.db

import android.provider.BaseColumns

// Class ini digunakan sebagai entitas tabel yang akan dibuat
// Entitas ini akan digenerate menjadi sebuat tabel ketika aplikasi pertama kali di jalankan
internal class DatabaseContract {
    internal class QuoteColumns : BaseColumns {
        companion object {
            const val TABLE_QUOTE = "quote"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val CATEGORY = "category"
            const val DATE = "date"
        }
    }
}