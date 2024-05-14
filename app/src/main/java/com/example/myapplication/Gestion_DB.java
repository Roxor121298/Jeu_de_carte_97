package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class Gestion_DB  extends SQLiteOpenHelper {

    private static Gestion_DB instance;

    private SQLiteDatabase db;


    public static Gestion_DB getInstance(Context contexte) {
        if (instance == null)
            instance = new Gestion_DB(contexte);
        return instance;
    }

    private Gestion_DB(Context context) {
        super(context, "db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Carte (_id INTEGER PRIMARY KEY AUTOINCREMENT, totalPoint INTEGER, totalCarte INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }

    public void emptyDatabase() {
        db.execSQL("DROP TABLE IF EXISTS Carte");
        onCreate(db);
    }

    public void ouvrirConnexion() {
        db = this.getWritableDatabase();
    }

    public void fermerConnexion() {
        db.close();
    }

}
