package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Ecran_acceuil extends AppCompatActivity {


    // Object media player
    private MediaPlayer mediaPlayer;

    Button Jouer;

    Ecouteurclick ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        Jouer = findViewById(R.id.Jouer);
        ec = new Ecouteurclick();
        Jouer.setOnClickListener(ec);

        // Inittialisation
        mediaPlayer = MediaPlayer.create(this, R.raw.musique);
        mediaPlayer.start(); // commence musique

        // Set sur loop
        mediaPlayer.setLooping(true);

        // lorsque cest finis (si on voulais faire dequoi mais la ça ne fait rien)
        mediaPlayer.setOnCompletionListener(mp -> {
            mediaPlayer.release();
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause si on pause
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // reviens si on reviens
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // stop la musique
            mediaPlayer.release(); // je crois que c'est un genre de clear
            mediaPlayer = null;
        }
    }

    private class Ecouteurclick implements View.OnClickListener {

        @Override
        public void onClick(View node) {

            if(node == Jouer){
                Intent i = new Intent(getApplicationContext(), Ecran_de_jeu.class );
                startActivity(i);
                //finish();
            }

        }
    }
}