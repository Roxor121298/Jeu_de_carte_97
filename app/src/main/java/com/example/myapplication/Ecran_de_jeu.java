/*package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

    int pointage;

    Drawable dessiNode;

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
                    for (int i = 0; i < 2; i++) {
                        Carte CarteNode = cartes.elementAt(i);
                        cartes.remove(i);
                        main.add(CarteNode);
                    }
                    PigeLesCartes();
                }
                if(cartes.size() == 0){
                    FindelaPartie();
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
            dessiNode = getApplicationContext().getDrawable(R.drawable.carte);
            main.get(i).DessinerCarte(deckLayouts[i], context, drag,dessiNode);
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


    public class EcouteurDrag implements View.OnTouchListener, View.OnDragListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onDrag(View source, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    handleDragEntered(source);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    handleDragExited(source);
                    break;

                case DragEvent.ACTION_DROP:
                    handleDrop(event, source);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    handleDragEnded(source);
                    break;
            }
            return true;
        }

        private void handleDragEntered(View source) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up_selection));
            } else {
                source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down_selection));
            }
        }

        private void handleDragExited(View source) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up));
            } else {
                source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down));
            }
        }

        private void handleDrop(DragEvent event, View source) {
            if (event.getLocalState() instanceof TextView && source instanceof LinearLayout) {
                TextView droppedCard = (TextView) event.getLocalState();
                LinearLayout parent = (LinearLayout) droppedCard.getParent();
                if (parent != null) {
                    removeCarte(droppedCard);
                    parent.removeView(droppedCard);
                }
                LinearLayout container = (LinearLayout) source;

                if (validateMove(droppedCard, container)) {
                    if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                        droppedCard.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up));
                    } else {
                        droppedCard.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down));
                    }
                    container.addView(droppedCard);
                    updateGameState(droppedCard, container);
                } else {
                    parent.addView(droppedCard);
                }
                droppedCard.setVisibility(View.VISIBLE);
                droppedCard.setOnTouchListener(this);
            }
        }

        private void removeCarte(TextView droppedCard){
            for(int i =0; i<main.size();i++){
                Carte node = main.get(i);
                if(droppedCard.getText().toString().equals(String.valueOf(node.getNb()))){
                    main.remove(i);
                }
            }
        }

        private void handleDragEnded(View source) {
            source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_up));
            source.setBackground(Ecran_de_jeu.this.getDrawable(R.drawable.pyramide_down));
        }

        private boolean validateMove(TextView cardView, LinearLayout container) {
            int cardValue = Integer.parseInt(cardView.getText().toString());
            String containerTag = (String) container.getTag();

            // Get the top card value of the pile
            int topCardValue = getTopCardValue(container);

            // Check move validity based on pile type
            if (containerTag.equals("up1") || containerTag.equals("up2")) {
                // Ascending piles
                if (cardValue > topCardValue || cardValue == topCardValue - 10) {
                    return true;
                }
            } else if (containerTag.equals("down1") || containerTag.equals("down2")) {
                // Descending piles
                if (cardValue < topCardValue || cardValue == topCardValue + 10) {
                    return true;
                }
            }

            // Invalid move
            return false;
        }

        // Helper method to get the top card value of the pile
        private int getTopCardValue(LinearLayout container) {
            int childCount = container.getChildCount();
            if (childCount == 0) {
                // Return the initial value of the pile if it is empty
                if (container.getTag().equals("up1") || container.getTag().equals("up2")) {
                    return 0;
                } else if (container.getTag().equals("down1") || container.getTag().equals("down2")) {
                    return 98;
                }
            } else {
                // Get the value of the top card in the pile
                TextView topCardView = (TextView) container.getChildAt(childCount - 1);
                return Integer.parseInt(topCardView.getText().toString());
            }
            return -1; // This should never happen
        }

        private void updateGameState(TextView cardView, LinearLayout container) {
            int cardValue = (int) cardView.getTag();
            String containerTag = (String) container.getTag();

            // Update the score, remaining cards, etc.
            // Example: Update the TextView displaying the score
        }
    }


    private void PigeLesCartes(){
        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            dessiNode = this.getDrawable(R.drawable.carte);
            main.get(i).DessinerCarte(deckLayouts[i], context, drag,dessiNode);
        }
    }

    private void jouerCarte(){

    }

    private void FindelaPartie(){

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
}*/

/*public class EcouteurDrag implements View.OnTouchListener,View.OnDragListener {

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

    }*/


package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class Ecran_de_jeu extends AppCompatActivity {

    private int[] deckIds;
    private int pointage;
    private Drawable dessiNode;
    private LinearLayout[] deckLayouts;
    private Vector<Carte> cartes;
    private Vector<Carte> main;
    private LinearLayout Deck, Jouer1, Jouer2, Jouer3, Jouer4;
    private LinearLayout Deck1, Deck2, Deck3, Deck4, Deck5, Deck6, Deck7, Deck8;
    private TextView CarteNode;
    private TextView TimerPoint;
    private EcouteurDrag drag;

    private Handler handler = new Handler();
    private Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimer();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupDragListeners();
    }

    private void initializeViews() {
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

        Jouer1.setBackground(getDrawable(R.drawable.pyramide_down));
        Jouer2.setBackground(getDrawable(R.drawable.pyramide_down));
        Jouer3.setBackground(getDrawable(R.drawable.pyramide_up));
        Jouer4.setBackground(getDrawable(R.drawable.pyramide_up));
    }

    private void setupDragListeners() {
        Jouer1.setOnDragListener(drag);
        Jouer2.setOnDragListener(drag);
        Jouer3.setOnDragListener(drag);
        Jouer4.setOnDragListener(drag);
    }

    private void updateTimer() {
        try {
            int node = Integer.parseInt(TimerPoint.getText().toString());
            TimerPoint.setText(String.valueOf(node + 1));
            if (main.size() <= 6) {
                for (int i = 0; i < 2; i++) {
                    Carte CarteNode = cartes.elementAt(i);
                    cartes.remove(i);
                    main.add(CarteNode);
                }
                PigeLesCartes();
            }
            if (cartes.size() == 0) {
                FindelaPartie();
            }
        } catch (NumberFormatException e) {
            System.out.println("pass");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(tickRunnable);

        initializeCards();
        InitialisationJeu();
    }

    private void initializeCards() {
        cartes = new Vector<>();
        for (int i = 1; i < 98; i++) {
            cartes.add(new Carte(i));
        }
        Collections.shuffle(cartes);

        main = new Vector<>();
        for (int i = 0; i < 8; i++) {
            Carte CarteNode = cartes.elementAt(i);
            cartes.remove(i);
            main.add(CarteNode);
        }
    }

    private void InitialisationJeu() {
        deckIds = new int[]{R.id.Deck1, R.id.Deck2, R.id.Deck3, R.id.Deck4, R.id.Deck5, R.id.Deck6, R.id.Deck7, R.id.Deck8};
        deckLayouts = new LinearLayout[deckIds.length];

        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            dessiNode = getApplicationContext().getDrawable(R.drawable.carte);
            main.get(i).DessinerCarte(deckLayouts[i], context, drag, dessiNode);
        }

        setupBackgroundColors();
        StartTimer(findViewById(R.id.Score));
    }

    private void setupBackgroundColors() {
        int[] fondIds = new int[]{R.id.Jeu, R.id.Deck, R.id.Score};
        LinearLayout[] fondLayout = new LinearLayout[fondIds.length];

        for (int i = 0; i < fondIds.length; i++) {
            fondLayout[i] = findViewById(fondIds[i]);
            fondLayout[i].setBackground(getDrawable(R.drawable.fond_carte));
        }
    }

    private void StartTimer(LinearLayout LayoutScore) {
        TimerPoint = new TextView(this);
        TimerPoint.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        TimerPoint.setGravity(Gravity.CENTER);
        TimerPoint.setText(String.valueOf(0));
        TimerPoint.setTextColor(Color.WHITE);
        TimerPoint.setTextSize(22);
        LayoutScore.addView(TimerPoint);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(tickRunnable);
    }

    public class EcouteurDrag implements View.OnTouchListener, View.OnDragListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        }

        @Override
        public boolean onDrag(View source, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    handleDragEntered(source);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    handleDragExited(source);
                    break;

                case DragEvent.ACTION_DROP:
                    handleDrop(event, source);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    handleDragEnded(source);
                    break;
            }
            return true;
        }

        private void handleDragEntered(View source) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(getDrawable(R.drawable.pyramide_up_selection));
            } else {
                source.setBackground(getDrawable(R.drawable.pyramide_down_selection));
            }
        }

        private void handleDragExited(View source) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(getDrawable(R.drawable.pyramide_up));
            } else {
                source.setBackground(getDrawable(R.drawable.pyramide_down));
            }
        }

        private void handleDrop(DragEvent event, View source) {
            if (event.getLocalState() instanceof TextView && source instanceof LinearLayout) {
                TextView droppedCard = (TextView) event.getLocalState();
                LinearLayout parent = (LinearLayout) droppedCard.getParent();
                if (parent != null) {
                    removeCarte(droppedCard);
                    parent.removeView(droppedCard);
                }
                LinearLayout container = (LinearLayout) source;

                if (validateMove(droppedCard, container)) {
                    if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                        droppedCard.setBackground(getDrawable(R.drawable.pyramide_up));
                    } else {
                        droppedCard.setBackground(getDrawable(R.drawable.pyramide_down));
                    }
                    container.removeAllViews();
                    container.addView(droppedCard);
                    updateGameState(droppedCard, container);
                } else {
                    parent.addView(droppedCard);
                }
                droppedCard.setVisibility(View.VISIBLE);
                droppedCard.setOnTouchListener(this);
            }
        }

        private void removeCarte(TextView droppedCard) {
            for (int i = 0; i < main.size(); i++) {
                Carte node = main.get(i);
                if (droppedCard.getText().toString().equals(String.valueOf(node.getNb()))) {
                    main.remove(i);
                }
            }
        }

        private void handleDragEnded(View source) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(getDrawable(R.drawable.pyramide_up));
            } else {
                source.setBackground(getDrawable(R.drawable.pyramide_down));
            }
        }

        private boolean validateMove(TextView cardView, LinearLayout container) {
            int cardValue = Integer.parseInt(cardView.getText().toString());
            String containerTag = (String) container.getTag();

            int topCardValue = getTopCardValue(container);

            if (containerTag.equals("up1") || containerTag.equals("up2")) {
                if (cardValue > topCardValue || cardValue == topCardValue - 10) {
                    return true;
                }
            } else if (containerTag.equals("down1") || containerTag.equals("down2")) {
                if (cardValue < topCardValue || cardValue == topCardValue + 10) {
                    return true;
                }
            }
            return false;
        }

        private int getTopCardValue(LinearLayout container) {
            int childCount = container.getChildCount();
            if (childCount == 0) {
                if (container.getTag().equals("up1") || container.getTag().equals("up2")) {
                    return 0;
                } else if (container.getTag().equals("down1") || container.getTag().equals("down2")) {
                    return 98;
                }
            } else {
                TextView topCardView = (TextView) container.getChildAt(childCount - 1);
                return Integer.parseInt(topCardView.getText().toString());
            }
            return -1;
        }

        private void updateGameState(TextView cardView, LinearLayout container) {
            // Update the score, remaining cards, etc.
        }
    }

    private void PigeLesCartes() {
        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            dessiNode = getDrawable(R.drawable.carte);
            main.get(i).DessinerCarte(deckLayouts[i], context, drag, dessiNode);
        }
    }

    private void jouerCarte() {
        // Game logic for playing a card
    }

    private void FindelaPartie() {
        // Logic to end the game
    }
}




