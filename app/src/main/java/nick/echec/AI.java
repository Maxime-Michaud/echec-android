package nick.echec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Nick on 2016-05-03.
 */
public class AI {
    Pion pion = new Pion();
    Fou fou = new Fou();
    Tour tour = new Tour();
    Reine reine = new Reine();
    Cheval cheval = new Cheval();
    Roi roi = new Roi();
    char couleur;
    ArrayList<String> toRemove;                             //Les mouvement à enlever de l'array


    public AI(char couleurAI)
    {
        couleur = couleurAI;
    }

    public String choisirPieceRandom(final String[] pieces,final boolean pionNoir[],final boolean pionBlanc[],final ArrayList<String> mouvDispos,final ArrayList<String> mouvCauseMort,final StringBuilder pionEnMouvement)
    {
        boolean ok = false;
        int nombreAleatoire = -1;
        while(!ok)
        {
            StringBuilder zText = new StringBuilder ();
            do
            {
                pionEnMouvement.delete(0, pionEnMouvement.capacity());
                Random rand = new Random();
                nombreAleatoire = rand.nextInt((pieces.length - 1) - 0 + 1);
                pionEnMouvement.append(pieces[nombreAleatoire]);
            }while(pionEnMouvement.toString().charAt(1) != couleur);

            ajouterMouvDispo(pionEnMouvement.toString(), pionNoir, pionBlanc, mouvDispos);
            validerLesCasesDispoPourMouvement(pieces, mouvDispos,mouvCauseMort, pionEnMouvement.toString());
            if (mouvDispos.size() > 0 && pionEnMouvement.toString().charAt(1) == couleur)
                ok = true;
            else
            {
                pionEnMouvement.delete(0, pionEnMouvement.capacity());
                mouvDispos.clear();
            }

        }
        Random rand = new Random();
        //Priorise un mouvement qui cause la mort d'une pièce
        if(!mouvCauseMort.isEmpty())
        {
            nombreAleatoire = rand.nextInt((mouvCauseMort.size() - 1) - 0 + 1);
            return mouvCauseMort.get(nombreAleatoire);
        }
        else
        {
            nombreAleatoire = rand.nextInt((mouvDispos.size() - 1) - 0 + 1);
            return mouvDispos.get(nombreAleatoire);
        }

    }

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

    public void validerLesCasesDispoPourMouvement(final String pieces[],final ArrayList<String> mouvDispos,final ArrayList<String> mouvCauseMort,final String pionEnMouvement)
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
}
