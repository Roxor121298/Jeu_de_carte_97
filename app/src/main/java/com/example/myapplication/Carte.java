package com.example.myapplication;

import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class Carte {

    int nb;
    int couleur;
    boolean played_state;

    public Carte(int nb){
        this.nb = nb;
        if(this.nb < 33){
            this.couleur = Integer.parseInt("#ffa500"); // orange
        }
        else if (this.nb < 66){
            this.couleur = YELLOW; // static color yellow
        }
        else
            this.couleur = RED; // static color red
        this.played_state = false;
    }
}
