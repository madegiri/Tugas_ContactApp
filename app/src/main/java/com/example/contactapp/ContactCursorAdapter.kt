package com.example.contactapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter

class ContactCursorAdapter(context: Context, cursor: Cursor?, flags: Int) :
    CursorAdapter(context, cursor, flags) {

    override fun newView(context: Context, cursor: Cursor?, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false)
    }

    @SuppressLint("Range")
    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val contactName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.CONTACT_NAME))
        val contactPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.CONTACT_PHONE))
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val phoneTextView: TextView = view.findViewById(R.id.phoneTextView)
        nameTextView.text = contactName
        phoneTextView.text = contactPhone
    }
}
