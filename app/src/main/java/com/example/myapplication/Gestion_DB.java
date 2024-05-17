package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
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
        db.execSQL("CREATE TABLE Carte (_id INTEGER PRIMARY KEY AUTOINCREMENT, totalPoint INTEGER, totalCarte INTEGER,tempsDeJeu INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Carte");
        onCreate(db);
    }

    public void emptyDatabase(){
        db.execSQL("DROP TABLE IF EXISTS Carte");
        onCreate(db);
    }


    public void ouvrirConnexion() {
        db = this.getWritableDatabase();
    }

    public void fermerConnexion() {
        db.close();
    }

    public void ajouterScore(int score, int carte,int temps){
        // besoin de faire un ContentValues (ce comporte comme un hashTable)
        ContentValues values = new ContentValues();

        // setup les data pour le insert
        values.put("totalPoint", score);
        values.put("totalCarte", carte);
        values.put("tempsDeJeu", temps);

        db.insert("Carte", null, values);
    }


    @SuppressLint("Range")
    public Vector<Integer> getTopThreeScores() {
        Vector<Integer> topScores = new Vector<>();
        Cursor cursor = null;

        String query = "SELECT totalPoint FROM Carte ORDER BY totalPoint DESC LIMIT 3";
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                if (!cursor.isNull(cursor.getColumnIndex("totalPoint"))) {
                    int score = cursor.getInt(cursor.getColumnIndex("totalPoint"));
                    topScores.add(score);
                }
                else{
                    topScores.add(0);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return topScores;
    }

}
