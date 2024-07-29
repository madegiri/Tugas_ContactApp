package com.example.contactapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_CONTACTS = "contact"
        const val CONTACT_ID = "_id"
        const val CONTACT_NAME = "contactName"
        const val CONTACT_PHONE = "contactPhone"
        const val CONTACT_CREATED_ON = "contactCreationTimeStamp"

        val ALL_COLUMNS = arrayOf(CONTACT_ID, CONTACT_NAME, CONTACT_PHONE, CONTACT_CREATED_ON)

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_CONTACTS (" +
                "$CONTACT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CONTACT_NAME TEXT, " +
                "$CONTACT_PHONE TEXT, " +
                "$CONTACT_CREATED_ON TEXT default CURRENT_TIMESTAMP" +
                ")"
    }
}
