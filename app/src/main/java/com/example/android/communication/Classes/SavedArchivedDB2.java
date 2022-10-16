package com.example.android.communication.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SavedArchivedDB2 {

    SQLiteDatabase cx2;
    ArrayList<String> list2 = new ArrayList<String>();
    SavedChat SA2;
    Context c2;
    String nombreBD2 = "BD2";
    String tabla2 = "create table if not exists SavedArchived2(sender text, senderID text)";

    public SavedArchivedDB2(Context c2) {
        this.c2 = c2;
        cx2 = c2.openOrCreateDatabase(nombreBD2, Context.MODE_PRIVATE, null);
        cx2.execSQL(tabla2);
    }

    public SavedArchivedDB2() {

    }

    public boolean insertar2(SavedChat SA2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sender", SA2.getSender());
        contentValues.put("senderID", SA2.getSenderID());
        return (cx2.insert("SavedArchived2", null, contentValues)) > 0;
    }

    public boolean eliminar2(String senderID) {
        return true;
    }

    public void clear2() {
        cx2.execSQL("DROP TABLE IF EXISTS SavedArchived2");
    }

    public boolean editar2(SavedChat SA) {
        return true;
    }

    public ArrayList<String> verTodos2() {
        list2.clear();
        Cursor cursor = cx2.rawQuery("select * from SavedArchived2", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list2.add(cursor.getString(0));
                list2.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return list2;
    }

    public void deleteOne2(String Sender, String SenderID) {
        cx2.execSQL("delete from SavedArchived2 where sender=Sender");
    }
}
