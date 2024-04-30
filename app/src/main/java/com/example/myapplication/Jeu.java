package com.example.myapplication;

import java.util.Vector;

public class Jeu {
    Packet Packet;

    public Jeu(){
        this.Packet = new Packet();
    }

    public Carte PigerUneCarte(){
        Carte node = this.Packet.getDeck().firstElement();
        this.Packet.getDeck().removeElementAt(0);
        return node;
    }
}
