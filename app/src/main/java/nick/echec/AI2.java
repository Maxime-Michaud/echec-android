package nick.echec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Nick on 2016-05-03.
 */

public class AI2 extends  AI{
    Pion pion = new Pion();
    Fou fou = new Fou();
    Tour tour = new Tour();
    Reine reine = new Reine();
    Cheval cheval = new Cheval();
    Roi roi = new Roi();
    char couleur;
    ArrayList<String> toRemove = new ArrayList<>();     //Les mouvement à enlever de l'array
    ArrayList<String> nomPieceMeilleurMouv = new ArrayList<>();
    ArrayList<Integer> scoreMeilleurMove = new ArrayList<>();
    ArrayList<Boolean> mouvVert = new ArrayList<>();
    public AI2(char couleurAI)
    {
        couleur = couleurAI;
    }

    /**
     * Choisi un pion a jouer en fonction de la meilleur piece à tuer ou sinon un piece au hasard
     * @param pieces    tous les pièces du jeux
     * @param pionNoir  les premiers tours des pion noir
     * @param pionBlanc les premiers tours des pion blancs
     * @param mouvDispos les mouvement disponible(vert)
     * @param mouvCauseMort     les mouvement qui cause la mort (en rouge)
     * @param pionEnMouvement   le pion en mouvement
     * @return
     */
    @Override
    public String choisirPieceRandom(final String[] pieces,final boolean pionNoir[],final boolean pionBlanc[],final ArrayList<String> mouvDispos,final ArrayList<String> mouvCauseMort,final StringBuilder pionEnMouvement)
    {

        mouvVert.clear();
        nomPieceMeilleurMouv.clear();
        scoreMeilleurMove.clear();
        toRemove.clear();
        ArrayList<String> piecesCouleurs = new ArrayList<>();
        ArrayList<String> piecesCouleursEnnemi = new ArrayList<>();
        for(String s : pieces)
        {
            if(s.charAt(1) == couleur)
            {
                piecesCouleurs.add(s);
            }
            else
            {
                piecesCouleursEnnemi.add(s);
            }
        }

        if(piecesCouleurs.isEmpty())
            return "AUCUNEPIECE";
        ArrayList<Integer> pointsMouvCauseMort = new ArrayList<>();
        ArrayList<Integer> pointsMouvVert = new ArrayList<>();
        int i = 0;
        ArrayList<String> meillMouvChaquePiece = new ArrayList<>();
        //Pour chaque pièce de la couleur de L'AI WTF
        for(String s : piecesCouleurs)
        {
            pionEnMouvement.append(pieces[i]);
            ajouterMouvDispo(pionEnMouvement.toString(), pionNoir, pionBlanc, mouvDispos);
            validerLesCasesDispoPourMouvement(pieces, mouvDispos, mouvCauseMort, pionEnMouvement.toString());
            //Pour chaque mouvement qui cause la mort, attribut des point à ce mouvement

            for (String move : mouvCauseMort)
            {
                int pointPieceQuiBouge = getPointagePieceQuiBouge(s);
                String pieceMorte = "";
                for(String pionMort : piecesCouleursEnnemi)
                {
                    if(pionMort.charAt(3) == move.charAt(0) && pionMort.charAt(4) == move.charAt(1))
                    {
                        int pointPionMange = getPointagePieceQuiBouge(pionMort);
                        pointsMouvCauseMort.add(pointPionMange*10 - pointPieceQuiBouge);
                        break;
                    }
                }

            }
            //Pour chaque mouvement vert, attribut des point à ce mouvement
            for (String move:mouvDispos)
            {
                int pointPieceQuiBouge = 1;
                String pieceMorte = "";
                String newPieces[] = pieces;

                List<String> list = new ArrayList<String>(Arrays.asList(newPieces));
                list.removeAll(Arrays.asList(s));
                newPieces = list.toArray(new String[list.size()]);
                newPieces = list.toArray(newPieces);

                ArrayList<String> piecesCouleursEnnemi2 = new ArrayList<>();
                for(String s3 : newPieces)
                {
                    if(s3.charAt(1) == couleur)
                    {
                        piecesCouleursEnnemi2.add(s3);
                    }
                }
                for(String pionBouge : piecesCouleursEnnemi2)
                {
                    String pionEnMouvement2 = pionBouge;
                    ArrayList<String>  mouvDispos2 = new ArrayList<>();
                    ArrayList<String>  mouvCauseMort2 = new ArrayList<>();
                    ajouterMouvDispo(pionEnMouvement2, pionNoir, pionBlanc, mouvDispos2);
                    validerLesCasesDispoPourMouvement(newPieces, mouvDispos2, mouvCauseMort2, pionEnMouvement2);
                    for(String mort : mouvCauseMort2)
                    {
                        for (String pieceAi: piecesCouleurs)
                        {
                            if(pieceAi.charAt(3) == mort.charAt(0) && pieceAi.charAt(4) == mort.charAt(1))
                            {
                                pointPieceQuiBouge -= 2 *getPointagePieceQuiBouge(pieceAi);
                            }
                        }

                    }

                }
                pointPieceQuiBouge -= getPointagePieceQuiBouge(pionEnMouvement.toString())/2;
                pointsMouvVert.add(pointPieceQuiBouge);
            }



            pionEnMouvement.delete(0, pionEnMouvement.capacity());
            i++;

            //regarde lequel move a eut le plus de point
            boolean mouvDispo = false;
            int meilleurScore = -999999999;
            int meilleurScorePos = -1;
            int j = 0;
            //regarde les points des mouvements qui cause la mort d'une pièce
            for(String pieceTemp : mouvCauseMort)
            {
                if(pointsMouvCauseMort.get(j) > meilleurScore)
                {
                    meilleurScorePos = j;
                    meilleurScore = pointsMouvCauseMort.get(j);
                }
                j++;
            }
            j = 0;
            //regarde les points des mouvements vert
            for(String pieceTemp : mouvDispos)
            {
                if(pointsMouvVert.get(j) > meilleurScore)
                {
                    meilleurScorePos = j;
                    meilleurScore = pointsMouvVert.get(j);
                    mouvDispo = true;
                }
                j++;
            }
            if(!mouvDispos.isEmpty() || !mouvCauseMort.isEmpty())
            {
                if(mouvDispo)
                {
                    meillMouvChaquePiece.add(mouvDispos.get(meilleurScorePos));
                    nomPieceMeilleurMouv.add(s);
                    scoreMeilleurMove.add(meilleurScore);
                    mouvVert.add(true);
                }
                else
                {
                    meillMouvChaquePiece.add(mouvCauseMort.get(meilleurScorePos));
                    nomPieceMeilleurMouv.add(s);
                    scoreMeilleurMove.add(meilleurScore);
                    mouvVert.add(false);
                }
            }
            mouvDispos.clear();
            mouvCauseMort.clear();

            pointsMouvCauseMort.clear();
            pointsMouvVert = new ArrayList<>();
        }
        piecesCouleurs = new ArrayList<>();
        piecesCouleursEnnemi.clear();
        int o = 0;
        int bestScore =-200;
        int bestPos = -1;
        String meilleurPiece = "";
        for(String s :meillMouvChaquePiece)
        {
            if(scoreMeilleurMove.get(o) > bestScore)
            {
                bestPos = o;
                bestScore = scoreMeilleurMove.get(o);
                meilleurPiece = nomPieceMeilleurMouv.get(o);
            }
            o++;
        }
        pionEnMouvement.delete(0, pionEnMouvement.capacity());
        pionEnMouvement.append(meilleurPiece);

        if(mouvVert.get(bestPos))
            mouvDispos.add(meillMouvChaquePiece.get(bestPos));
        else
            mouvCauseMort.add(meillMouvChaquePiece.get(bestPos));

        return meillMouvChaquePiece.get(bestPos);
    }

    /**
     * Ajoute les mouvements disponibles
     * @param s             //le pions en mouvement
     * passage par valeur pour pouvoir affecter les variables
     * @param pionNoir
     * @param pionBlanc
     * @param mouvDispos
     * @return un arraylist de mouvement disponibles
     */
    public ArrayList<String> ajouterMouvDispo(final String s,final boolean pionNoir[],final boolean pionBlanc[],final ArrayList<String> mouvDispos) {
        switch (s.charAt(0)) {
            case 'T':
                tour.mouvement(Character.getNumericValue(s.charAt(4)), Character.getNumericValue(s.charAt(3)), mouvDispos);
                break;
            case 'C':
                cheval.mouvement(Character.getNumericValue(s.charAt(4)), Character.getNumericValue(s.charAt(3)),mouvDispos);
                break;
            case 'F':
                fou.mouvement(Character.getNumericValue(s.charAt(4)), Character.getNumericValue(s.charAt(3)),mouvDispos);
                break;
            case 'Q':
                reine.mouvement(Character.getNumericValue(s.charAt(4)), Character.getNumericValue(s.charAt(3)),mouvDispos);
                break;
            case 'K':
                roi.mouvement(Character.getNumericValue(s.charAt(4)), Character.getNumericValue(s.charAt(3)),mouvDispos);
                break;
            case 'P':
                boolean premierTour = false;
                if (couleur == 'B' && pionBlanc[Character.getNumericValue(s.charAt(2) - 1)]) {
                    premierTour = true;
                } else if (couleur == 'N' && pionNoir[Character.getNumericValue(s.charAt(2) - 1)]) {
                    premierTour = true;
                }
                pion.mouvement(Character.getNumericValue(s.charAt(4)), Character.getNumericValue(s.charAt(3)), premierTour, s.charAt(1),mouvDispos);
                break;
            default:
                break;
        }
        return mouvDispos;
    }

    /**
     * Valide les case disponible pour le mouvement
     * @param pieces        tous les pièces
     * passage par valeur pour pouvoir affecter les variables
     * @param mouvDispos
     * @param mouvCauseMort
     * @param pionEnMouvement
     */
    public void validerLesCasesDispoPourMouvement(final String pieces[],final ArrayList<String> mouvDispos,final ArrayList<String> mouvCauseMort,final String pionEnMouvement)
    {
        toRemove.clear();
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
                                enleverMouvApres(s, false,mouvDispos);
                        }
                        else
                        {
                            if(s.charAt(2) != 'E' && s.charAt(2) != 'K')
                                enleverMouvApres(s, true, mouvDispos);
                        }
                        it.remove();
                    }
                    else
                    {
                        if(pionEnMouvement.charAt(1) != s2.charAt(1))
                        {
                            enleverMouvApres(s, true, mouvDispos);
                        }
                    }

                }
            }
        }
        for(String s : toRemove)
        {
            mouvDispos.remove(s);
        }
    }

    /**
     * Enlève les mouvement non légaux
     * @param mouv //La case et la direction du mouvement
     * @param caseActuel si le mouvement part de la case acutuel ou non
     *passage par valeur pour pouvoir affecter les variables
     * @param mouvDispos
     */
    public void enleverMouvApres(String mouv, boolean caseActuel, ArrayList<String> mouvDispos)
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
    public int getPointagePieceQuiBouge(String s)
    {
        switch (s.charAt(0))
        {
            case 'P':
                return 1;
            case 'C':
                return 2;
            case 'F':
                return 3;
            case 'T':
                return 4;
            case 'Q':
                return 8;
            case 'K':
                return 16;
            default:
                return 0;

        }
    }
}
