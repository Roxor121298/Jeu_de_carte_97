package com.example.myapplication;

import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Carte {

    int nb;
    int couleur;
    boolean played_state;

    public Carte(int nb){
        this.nb = nb;
        if(this.nb < 33){
            this.couleur = Color.parseColor("#ffa500"); // orange
        }
        else if (this.nb < 66){
            this.couleur = YELLOW; // static color yellow
        }
        else
            this.couleur = RED; // static color red
        this.played_state = false;
    }
    public void DessinerCarte(LinearLayout layout, Context contexte, Ecran_de_jeu.EcouteurDrag drag,Drawable dessin){

        TextView textView = new TextView(contexte);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(String.valueOf(this.getNb())); // set le num sur la carte
        textView.setBackground(contexte.getDrawable(R.drawable.carte));
        textView.setTextColor(Color.WHITE);  // Set text color that contrasts with the card image
        textView.setTextSize(22); // Set text size
        textView.setTag(this.getNb()); // Set tag == nb de la carte
        textView.setOnTouchListener(drag);
        layout.addView(textView);
    }
    public void JouerCarte(LinearLayout l, ImageView I){

    }
    public int getNb() {return nb;}
    public void setNb(int nb) {this.nb = nb;}
    public int getCouleur() {return couleur;}
    public void setCouleur(int couleur) {this.couleur = couleur;}
    public boolean isPlayed_state() {return played_state;}
    public void setPlayed_state(boolean played_state) {this.played_state = played_state;}
}
