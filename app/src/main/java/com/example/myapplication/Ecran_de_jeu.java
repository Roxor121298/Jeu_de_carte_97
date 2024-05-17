
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
    private TextView remainingCardsTextView;
    private TextView scoreTextView;
    private int score, carteRestantes;

    Gestion_DB instance;


    private Handler handler = new Handler();
    private Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimer();
            if (main.size() <= 6) {
                for (int i = 0; i < 2; i++) {
                    Carte CarteNode = cartes.elementAt(i);
                    cartes.remove(i);
                    main.add(CarteNode);
                }
                PigeLesCartes();
            }
            if(main.size() == 0 && cartes.size() == 0){
                FindelaPartie();
            }
            // repart le timer dans 1000 milliseconde
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inittialisation des View (LinearLayout)
        initializeViews();

        // donne au View les Ecouteurs appropriés
        setupDragListeners();
    }

    private void initializeViews() {
        CarteNode = new TextView(this);
        Deck = findViewById(R.id.Deck);
        drag = new EcouteurDrag();


        // bind les LinearLayout Deck
        Deck1 = findViewById(R.id.Deck1);
        Deck2 = findViewById(R.id.Deck2);
        Deck3 = findViewById(R.id.Deck3);
        Deck4 = findViewById(R.id.Deck4);
        Deck5 = findViewById(R.id.Deck5);
        Deck6 = findViewById(R.id.Deck6);
        Deck7 = findViewById(R.id.Deck7);
        Deck8 = findViewById(R.id.Deck8);


        // bind les LinearLayout jouer
        Jouer1 = findViewById(R.id.Jouer1);
        Jouer2 = findViewById(R.id.Jouer2);
        Jouer3 = findViewById(R.id.Jouer3);
        Jouer4 = findViewById(R.id.Jouer4);


        //donne les imamge de base au LinearLayout
        Jouer1.setBackground(getDrawable(R.drawable.pyramide_down));
        Jouer2.setBackground(getDrawable(R.drawable.pyramide_down));
        Jouer3.setBackground(getDrawable(R.drawable.pyramide_up));
        Jouer4.setBackground(getDrawable(R.drawable.pyramide_up));

        ajouterValeurInitial();

    }

    private void setupDragListeners() {
        //set les drag Listenner pour les LinearLayout jouer
        Jouer1.setOnDragListener(drag);
        Jouer2.setOnDragListener(drag);
        Jouer3.setOnDragListener(drag);
        Jouer4.setOnDragListener(drag);
    }

    private void ajouterValeurInitial() {
        addTextViewToLayout(Jouer1, "98");
        addTextViewToLayout(Jouer2, "98");
        addTextViewToLayout(Jouer3, "1");
        addTextViewToLayout(Jouer4, "1");
    }

    private void addTextViewToLayout(LinearLayout layout, String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextColor(Color.WHITE);  // Set text color
        textView.setTextSize(20); // Set text size
        textView.setTag(Integer.parseInt(text));
        textView.setOnTouchListener(new EcouteurDrag());

        // Clear existing views and add the new TextView
        layout.removeAllViews();
        layout.addView(textView);
    }

    // update le tick
    private void updateTimer() {
            int node = Integer.parseInt(TimerPoint.getText().toString());
            TimerPoint.setText(String.valueOf( node + 1));
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = Gestion_DB.getInstance(getApplicationContext());
        instance.ouvrirConnexion();

        remainingCardsTextView = findViewById(R.id.remainingCardsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        score = 0;
        // commence le timer
        handler.post(tickRunnable);
        initializeCards();
        InitialisationJeu();
    }

    private void initializeCards() {

        // Initalize un vecteur pour tout les cartes qui seront jouer
        cartes = new Vector<>();
        for (int i = 2; i < 98; i++) {
            cartes.add(new Carte(i));
        }
        // shuffle les cartes de facona  ce que l'ordre d'appartition soit aléatoire
        Collections.shuffle(cartes);

        // Initialise la main du jouer (les 8 carte que le joueur peut jouer)
        // retire les 8 premier carte du packet principal
        main = new Vector<>();
        for (int i = 0; i < 8; i++) {
            Carte CarteNode = cartes.elementAt(i);
            cartes.remove(i);
            main.add(CarteNode);
        }
    }

    private void InitialisationJeu() {

        // Liste des ids possible (tous sont appelé deck avec une numero)
        deckIds = new int[]{R.id.Deck1, R.id.Deck2, R.id.Deck3, R.id.Deck4, R.id.Deck5, R.id.Deck6, R.id.Deck7, R.id.Deck8};

        // Liste des 8 LinearLayout qui sont utilisé pour la main
        deckLayouts = new LinearLayout[deckIds.length];

        // pour chaque carte dans la main dessiner uen carte dans le layout approprié
        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            dessiNode = getApplicationContext().getDrawable(R.drawable.carte);
            main.get(i).DessinerCarte(deckLayouts[i], context, drag, dessiNode);
        }

        // mettre la couleur de fond des LinearLayout Jeu, Deck et Score (les Linear Layout englobant les autres)
        setupBackgroundColors();

        // Dessiner le timer de maniere directe dans java
        DessinerTimer(findViewById(R.id.Score));
    }

    private void setupBackgroundColors() {
        // Liste des ids possible
        int[] fondIds = new int[]{R.id.Jeu, R.id.Deck, R.id.Score};
        // Liste des 3 Linear Layout
        LinearLayout[] fondLayout = new LinearLayout[fondIds.length];

        // pour chaque LinearLayout englobant mettre leur couleur selon ce drawable xml
        for (int i = 0; i < fondIds.length; i++) {
            fondLayout[i] = findViewById(fondIds[i]);
            fondLayout[i].setBackground(getDrawable(R.drawable.fond_carte));
        }
    }

    // Ajoute un Text View dans le LinearLayout Score
    private void DessinerTimer(LinearLayout LayoutScore) {
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


    // On Stop arreter le timer
    // Mettre la base de donnée ici
    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(tickRunnable);
        instance.fermerConnexion();
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
            // mettre l'image selectionné quand on passe dessus
            // avec un contour rouge comme on a fait dans l'annexe de drag and drop
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(getDrawable(R.drawable.pyramide_up_selection));
            } else {
                source.setBackground(getDrawable(R.drawable.pyramide_down_selection));
            }
        }

        // Enlever le contour rouge si on sors du drag and drop
        private void handleDragExited(View source) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                source.setBackground(getDrawable(R.drawable.pyramide_up));
            } else {
                source.setBackground(getDrawable(R.drawable.pyramide_down));
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private void handleDrop(DragEvent event, View source) {
            // si l'événement est bien un texteView et que la soruce est bien un LinearLayout
            if (event.getLocalState() instanceof TextView && source instanceof LinearLayout) {

                // l'événement correspond au textView que sur laquelle on a cliquer
                TextView droppedCard = (TextView) event.getLocalState();
                // le parent de la carte (Le LinearLayout dans laquelle le textView ce trouve)
                LinearLayout parent = (LinearLayout) droppedCard.getParent();
                // Le LinearLayout dans sur laquelle on veut dropper notre carte
                LinearLayout container = (LinearLayout) source;

                // On verifie que le drop est valide selon les regle du jeu et la valeur des cartes
                if (validateMove(droppedCard, container)) {
                    // Si le parent dans laquelle la carte ce trouve n'est pas null  on retire le layout
                    if (parent != null) {
                        // On dois retirer la carte de notre la main du joueur
                        removeCarte(droppedCard);
                        parent.removeView(droppedCard);
                    }
                    // on met le background au par default
                    handleDragDrop(container, droppedCard);
                    // Il faut retirer les ImageView présent dans le container avant d'en ajouter un nouveau
                    updateGameState(droppedCard, container);
                    container.removeAllViews();
                    //Il faut enlever l'écouteur pour droppedCard
                    droppedCard.setOnTouchListener(null);
                    // On ajoute le nouveau TextView
                    container.addView(droppedCard);
                }
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

        private void handleDragDrop(View source, TextView droppedCard) {
            if (source.getTag().toString().equals("up1") || source.getTag().toString().equals("up2")) {
                droppedCard.setBackground(getDrawable(R.drawable.pyramide_up));
            } else {
                droppedCard.setBackground(getDrawable(R.drawable.pyramide_down));
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

        // update le nombre de carte
        private void updateGameState(TextView cardView, LinearLayout container) {
            carteRestantes = cartes.size() + main.size() + 1;
            remainingCardsTextView.setText(" Cartes restantes : " + carteRestantes);

            // Update le Socre
            calculateAndUpdateScore(cardView, container);
        }

        private void calculateAndUpdateScore(TextView cardView, LinearLayout container) {
            int topCardValue = getTopCardValue(container);
            int cardValue = Integer.parseInt(cardView.getText().toString());

            System.out.println(cardValue);
            System.out.println(topCardValue);

            // Proximity score
            int proximityScore = Math.abs(cardValue - topCardValue);

            // Calculate the total score
            score += proximityScore;
            scoreTextView.setText("Score: " + score);
        }
    }

    private void PigeLesCartes() {
        for (int i = 0; i < main.size(); i++) {
            deckLayouts[i].removeAllViews();
            deckLayouts[i] = findViewById(deckIds[i]);
            Context context = this;
            dessiNode = getDrawable(R.drawable.carte);
            main.get(i).DessinerCarte(deckLayouts[i], context, drag, dessiNode);
        }
    }

    private void FindelaPartie() {

        int scoreFinal = Integer.valueOf(scoreTextView.getText().toString());
        int carteFinal = carteRestantes;
        int tempsFinal = Integer.valueOf(TimerPoint.getText().toString());
        instance.ajouterScore(scoreFinal,carteFinal,tempsFinal);

        Intent i = new Intent(getApplicationContext(), Ecran_de_jeu.class );
        startActivity(i);

    }
}




