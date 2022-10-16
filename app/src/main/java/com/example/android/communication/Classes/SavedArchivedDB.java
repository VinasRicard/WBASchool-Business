package com.example.android.communication.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SavedArchivedDB {

    SQLiteDatabase cx;
    ArrayList<String> list = new ArrayList<String>();
    SavedChat SA;
    Context c;
    String nombreBD = "BD";
    String tabla = "create table if not exists SavedArchived(sender text, senderID text)";

    public SavedArchivedDB(Context c) {
        this.c = c;
        cx = c.openOrCreateDatabase(nombreBD, Context.MODE_PRIVATE, null);
        cx.execSQL(tabla);
    }

    public SavedArchivedDB() {

    }

    public boolean insertar(SavedChat SA) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sender", SA.getSender());
        contentValues.put("senderID", SA.getSenderID());
        return (cx.insert("SavedArchived", null, contentValues)) > 0;
    }

    public boolean eliminar(String senderID) {
        return true;
    }

    public void clear() {
        cx.execSQL("DROP TABLE IF EXISTS SavedArchived");
    }

    public boolean editar(SavedChat SA) {
        return true;
    }

    public ArrayList<String> verTodos() {
        list.clear();
        Cursor cursor = cx.rawQuery("select * from SavedArchived", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(0));
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void deleteOne(String Sender, String SenderID) {
        cx.execSQL("delete from SavedArchived where sender=Sender");
    }
}
