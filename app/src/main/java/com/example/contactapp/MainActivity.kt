package com.example.contactapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cursoradapter.widget.CursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ListView

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var cursorAdapter: CursorAdapter

    companion object {
        private const val AUTHORITY = "com.example.contactapp"
        private const val BASE_PATH = "contact"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cursorAdapter = ContactCursorAdapter(this, null, 0)
        val list = findViewById<ListView>(R.id.list)
        list.adapter = cursorAdapter

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        LoaderManager.getInstance(this).initLoader(0, null, this)
        val fab = findViewById<FloatingActionButton>(R.id.add_button)
        fab.setOnClickListener {
            inputKontak()
        }
    }

    private fun restartLoader() {
        LoaderManager.getInstance(this).restartLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this, CONTENT_URI, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursorAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        cursorAdapter.swapCursor(null)
    }

    private fun inputKontak() {
        val li = LayoutInflater.from(this)
        val view = li.inflate(R.layout.dialog_add_contact, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(view)

        val nameInput = view.findViewById<EditText>(R.id.edtNama)
        val numberInput = view.findViewById<EditText>(R.id.edtPhone)

        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                val contentValues = ContentValues().apply {
                    put(DBOpenHelper.CONTACT_NAME, nameInput.text.toString())
                    put(DBOpenHelper.CONTACT_PHONE, numberInput.text.toString())
                }

                contentResolver.insert(ContentProvider.CONTENT_URI, contentValues)
                restartLoader()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }
}
