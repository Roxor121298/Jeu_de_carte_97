package com.example.myapplication;

import java.util.Collections;
import java.util.Vector;

public class Packet {

    Vector<Carte> deck;

    public Packet(){
        for(int i = 1; i<98; i++){
            this.deck.add(new Carte(i));
        }
        Collections.shuffle(this.deck);
    }

    private void shuffle(Vector<Carte> deck){

    }

    public void RetirerUneCarte(){ }

    public Vector<Carte> getDeck() {return deck;}
    public void setDeck(Vector<Carte> deck) {this.deck = deck;}
}
