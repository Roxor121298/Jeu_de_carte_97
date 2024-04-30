package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class Ecran_de_jeu extends AppCompatActivity {

    LinearLayout Deck;

    View CarteNode;

    Jeu jeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarteNode = new View(this);
        Deck = findViewById(R.id.Deck);

        for(int i =0; i<Deck.getChildCount(); i++){
            LinearLayout ranger = (LinearLayout) Deck.getChildAt(i);
            for(int j =0; j<ranger.getChildCount();j++){
                ranger.addView(new View(this));
            }
        }
    }


    private class EcouteurTouch implements View.OnTouchListener,View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }
}