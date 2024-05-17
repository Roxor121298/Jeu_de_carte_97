package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class Ecran_fin extends AppCompatActivity {

    Gestion_DB instance;

    TextView TopScore1,TopScore2,TopScore3;

    List<Integer> topScore;

    Button Recommencer;

    Ecouteurclick ec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_fin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = Gestion_DB.getInstance(getApplicationContext());
        instance.ouvrirConnexion();

        ec = new Ecouteurclick();

        Recommencer = findViewById(R.id.Recommencer);
        Recommencer.setOnClickListener(ec);

        TopScore1 = findViewById(R.id.TopScore1);
        TopScore2 = findViewById(R.id.TopScore2);
        TopScore3 = findViewById(R.id.TopScore3);


        topScore = instance.getTopThreeScores();

        if (topScore.size() > 0) {
            TopScore1.setText("Meilleur score !! " + topScore.get(0).toString());
        } else {
            TopScore1.setText("Meilleur score !! " + "N/A");
        }

        if (topScore.size() > 1) {
            TopScore2.setText("Deuxieme Position ! " + topScore.get(1).toString());
        } else {
            TopScore2.setText("Deuxieme Position ! " + "N/A");
        }

        if (topScore.size() > 2) {
            TopScore3.setText("Troisieme place... " + topScore.get(2).toString());
        } else {
            TopScore3.setText("Troisieme place... " + "N/A");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.fermerConnexion();
    }

    private class Ecouteurclick implements View.OnClickListener {

        @Override
        public void onClick(View node) {

            Intent i = new Intent(getApplicationContext(), Ecran_fin.class );
            startActivity(i);


        }
    }
}