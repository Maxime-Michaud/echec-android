package nick.echec;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ClipDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import data.GestionnaireUtilisateurs;
import data.Utilisateur;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ChangementAlerte ca = new ChangementAlerte(); //classe gère le changement de pièces (kev)
    int cliqueX, cliqueY;   //ou le joueur a cliqué
    RelativeLayout layout;
    ArrayList<MoveListener> lstMoveListener;
    ImageView PN1, PN2, PN3, PN4, PN5, PN6, PN7, PN8;
    ImageView PB1, PB2, PB3, PB4, PB5, PB6, PB7, PB8;
    ImageView TN1, TN2, FN1, FN2, CN1, CN2, KN1, QN1;
    ImageView TB1, TB2, FB1, FB2, CB1, CB2, KB1, QB1;
    ArrayList<ImageView> pionsNoirs, pionsBlancs;
    RelativeLayout.LayoutParams params;
    Bitmap bitmap;
    Resources r;
    boolean[] premierTourPionsNoirs = new boolean[8];       //Si le pion peut bouger de 2 ou non
    boolean premierTourPionsBlancs[] = new boolean[8];      //Si le pion peut bouger de 2 ou non
    boolean tourBlanc = true;                               //Si c'est le tour des pions blanc ou non
    boolean estEnSelectionMouv = false;                     //Si le joueur est en sélection de la case à bouger le pion
    ArrayList<String> mouvDispos;                           //Les mouvements disponibles sans les mouvements qui tuent un pion
    ArrayList<String> mouvCauseMort;                        //Les mouvements disponible qui causent la mort d'un pion
    //Toutes les pièces et leur position; la lettre du nom de la pièce (Pion, Tour, Fou, Roi, (Q)Reine, (K)Roi), le no de la pièce, sa position Y, sa position X (c inversé mais jmen caliss je continue sur mon brainfart)
    String pieces[] = {"TN100", "CN101", "FN102", "KN103", "QN104", "FN205", "CN206", "TN207", "PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
            "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867", "TB170", "CB171", "FB172", "KB173", "QB174", "FB275", "CB276", "TB277"};
    AI newAI;
    //Les controlleurs de pièces
    Pion pion = new Pion();
    Fou fou = new Fou();
    Tour tour = new Tour();
    Reine reine = new Reine();
    Cheval cheval = new Cheval();
    Roi roi = new Roi();

    String pionRenduAuBoutte;                               //Le pion qui est rendu au bout
    String pionEnMouvement;                                 //Le pion qui est sélection pour un mouvement
    int nbPionBlancMort = 0;
    int nbPionNoirMort = 0;
    ArrayList<String> toRemove;                             //Les mouvement à enlever de l'array
    boolean attendreAnimation;
    long start_tour = System.currentTimeMillis();           //Le tempps au début d'un tour
    long start_time = System.currentTimeMillis();           //Le tempps au début de la partie
    Utilisateur utilisateur;
    Suggestion suggestion;


    /**
     *
     * @param bitmap L'image
     * @param posx la position en x commencant à 0 de la partie voulu
     * @param posy la position en y commencant à 0 de la partie voulu
     * @param nbImgLargeur Le nombre d'image en largeur
     * @param nbImageHauteur Le nombre d'image en hauteur
     * @return la partie de l'image découpé
     */
    public static Bitmap couperImage(Bitmap bitmap, int posx, int posy, int nbImgLargeur, int nbImageHauteur) {
        int topLx, topLy;
        topLx = posx * (bitmap.getWidth() / 6);
        topLy = posy * (bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, topLx, topLy, bitmap.getWidth() / nbImgLargeur, bitmap.getHeight() / nbImageHauteur);
    }

    /**
     * initialisations des paramètres
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestionnaireUtilisateurs.ajouter("Bob", "test");
        utilisateur = GestionnaireUtilisateurs.get("Bob");
        demarrer(savedInstanceState,utilisateur, 1, null);

    }
    protected void demarrer(Bundle savedInstanceState, Utilisateur utilisateur, int niveauAI, String pieceDepart[]) {

        if(!utilisateur.getUsername().equals("Bob"))
            super.onCreate(savedInstanceState);
        this.utilisateur = utilisateur;
        suggestion = new Suggestion((tourBlanc?'B':'N'));
        if(niveauAI == 0)
            newAI = new AI('N');
        else
            newAI = new AI2('N');
        lstMoveListener = new ArrayList<>();
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relative);
        Intent intent = getIntent();
        r = getResources();
        mouvDispos = new ArrayList<>();
        mouvCauseMort = new ArrayList<>();
        for(int i = 0;i < 8;i++)
        {
            premierTourPionsBlancs[i] = true;
            premierTourPionsNoirs[i] = true;
        }
        for (int j = 0; j < layout.getChildCount(); j++) {
            View v = layout.getChildAt(j);
            if (v instanceof Button) {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnClickListener(this);
            }
        }
        pionsNoirs = new ArrayList<>();

        //Les images des pions et leur tag pour les reconnaître dans d'autre fonction
        PN1 = new ImageView(this);
        PN1.setTag("PN1");
        PN2 = new ImageView(this);
        PN2.setTag("PN2");
        PN3 = new ImageView(this);
        PN3.setTag("PN3");
        PN4 = new ImageView(this);
        PN4.setTag("PN4");
        PN5 = new ImageView(this);
        PN5.setTag("PN5");
        PN6 = new ImageView(this);
        PN6.setTag("PN6");
        PN7 = new ImageView(this);
        PN7.setTag("PN7");
        PN8 = new ImageView(this);
        PN8.setTag("PN8");
        pionsNoirs.add(PN1);
        pionsNoirs.add(PN2);
        pionsNoirs.add(PN3);
        pionsNoirs.add(PN4);
        pionsNoirs.add(PN5);
        pionsNoirs.add(PN6);
        pionsNoirs.add(PN7);
        pionsNoirs.add(PN8);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawns);
        for (int i = 0; i < 8; i++) {
            setPawn(pionsNoirs.get(i),i,1,5,0);
        }
        pionsBlancs = new ArrayList<>();
        PB1 = new ImageView(this);
        PB1.setTag("PB1");
        PB2 = new ImageView(this);
        PB2.setTag("PB2");
        PB3 = new ImageView(this);
        PB3.setTag("PB3");
        PB4 = new ImageView(this);
        PB4.setTag("PB4");
        PB5 = new ImageView(this);
        PB5.setTag("PB5");
        PB6 = new ImageView(this);
        PB6.setTag("PB6");
        PB7 = new ImageView(this);
        PB7.setTag("PB7");
        PB8 = new ImageView(this);
        PB8.setTag("PB8");
        pionsBlancs.add(PB1);
        pionsBlancs.add(PB2);
        pionsBlancs.add(PB3);
        pionsBlancs.add(PB4);
        pionsBlancs.add(PB5);
        pionsBlancs.add(PB6);
        pionsBlancs.add(PB7);
        pionsBlancs.add(PB8);
        for (int i = 0; i < 8; i++) {
            setPawn(pionsBlancs.get(i),i,6,5,1);
        }
        TN1 = new ImageView(this);
        setPawn(TN1, 0, 0, 2, 0);
        TN1.setTag("TN1");
        TN2 = new ImageView(this);
        setPawn(TN2, 7, 0, 2, 0);
        TN2.setTag("TN2");
        FN1 = new ImageView(this);
        setPawn(FN1, 2, 0, 3, 0);
        FN1.setTag("FN1");
        FN2 = new ImageView(this);
        setPawn(FN2,5,0,3,0);
        FN2.setTag("FN2");
        CN1 = new ImageView(this);
        setPawn(CN1, 1, 0, 4, 0);
        CN1.setTag("CN1");
        CN2 = new ImageView(this);
        setPawn(CN2, 6, 0, 4, 0);
        CN2.setTag("CN2");
        KN1 = new ImageView(this);
        setPawn(KN1,3,0,1,0);
        KN1.setTag("KN1");
        QN1 = new ImageView(this);
        setPawn(QN1,4,0,0,0);
        QN1.setTag("QN1");
        
        TB1 = new ImageView(this);
        setPawn(TB1,0,7,2,1);
        TB1.setTag("TB1");
        TB2 = new ImageView(this);
        setPawn(TB2,7,7,2,1);
        TB2.setTag("TB2");
        FB1 = new ImageView(this);
        setPawn(FB1,2,7,3,1);
        FB1.setTag("FB1");
        FB2 = new ImageView(this);
        setPawn(FB2,5,7,3,1);
        FB2.setTag("FB2");
        CB1 = new ImageView(this);
        setPawn(CB1,1,7,4,1);
        CB1.setTag("CB1");
        CB2 = new ImageView(this);
        setPawn(CB2,6,7,4,1);
        CB2.setTag("CB2");
        KB1 = new ImageView(this);
        setPawn(KB1,3,7,1,1);
        KB1.setTag("KB1");
        QB1 = new ImageView(this);
        setPawn(QB1,4,7,0,1);
        QB1.setTag("QB1");
        start_time = System.currentTimeMillis();


        //recommencer();
        /*String piecesTemp[] = {"PN126", "PN227", "PN325", "PN413", "PN514", "PN615", "PN716","PN817",
                "PB120", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867"};*/
        if(pieceDepart == null)
            initialiserUneGille(pieces);
        else
            initialiserUneGille(pieceDepart);
    }

    /**
     *
     * @param img l'imageView a setter en tant que pion
     * @param posX la position en x du pion sur le jeu
     * @param posY la position en y du pion sur le jeu
     * @param imgX la position en x du sprite dans l'image, en partant de 0
     * @param imgY la position en y du sprite dans l'image, en partant de 0
     */
    public void setPawn(final ImageView img,int posX, int posY, int imgX, int imgY)
    {
        Bitmap bitmapTemp = couperImage(bitmap, imgX, imgY, 6, 2);
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8 + (37 * posX), r.getDisplayMetrics());
        int py = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108 + (37 * posY), r.getDisplayMetrics());
        int test180 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        params = new RelativeLayout.LayoutParams(test180, test180);
        img.setImageBitmap(bitmapTemp);
        params.leftMargin = px;
        params.topMargin = py;
        if(layout.indexOfChild(img) == -1)
            layout.addView(img, params);
    }

    /**
     * Sur le clique de n'importe quel case, va détecter quel type de pièce est sur la case et donner les mouvement possible
     * @param v
     */
    @Override
    public void onClick(View v) {
        cliqueY = Character.getNumericValue(v.getResources().getResourceEntryName(v.getId()).charAt(3));
        cliqueX = Character.getNumericValue(v.getResources().getResourceEntryName(v.getId()).charAt(4));
        if(!estEnSelectionMouv)
        {
            for(String s : pieces)
            {
                String caseClique = String.valueOf(cliqueY) + String.valueOf(cliqueX);
                String temp = String.valueOf(s.charAt(3)) + s.charAt(4);
                if (caseClique.equals(temp))
                {
                    String nomCouleur;
                    switch (s.charAt(1))
                    {
                        case 'N':
                            nomCouleur = "noir";
                            break;
                        case 'B':
                            nomCouleur = "blanc";
                            break;
                        default:
                            nomCouleur = "AUCUNECOULEUR";
                            break;
                    }
                    boolean valide = (tourBlanc && nomCouleur == "blanc") ||(!tourBlanc && nomCouleur == "noir");
                    if(valide)
                    {
                        String nomPiece;
                        switch (s.charAt(0))
                        {
                            case 'T':
                                nomPiece = "Tour";
                                tour.mouvement(cliqueX,cliqueY,mouvDispos);
                                break;
                            case 'C':
                                nomPiece = "Cheval";
                                cheval.mouvement(cliqueX,cliqueY,mouvDispos);
                                break;
                            case 'F':
                                nomPiece = "Fou";
                                fou.mouvement(cliqueX, cliqueY, mouvDispos);
                                break;
                            case 'Q':
                                nomPiece = "Reine";
                                reine.mouvement(cliqueX, cliqueY,mouvDispos);
                                break;
                            case 'K':
                                nomPiece = "Roi";
                                roi.mouvement(cliqueX, cliqueY,mouvDispos);
                                break;
                            case 'P':
                                boolean premierTour = false;
                                if(nomCouleur.equals("blanc") && premierTourPionsBlancs[Character.getNumericValue(s.charAt(2) - 1)])
                                {
                                    premierTour = true;
                                }
                                else if(nomCouleur.equals("noir") && premierTourPionsNoirs[Character.getNumericValue(s.charAt(2) - 1)])
                                {
                                    premierTour = true;
                                }
                                pion.mouvement(cliqueX,cliqueY,premierTour, s.charAt(1),mouvDispos);
                                break;
                            default:
                                nomPiece = "AUCUNNOM";
                                break;

                        }
                        if(mouvDispos.size() > 0)
                        {
                            pionEnMouvement = s;
                            validerLesCasesDispoPourMouvement();
                            dessinerMouvDispo();
                            estEnSelectionMouv = true;

                        }

                    }

                }
            }
        }
        else
        {
            finaliserTour(false);
            if((newAI.couleur == 'N' && !tourBlanc) || (newAI.couleur == 'B' && tourBlanc))
            {
                String newPos;
                StringBuilder temp = new StringBuilder();
                newPos = newAI.choisirPieceRandom(pieces, premierTourPionsNoirs, premierTourPionsBlancs, mouvDispos, mouvCauseMort, temp);
                pionEnMouvement = temp.toString();
                cliqueY = Character.getNumericValue(newPos.charAt(0));
                cliqueX = Character.getNumericValue(newPos.charAt(1));
                finaliserTour(true);

            }
        }

    }

    public void finaliserTour(boolean tourAi)
    {
        boolean mouvValide = false;
        boolean tuePion = false;
        String nomPionMort = "";
        if(!tourAi)
        {
            for(int i = 0; i < mouvDispos.size();i++)
            {
                if(!mouvValide && (Character.getNumericValue(mouvDispos.get(i).charAt(0)) == cliqueY && Character.getNumericValue(mouvDispos.get(i).charAt(1)) == cliqueX))
                {
                    mouvValide = true;
                }
                layout.removeViewAt(layout.getChildCount() -1);
            }
        }

        for(int i = 0; i < mouvCauseMort.size();i++)
        {
            if(!mouvValide && (Character.getNumericValue(mouvCauseMort.get(i).charAt(0)) == cliqueY && Character.getNumericValue(mouvCauseMort.get(i).charAt(1)) == cliqueX))
            {
                mouvValide = true;
                //tuePion = true;
                if(tourBlanc)
                {
                    nomPionMort = "N";
                    nbPionNoirMort++;
                }

                else
                {
                    nomPionMort = "B";
                    nbPionBlancMort++;
                }
                nomPionMort += Integer.toString(cliqueY);
                nomPionMort += Integer.toString(cliqueX);
                int j = 0;
                for(String s : pieces)
                {
                    String posTemp = Character.toString(s.charAt(1)) + Character.toString(s.charAt(3)) + Character.toString(s.charAt(4));
                    if(posTemp.equals(nomPionMort))
                    {
                        suggestion.prendreStatPionMort(System.currentTimeMillis(), s);
                        if(s.charAt(0) == 'K')
                            partieTerminer(s, this);
                        final String npm = Character.toString(nomPionMort.charAt(0));
                        Thread t1 = new Thread(new Runnable() {
                            public void run() {
                                bougerPiece(s,npm, false);
                            }
                        });
                        t1.start();
                        while(t1.isAlive());

                        List<String> list = new ArrayList<String>(Arrays.asList(pieces));
                        list.removeAll(Arrays.asList(s));
                        pieces = list.toArray(new String[list.size()]);
                        pieces = list.toArray(pieces);
                        break;
                    }
                    j++;
                }
            }
            if(!tourAi)
                layout.removeViewAt(layout.getChildCount() -1);
        }
        mouvDispos.clear();
        mouvCauseMort.clear();
        if(mouvValide || tourAi)
        {
            int k = 0;
            for(String string: pieces)
            {
                if (pionEnMouvement.equals(string))
                {
                    String nouvelPos = Integer.toString(cliqueY) + Integer.toString(cliqueX);
                    String nomPiece = Character.toString(string.charAt(0)) + Character.toString(string.charAt(1)) + Character.toString(string.charAt(2));
                    final String npm = nouvelPos;
                    Thread t2 = new Thread(new Runnable() {
                        public void run() {
                            bougerPiece(string,npm, false);
                        }
                    });
                    t2.start();
                    while(t2.isAlive());
                    bougerPiece(string, nouvelPos, false);
                    pieces[k] = nomPiece +nouvelPos;
                    if(pionEnMouvement.charAt(0) == 'P' && pionEnMouvement.charAt(1) == 'B')
                    {
                        premierTourPionsBlancs[Character.getNumericValue(pionEnMouvement.charAt(2) -1)] = false;
                    }
                    else if(pionEnMouvement.charAt(0) == 'P' && pionEnMouvement.charAt(1) == 'N')
                    {
                        premierTourPionsNoirs[Character.getNumericValue(pionEnMouvement.charAt(2) -1)] = false;
                    }
                    suggestion.prendreTempsPasseTour(System.currentTimeMillis() - start_tour,(tourBlanc?'B':'N'));
                    start_tour = System.currentTimeMillis();
                    if(pionEnMouvement.charAt(0) == 'P' && pionEnMouvement.charAt(1) == 'B' && nouvelPos.charAt(0) == '0')
                    {
                        //TODO MENU CHOIX
                        pionRenduAuBoutte = pionEnMouvement;
                        choixPiece(pieces[k]);
                    }
                    if(pionEnMouvement.charAt(0) == 'P' && pionEnMouvement.charAt(1) == 'N' && nouvelPos.charAt(0) == '7')
                    {
                        pionRenduAuBoutte = pionEnMouvement;
                        //TODO MENU CHOIX
                        choixPiece(pieces[k]);
                    }
                    break;

                }
                k++;
            }

            if(tourBlanc)
                layout.setBackgroundColor(Color.BLACK);
            else
                layout.setBackgroundColor(Color.WHITE);
            tourBlanc= !tourBlanc;
            //Toast.makeText(this, pionEnMouvement + " vers " + Integer.toString(cliqueY) + Integer.toString(cliqueX), Toast.LENGTH_LONG).show();

        }
        estEnSelectionMouv = false;
        pionEnMouvement = "";
    }
    public void bougerPiece(String nomPiece, String nouvelPos, boolean initialisation)
    {
        //TODO mettre sa dans un thread quon peut faire un while pour attendre que les pièces aient finis de bouger pour la fct recommencer()

        int anciennePosX, anciennePosY;
        if(!initialisation)
            anciennePosX = Character.getNumericValue(nomPiece.charAt(4));
        else
            anciennePosX = 4;
        if(!initialisation)
            anciennePosY = Character.getNumericValue(nomPiece.charAt(3));
        else
            anciennePosY = (nomPiece.charAt(1) == 'B'?8:-1);
        int nouvPosX, nouvPosY;
        if(nouvelPos.equals("B"))
        {
            nouvPosX = 0;
            nouvPosY = 8;
        }
        else if(nouvelPos.equals("N"))
        {
            nouvPosX = 0;
            nouvPosY = -1;
        }
        else
        {
            nouvPosX = Character.getNumericValue(nouvelPos.charAt(1));
            nouvPosY = Character.getNumericValue(nouvelPos.charAt(0));
        }


        int mouvX = nouvPosX-anciennePosX;
        int mouvY = nouvPosY-anciennePosY;
        String nomImgView = Character.toString(nomPiece.charAt(0)) + Character.toString(nomPiece.charAt(1)) + Character.toString(nomPiece.charAt(2));
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (nouvelPos.equals("N")?(37 * mouvX) + 10* nbPionNoirMort:(nouvelPos.equals("B")?(37 * mouvX) + 10 * nbPionBlancMort:37 * mouvX)), r.getDisplayMetrics());
        int py = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 37 * mouvY, r.getDisplayMetrics());
        for(int i = 0; i < layout.getChildCount();i++)
        {
            View v = layout.getChildAt(i);
            attendreAnimation = true;
            if (v instanceof ImageView && v.getTag().equals(nomImgView)) {
                v.animate().translationX(v.getTranslationX() + px).setDuration(500);
                v.animate().translationY(v.getTranslationY() + py).setDuration(500);
                /*long start_time = System.currentTimeMillis();
                long wait_time = 1000;
                long end_time = start_time + wait_time;

                while (System.currentTimeMillis() < end_time) {
                    //..
                }*/
                break;
            }
        }
    }

    /**
     * Valide les cases disponibles selon le contenu de mouvDispos,
     * A = un mouvement de pion en diagonal
     * B = un mouvement de pion en ligne droite
     * Les lettres indiquent comment le déplacement se trace:
     * H haut, B bas, D diagonal
     */
    public void validerLesCasesDispoPourMouvement()
    {
        toRemove = new ArrayList<>();
        Iterator<String> it = mouvDispos.iterator();
        while(it.hasNext())
        {
            String s = it.next();
            if(toRemove.contains(s))
            {
                continue;
            }
            String mouvDispo = Character.toString(s.charAt(0)) + Character.toString(s.charAt(1)); //les 2 chiffre du mouvement disponible ex: 54
            if (s.charAt(2) == 'A')
            {
                for(String s2 : pieces)
                {
                    String posTemp = Character.toString(s2.charAt(3)) + Character.toString(s2.charAt(4));
                    if (mouvDispo.equals(posTemp))
                    {
                        if(pionEnMouvement.charAt(1) != s2.charAt(1))
                            mouvCauseMort.add(s);
                    }
                }
                toRemove.add(s);
                continue;
            }
            for(String s2 : pieces)
            {
                String posTemp = Character.toString(s2.charAt(3)) + Character.toString(s2.charAt(4));
                if(mouvDispo.equals(posTemp))
                {
                    if(s.charAt(2) != 'C')
                    {
                        if(pionEnMouvement.charAt(1) != s2.charAt(1))
                        {
                            if(pionEnMouvement.charAt(0) != 'P')
                                mouvCauseMort.add(s);
                            if(s.charAt(2) != 'E' && s.charAt(2) != 'K')
                                enleverMouvApres(s, false);
                        }
                        else
                        {
                            if(s.charAt(2) != 'E' && s.charAt(2) != 'K')
                                enleverMouvApres(s, true);
                        }
                        it.remove();
                        break;
                    }
                    else
                    {
                        if(pionEnMouvement.charAt(1) != s2.charAt(1))
                        {
                            enleverMouvApres(s, true);
                        }
                    }

                }
            }
        }
        for(String s : toRemove)
        {
            mouvDispos.remove(s);
        }
        if(mouvDispos.size() == 0)
        {
            estEnSelectionMouv=false;
        }
    }

    public void enleverMouvApres(String mouv, boolean caseActuel)
    {
        int posDepartX = Character.getNumericValue(mouv.charAt(1));
        int posDepartY = Character.getNumericValue(mouv.charAt(0));
        int aSupprimerX = 0;
        int aSupprimerY = 0;
        int posX, posY;             //La position a détruire
        switch (mouv.charAt(2))
        {
            case 'H':
                aSupprimerY = -1;
                break;
            case 'B':
                aSupprimerY = 1;
                break;
            case 'G':
                aSupprimerX = -1;
                break;
            case 'D':
                aSupprimerX = 1;
                break;
            case 'X':
                switch (mouv.charAt(3))
                {
                    case 'H':
                        aSupprimerY = -1;
                        break;
                    case 'B':
                        aSupprimerY = 1;
                        break;
                    default:
                        break;
                }
                switch (mouv.charAt(4))
                {
                    case 'G':
                        aSupprimerX = -1;
                        break;
                    case 'D':
                        aSupprimerX = 1;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        if(caseActuel)
        {
            posX = posDepartX;
            posY = posDepartY;
        }
        else
        {
            posX =posDepartX + aSupprimerX;
            posY =posDepartY + aSupprimerY;
        }

        while((posX >= 0 && posX <= 7) && (posY >= 0 && posY <= 7))
        {
            for(String s : mouvDispos)
            {
                int posTempY = Character.getNumericValue(s.charAt(0));
                int posTempX = Character.getNumericValue(s.charAt(1));
                if (posX == posTempX && posY == posTempY)
                {
                    toRemove.add(s);
                    break;
                }

            }

            if(mouv.charAt(2) == 'C')
                posX = 8;
            posX += aSupprimerX;
            posY += aSupprimerY;
        }
    }

    public void dessinerMouvDispo()
    {
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.square);
        Bitmap bitmapTemp = couperImage(bitmap2, 1, 0, 6, 1);
        int test180 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
        for(String s :mouvDispos)
        {
            ImageView img = new ImageView(this);
            img.setImageBitmap(bitmapTemp);
            params = new RelativeLayout.LayoutParams(test180, test180);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14 + (37 * Character.getNumericValue(s.charAt(1))), r.getDisplayMetrics());
            int py = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 112 + (37 * Character.getNumericValue(s.charAt(0))), r.getDisplayMetrics());
            params.leftMargin = px;
            params.topMargin = py;
            layout.addView(img, params);
        }
        for(String s :mouvCauseMort)
        {
            bitmapTemp = couperImage(bitmap2, 0, 0, 6, 1);
            ImageView img = new ImageView(this);
            img.setImageBitmap(bitmapTemp);
            params = new RelativeLayout.LayoutParams(test180, test180);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14 + (37 * Character.getNumericValue(s.charAt(1))), r.getDisplayMetrics());
            int py = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 112 + (37 * Character.getNumericValue(s.charAt(0))), r.getDisplayMetrics());
            params.leftMargin = px;
            params.topMargin = py;
            layout.addView(img, params);
        }

    }

    public void partieTerminer(String s, final Context c)
    {
        Toast.makeText(this, "La partie est terminé, le roi mort est " + s, Toast.LENGTH_LONG).show();
        new AlertDialog.Builder(this)
                .setTitle("Partie terminé")
                .setMessage("Voulez-vous recommencer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        recommencer();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(c,"Vous pouvez recommencer en cliquant sur l'option dans le menu", Toast.LENGTH_LONG).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        ArrayList<String> suggestions = suggestion.genererEnvoyerSuggestions(utilisateur);
    }

    public void initialiserUneGille(String tousPieces[])
    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawns);
        pieces = tousPieces;
        suggestion= new Suggestion('B');
        for(int i = 0;i < 8;i++)
        {
            premierTourPionsBlancs[i] = true;
            premierTourPionsNoirs[i] = true;
        }

        lstMoveListener = new ArrayList<>();
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relative);
        r = getResources();
        mouvDispos = new ArrayList<>();
        mouvCauseMort = new ArrayList<>();
        for (int j = 0; j < layout.getChildCount(); j++) {
            View v = layout.getChildAt(j);
            if (v instanceof Button) {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnClickListener(this);
            }
        }

        pionsNoirs = new ArrayList<>();

        //Les images des pions et leur tag pour les reconnaître dans d'autre fonction
        pionsNoirs.add(PN1);
        pionsNoirs.add(PN2);
        pionsNoirs.add(PN3);
        pionsNoirs.add(PN4);
        pionsNoirs.add(PN5);
        pionsNoirs.add(PN6);
        pionsNoirs.add(PN7);
        pionsNoirs.add(PN8);
        int test = 0;
        for(String s : tousPieces)
        {
            int couleur = (s.charAt(1) == 'B'?1:0);
            int pion = (s.charAt(0) == 'P'?5:s.charAt(0) == 'C'?4:s.charAt(0) == 'F'?3:s.charAt(0) == 'T'?2:s.charAt(0) == 'K'?1:0);
            int posX = 4;
            int posY = (s.charAt(1) == 'B'?8:-1);
            ImageView image = new ImageView(this);
            image.setTag(Character.toString(s.charAt(0)) + Character.toString(s.charAt(1)) + Character.toString(s.charAt(2)));
            setPawn(image, posX, posY, pion, couleur);
            String nouvelPos = Character.toString(s.charAt(3)) + Character.toString(s.charAt(4));
            bougerPiece(s, nouvelPos, true);
        }
    }

    public void addMoveListener(MoveListener ml)
    {
        lstMoveListener.add(ml);
    }

    public void doMove()
    {
        for(MoveListener v : lstMoveListener)
        {
            v.onMove();
        }
    }

    public void recommencer()
    {
        lstMoveListener = new ArrayList<>();
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relative);
        r = getResources();
        mouvDispos = new ArrayList<>();
        mouvCauseMort = new ArrayList<>();
        for(int i = 0;i < 8;i++)
        {
            premierTourPionsBlancs[i] = true;
            premierTourPionsNoirs[i] = true;
        }
        for (int j = 0; j < layout.getChildCount(); j++) {
            View v = layout.getChildAt(j);
            if (v instanceof Button) {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnClickListener(this);
            }
        }
        layout.setBackgroundColor(Color.WHITE);
        tourBlanc= true;
        pionsNoirs = new ArrayList<>();
        String piecesTemp[] = {"TN100", "CN101", "FN102", "KN103", "QN104", "FN205", "CN206", "TN207", "PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
            "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867", "TB170", "CB171", "FB172", "KB173", "QB174", "FB275", "CB276", "TB277"};
        /*String piecesTemp[] = {"PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
                "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867"};*/
        pieces = piecesTemp;
        //Les images des pions et leur tag pour les reconnaître dans d'autre fonction
        PN1 = new ImageView(this);
        PN1.setTag("PN1");
        PN2 = new ImageView(this);
        PN2.setTag("PN2");
        PN3 = new ImageView(this);
        PN3.setTag("PN3");
        PN4 = new ImageView(this);
        PN4.setTag("PN4");
        PN5 = new ImageView(this);
        PN5.setTag("PN5");
        PN6 = new ImageView(this);
        PN6.setTag("PN6");
        PN7 = new ImageView(this);
        PN7.setTag("PN7");
        PN8 = new ImageView(this);
        PN8.setTag("PN8");
        pionsNoirs.add(PN1);
        pionsNoirs.add(PN2);
        pionsNoirs.add(PN3);
        pionsNoirs.add(PN4);
        pionsNoirs.add(PN5);
        pionsNoirs.add(PN6);
        pionsNoirs.add(PN7);
        pionsNoirs.add(PN8);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawns);
        for (int i = 0; i < 8; i++) {
            setPawn(pionsNoirs.get(i),i,1,5,0);
        }
        pionsBlancs = new ArrayList<>();
        PB1 = new ImageView(this);
        PB1.setTag("PB1");
        PB2 = new ImageView(this);
        PB2.setTag("PB2");
        PB3 = new ImageView(this);
        PB3.setTag("PB3");
        PB4 = new ImageView(this);
        PB4.setTag("PB4");
        PB5 = new ImageView(this);
        PB5.setTag("PB5");
        PB6 = new ImageView(this);
        PB6.setTag("PB6");
        PB7 = new ImageView(this);
        PB7.setTag("PB7");
        PB8 = new ImageView(this);
        PB8.setTag("PB8");
        pionsBlancs.add(PB1);
        pionsBlancs.add(PB2);
        pionsBlancs.add(PB3);
        pionsBlancs.add(PB4);
        pionsBlancs.add(PB5);
        pionsBlancs.add(PB6);
        pionsBlancs.add(PB7);
        pionsBlancs.add(PB8);
        for (int i = 0; i < 8; i++) {
            setPawn(pionsBlancs.get(i),i,6,5,1);
        }
        TN1 = new ImageView(this);
        setPawn(TN1, 0, 0, 2, 0);
        TN1.setTag("TN1");
        TN2 = new ImageView(this);
        setPawn(TN2, 7, 0, 2, 0);
        TN2.setTag("TN2");
        FN1 = new ImageView(this);
        setPawn(FN1, 2, 0, 3, 0);
        FN1.setTag("FN1");
        FN2 = new ImageView(this);
        setPawn(FN2, 5, 0, 3, 0);
        FN2.setTag("FN2");
        CN1 = new ImageView(this);
        setPawn(CN1, 1, 0, 4, 0);
        CN1.setTag("CN1");
        CN2 = new ImageView(this);
        setPawn(CN2, 6, 0, 4, 0);
        CN2.setTag("CN2");
        KN1 = new ImageView(this);
        setPawn(KN1, 3, 0, 1, 0);
        KN1.setTag("KN1");
        QN1 = new ImageView(this);
        setPawn(QN1, 4, 0, 0, 0);
        QN1.setTag("QN1");

        TB1 = new ImageView(this);
        setPawn(TB1, 0, 7, 2, 1);
        TB1.setTag("TB1");
        TB2 = new ImageView(this);
        setPawn(TB2, 7, 7, 2, 1);
        TB2.setTag("TB2");
        FB1 = new ImageView(this);
        setPawn(FB1, 2, 7, 3, 1);
        FB1.setTag("FB1");
        FB2 = new ImageView(this);
        setPawn(FB2, 5, 7, 3, 1);
        FB2.setTag("FB2");
        CB1 = new ImageView(this);
        setPawn(CB1, 1, 7, 4, 1);
        CB1.setTag("CB1");
        CB2 = new ImageView(this);
        setPawn(CB2, 6, 7, 4, 1);
        CB2.setTag("CB2");
        KB1 = new ImageView(this);
        setPawn(KB1, 3, 7, 1, 1);
        KB1.setTag("KB1");
        QB1 = new ImageView(this);
        setPawn(QB1, 4, 7, 0, 1);
        QB1.setTag("QB1");
        start_time = start_tour = System.currentTimeMillis();
        suggestion = new Suggestion('B');
    }

    /**
     * Méthode qui affiche un alertDialog qui permet au joueur de choisir la pièce qui va
     * remplacer le pion, quand celui-çi à atteind l'auttre bout du plateau.
     * @param pion string qui représente le pion a changer.
     */
    public void choixPiece(String pion) {
        pionRenduAuBoutte = pion;
        if (!tourBlanc)
        {
            char choix[] = new char[2];
            choix[0] = 'Q';
            int i = 0;
            for (String s : pieces)
            {
                if (pionRenduAuBoutte.equals(s))
                {
                    String temp = choix[0] + Character.toString(s.charAt(1)) + Character.toString(s.charAt(2)) + Character.toString(s.charAt(3)) + Character.toString(s.charAt(4));
                    pieces[i] = temp;
                    String tag = Character.toString(pionRenduAuBoutte.charAt(0)) + Character.toString(pionRenduAuBoutte.charAt(1)) + Character.toString(pionRenduAuBoutte.charAt(2));

                    for (int j = 0; j < layout.getChildCount(); j++) {
                        View vw = layout.getChildAt(j);
                        if (vw instanceof ImageView && layout.getChildAt(j).getTag().equals(tag)) {
                            String tagtemp = Character.toString(choix[0]) + Character.toString(tag.charAt(1)) + Character.toString(tag.charAt(2));
                            layout.getChildAt(j).setTag(tagtemp);
                            setPawn((ImageView) vw, Character.getNumericValue(temp.charAt(4)), Character.getNumericValue(temp.charAt(3)), 0, (tourBlanc ? 1 : 0));
                        }
                    }
                }
                i++;
            }
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alerte_changement, null);

        //Création de l'AlertDialog
        AlertDialog alert = new AlertDialog.Builder(this).create();

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        alert.setView(alertDialogView);

        ImageView tour = (ImageView)alertDialogView.findViewById(R.id.btnTour);
        ImageView cavalier = (ImageView)alertDialogView.findViewById(R.id.btnCheval);
        ImageView fou = (ImageView)alertDialogView.findViewById(R.id.btnFou);
        ImageView reine = (ImageView)alertDialogView.findViewById(R.id.btnReine);
        Button ok = (Button)alertDialogView.findViewById(R.id.btnACok);

        tour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ca.setChoix('T');
                ca.setConfirme(true);
                tour.setBackgroundResource(R.drawable.tour_select);
                cavalier.setBackgroundResource(R.drawable.cheval);
                fou.setBackgroundResource(R.drawable.fou);
                reine.setBackgroundResource(R.drawable.reine);
            }
        });

        cavalier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.setChoix('C');
                ca.setConfirme(true);
                tour.setBackgroundResource(R.drawable.tour);
                cavalier.setBackgroundResource(R.drawable.cheval_select);
                fou.setBackgroundResource(R.drawable.fou);
                reine.setBackgroundResource(R.drawable.reine);
            }
        });

        fou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.setChoix('F');
                ca.setConfirme(true);
                tour.setBackgroundResource(R.drawable.tour);
                cavalier.setBackgroundResource(R.drawable.cheval);
                fou.setBackgroundResource(R.drawable.fou_select);
                reine.setBackgroundResource(R.drawable.reine);
            }
        });

        reine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.setChoix('Q');
                ca.setConfirme(true);
                tour.setBackgroundResource(R.drawable.tour);
                cavalier.setBackgroundResource(R.drawable.cheval);
                fou.setBackgroundResource(R.drawable.fou);
                reine.setBackgroundResource(R.drawable.reine_select);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ca.getConfirme()){
                    char[] choix= new char[7];
                    choix = pionRenduAuBoutte.toCharArray();
                    choix[0] = ca.getChoix();
                    int i = 0;

                    //Recherche le tag du pion actuelle pour changer son tag et lui attribuer l'image
                    //correspondant au nouveau tag. Exemple: TB5 = Cinquième tour blanche.
                    for (String s: pieces){
                        if (pionRenduAuBoutte.equals(s)){
                            String temp = choix[0]+Character.toString(s.charAt(1))+Character.toString(s.charAt(2))+Character.toString(s.charAt(3))+Character.toString(s.charAt(4));
                            pieces[i] = temp;
                            String tag = Character.toString(pionRenduAuBoutte.charAt(0))+Character.toString(pionRenduAuBoutte.charAt(1))+Character.toString(pionRenduAuBoutte.charAt(2));

                            for(int j=0;j<layout.getChildCount();j++)
                            {
                                View vw = layout.getChildAt(j);
                                if (vw instanceof ImageView && layout.getChildAt(j).getTag().equals(tag))
                                {
                                    String tagtemp = Character.toString(choix[0])+ Character.toString(tag.charAt(1))+ Character.toString(tag.charAt(2));
                                    layout.getChildAt(j).setTag(tagtemp);
                                    setPawn((ImageView)vw,Character.getNumericValue(temp.charAt(4)),Character.getNumericValue(temp.charAt(3)),(choix[0] == 'Q'?0:(choix[0] == 'T'?2:choix[0] == 'F'?3:4)),(tourBlanc?1:0));
                                }
                            }
                            break;
                        }
                        i++;
                    }
                    tour.setBackgroundResource(R.drawable.tour);
                    cavalier.setBackgroundResource(R.drawable.cheval);
                    fou.setBackgroundResource(R.drawable.fou);
                    reine.setBackgroundResource(R.drawable.reine);
                    alert.dismiss();
                }
            }
        });
        alert.show();
    }
}
