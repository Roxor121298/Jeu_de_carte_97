package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Vector;

//import kotlin.io.LineReader;

public class Ecran_de_jeu extends AppCompatActivity {

    int[] deckIds;

    LinearLayout[] deckLayouts;
    Vector<Carte> cartes;
    Vector<Carte> main;
    LinearLayout Deck, Jouer1, Jouer2, Jouer3, Jouer4;

    LinearLayout Deck1,Deck2,Deck3,Deck4,Deck5,Deck6,Deck7,Deck8;

    TextView CarteNode;

    Jeu jeu;

    EcouteurDrag drag;

    TextView TimerPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarteNode = new TextView(this);
        Deck = findViewById(R.id.Deck);
        drag = new EcouteurDrag();



        Deck1 = findViewById(R.id.Deck1);
        Deck2 = findViewById(R.id.Deck2);
        Deck3 = findViewById(R.id.Deck3);
        Deck4 = findViewById(R.id.Deck4);

        Deck5 = findViewById(R.id.Deck5);
        Deck6 = findViewById(R.id.Deck6);
        Deck7 = findViewById(R.id.Deck7);
        Deck8 = findViewById(R.id.Deck8);

        Jouer1 = findViewById(R.id.Jouer1);
        Jouer2 = findViewById(R.id.Jouer2);
        Jouer3 = findViewById(R.id.Jouer3);
        Jouer4 = findViewById(R.id.Jouer4);

        Jouer1.setOnDragListener(drag);
        Jouer2.setOnDragListener(drag);
        Jouer3.setOnDragListener(drag);
        Jouer4.setOnDragListener(drag);

        Jouer1.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down));
        Jouer2.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down));
        Jouer3.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up));
        Jouer4.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up));



    }

    /*LinearLayout l = (LinearLayout) TimerPoint.getParent();
                l.removeView(TimerPoint);
                l.addView(TimerPoint);*/

    private Handler handler = new Handler();
    private Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            // Repeat apres 1000ms (SetTimout Js 👍 )
            //System.out.println("pass");
            try {
                Integer node = Integer.parseInt(TimerPoint.getText().toString());
                TimerPoint.setText(String.valueOf(node + 1));
                if(main.size() <= 6){
                    PigeLesCartes();
                }
            } catch (NumberFormatException e) {
                System.out.println("pass");
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // commencer le tick pour le timer
        handler.post(tickRunnable);


        // Création des 97 cartes
        cartes = new Vector<Carte>();
        // Creation des cartes
        for (int i = 1; i < 98; i++) {
            cartes.add(new Carte(i));
        }

        // Shuffle les cartes
        Collections.shuffle(cartes);

        main = new Vector<Carte>();
        //mettre 8 carte dans notre main
        for (int i = 0; i < 8; i++) {
            Carte CarteNode = cartes.elementAt(i);
            cartes.remove(i);
            main.add(CarteNode);
        }



        InitialisationJeu();


    }

    private void InitialisationJeu(){
        // Pour setup les couleurs des premiere carte et les changer plus tard
        deckIds = new int[]{R.id.Deck1, R.id.Deck2, R.id.Deck3, R.id.Deck4, R.id.Deck5, R.id.Deck6, R.id.Deck7, R.id.Deck8}; // Adjust if you have Jouer3 and Jouer4 defined
        deckLayouts = new LinearLayout[deckIds.length];

        // Pour dessiner les carte qui sont dans ma mains
        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            main.get(i).DessinerCarte(deckLayouts[i], context, drag);
        }

        // pour setup les couleur de fond et pouvoir les changer plus tard
        int[] fondIds = new int[]{R.id.Jeu, R.id.Deck, R.id.Score};
        LinearLayout[] fondLayout = new LinearLayout[fondIds.length];

        // Loop through each ID and initialize the corresponding LinearLayout
        for (int i = 0; i < fondIds.length; i++) {
            fondLayout[i] = findViewById(fondIds[i]);
            fondLayout[i].setBackground(getDrawable((R.drawable.fond_carte)));
        }

        // commencer le timer dans la section en haut
        StartTimer(fondLayout[2]);

    }

    private void StartTimer(LinearLayout LayoutScore){
        TimerPoint = new TextView(this);
        TimerPoint.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        TimerPoint.setGravity(Gravity.CENTER);
        TimerPoint.setText(String.valueOf(0)); // Assuming Carte class has a method getNumber()
        TimerPoint.setTextColor(Color.WHITE);  // Set text color that contrasts with the card image
        TimerPoint.setTextSize(22); // Set text size
        LayoutScore.addView(TimerPoint);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Arreter le tick
        handler.removeCallbacks(tickRunnable);
    }

    public class EcouteurDrag implements View.OnTouchListener,View.OnDragListener {

        @Override
        public boolean onTouch(View source, MotionEvent event) {
            // source = imageView / jeton à bouger
            // C'est l'ombre qu'on va transporter d'une colonne a l'autre
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(source);

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion <= 24){
                source.startDrag(null,shadowBuilder,source,0);
            }
            else
                source.startDragAndDrop(null,shadowBuilder,source,0);

            // on set l'object que l'on drag en mode invisible
            source.setVisibility(View.INVISIBLE);
            return true;
        }


        @Override
        public boolean onDrag(View source, DragEvent event) {
            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_ENTERED:
                    if(source.getTag().toString().equals("up")) {
                        source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up_selection));
                    }
                    else{
                        source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down_selection));
                    }
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    if(source.getTag().toString().equals("up")){
                        source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up));
                    }
                    else{
                        source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down));
                    }
                    break;

                case DragEvent.ACTION_DROP:
                    if (event.getLocalState() instanceof TextView && source instanceof LinearLayout) {
                        CarteNode = (TextView) event.getLocalState();
                        LinearLayout parent = (LinearLayout) CarteNode.getParent();
                        if (parent != null) {
                            parent.removeView(CarteNode);
                        }
                        LinearLayout container = (LinearLayout) source;
                        container.addView(CarteNode);
                        CarteNode.setVisibility(View.VISIBLE);
                        CarteNode.setOnTouchListener(drag);
                    }
                    else{
                        CarteNode = (TextView) event.getLocalState();
                        CarteNode.setVisibility(View.VISIBLE);
                        CarteNode.setText((CharSequence) CarteNode.getTag());
                        CarteNode.setOnTouchListener(drag);
                    }
                    break;
            }
            return true;
        }

    }

    private void PigeLesCartes(){
        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            main.get(i).DessinerCarte(deckLayouts[i], context, drag);
        }
    }

    private void jouerCarte(){

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