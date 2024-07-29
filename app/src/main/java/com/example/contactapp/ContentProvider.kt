package com.example.contactapp

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class ContentProvider : ContentProvider() {

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val AUTHORITY = "com.example.contactapp"
        private const val BASE_PATH = "contact"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")

        private const val CONTACTS = 1
        private const val CONTACT_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, BASE_PATH, CONTACTS)
            addURI(AUTHORITY, "$BASE_PATH/#", CONTACT_ID)
        }
    }

    override fun onCreate(): Boolean {
        context?.let {
            val helper = DBOpenHelper(it)
            database = helper.writableDatabase
            return true
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = when (uriMatcher.match(uri)) {
            CONTACTS -> database.query(
                DBOpenHelper.TABLE_CONTACTS,
                DBOpenHelper.ALL_COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                "${DBOpenHelper.CONTACT_NAME} ASC"
            )
            else -> throw IllegalArgumentException("unknown URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            CONTACTS -> "vnd.android.cursor.dir/contact"
            else -> throw IllegalArgumentException("Unknown URI : $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = database.insert(DBOpenHelper.TABLE_CONTACTS, null, values)
        if (id > 0) {
            val insertedUri = ContentUris.withAppendedId(CONTENT_URI, id)
            context?.contentResolver?.notifyChange(insertedUri, null)
            return insertedUri
        }
        throw SQLException("failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delCount = when (uriMatcher.match(uri)) {
            CONTACTS -> database.delete(DBOpenHelper.TABLE_CONTACTS, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return delCount
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updCount = when (uriMatcher.match(uri)) {
            CONTACTS -> database.update(DBOpenHelper.TABLE_CONTACTS, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return updCount
    }
}
