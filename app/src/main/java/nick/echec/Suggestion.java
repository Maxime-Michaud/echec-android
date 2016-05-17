package nick.echec;

import android.util.Pair;

import java.util.ArrayList;


import data.GestionnaireSuggestion;
import data.Utilisateur;

/**
 * Created by Nick on 2016-05-08.
 */
public class Suggestion {
    char couleur;
    long start_time = System.currentTimeMillis();
    long timePassed;
    ArrayList<Pair<Character,Long>> pionMort = new ArrayList<>();
    ArrayList<Pair<Long, Character>> tempsPasseParTour = new ArrayList<>();
    ArrayList<String> suggestions = new ArrayList<>();


    Suggestion(char couleur)
    {
        this.couleur = couleur;
    }


    public ArrayList<String> genererEnvoyerSuggestions(Utilisateur utilisateur)
    {
        genererCommentaireTemps(utilisateur);
        genererSuggestionDefiPiece(utilisateur);
        return suggestions;
    }
    /**
     * Prends les statistique sur les tours
     * @param tempsPasseMillis le temps passé pour un tour
     * @param couleur la couleur du joueur en relation avec le param tempsPasseMillis
     */
    public void prendreTempsPasseTour(long tempsPasseMillis, char couleur)
    {
        tempsPasseParTour.add(new Pair(tempsPasseMillis, couleur));
    }

    /**
     * Prend les statistique quand un pion est mort
      * @param tempsActuel le temps passé depuis le début de lapartie jusqua la mort du pion
     * @param piece la pieces morte
     */
    public void prendreStatPionMort(long tempsActuel, String piece)
    {
        Pair<Character,Long> pairTemp = new Pair(piece.charAt(0),tempsActuel-start_time);
        pionMort.add(pairTemp);
    }

    /**
     * Génère la suggestion en rapport avec le temps puis, si besoin est, suggère un défi en relation avec le score du temps
     * @param utilisateur l'utilisateur
     * @return
     */
    public String genererCommentaireTemps(Utilisateur utilisateur)
    {
        String suggestions = "Vous avez prit au total ";
        long tempsTotalBlanc = 0, tempsMoyenBlanc = 0, tempsTotalNoir = 0, tempsMoyenNoir = 0;
        int nombreTourBlanc = 0, nombreTourNoir = 0;
        int niveauTemps = 0;
        for(Pair<Long,Character> p: tempsPasseParTour)
        {
            if(p.second == 'B')
            {
                tempsTotalBlanc += p.first;
                nombreTourBlanc++;
            }
            else
            {
                tempsTotalNoir += p.first;
                nombreTourNoir++;
            }
        }
        if (nombreTourBlanc != 0)
            tempsMoyenBlanc = tempsTotalBlanc/nombreTourBlanc;
        if (nombreTourNoir != 0)
            tempsMoyenNoir = tempsTotalNoir/nombreTourNoir;
        suggestions += (couleur == 'B'?Long.toString(tempsTotalBlanc / 60000): Long.toString(tempsTotalNoir / 60000));
        suggestions += " minutes. Et en moyenne, vous prennez ";
        suggestions += (couleur == 'B'?Long.toString(tempsMoyenBlanc): Long.toString(tempsMoyenNoir /1000));
        suggestions += " secondes par tour.\n";
        suggestions += "Une partie moyenne d'échec comprends 40 mouvements, vous en avez fait " + (couleur == 'B'?Long.toString(nombreTourBlanc):Long.toString(nombreTourNoir)) + " tours\n";
        suggestions += "Votre temps moyen par mouvement est ";
        if((couleur=='B'?tempsMoyenBlanc > 6000:tempsTotalNoir> 6000))
        {
            suggestions += "un peu trop long, garder en tête que lors d'une partie d'échec en tournoi, le temps est compté. Nous vous recommandons donc des exercice de temps et viser de 5 secondes à 10 secondes par mouvement";
        }
        else
        {
            suggestions += "très rapide et adéquat pour un tournoi.";
        }
        niveauTemps = defenirNivDiffTemps((couleur=='B'?tempsMoyenBlanc:tempsMoyenNoir));
        if(niveauTemps>0)
            GestionnaireSuggestion.ajouter(utilisateur,GestionnaireSuggestion.Type_Defi.TEMPS,niveauTemps);
        this.suggestions.add(suggestions);
        return suggestions;
    }

    /**
     * Défini li niveau de difficulté pour le défi du temps
     * @param tempsMoyen le temps moyen (duh)
     * @return
     */
    private int defenirNivDiffTemps(long tempsMoyen)
    {
        int niveau = 0;
        if(tempsMoyen > 12000)
            niveau = 1;
        else if(tempsMoyen >10000)
            niveau = 2;
        else if(tempsMoyen > 9000)
            niveau = 3;
        else if(tempsMoyen > 8000)
            niveau = 4;
        else if(tempsMoyen > 6500)
            niveau = 5;
        else
            niveau = 0;
        return niveau;
    }

    /**
     * Génère les suggestion et ajoute les défi dans la bd, concernant les pions
     * @param utilisateur l'utilisateur
     */
    public void genererSuggestionDefiPiece(Utilisateur utilisateur)
    {
        for(int i = 0;i < 5;i++)
        {
            String suggestion = "Pour les " + (i ==4?"pions, ":(i==3?"chevaux, ":i==2?"fous, ":i==1?"tours, ":"reines"));
            int niveau = defenirNivDiffPiece((i ==4?'P':(i==3?'C':i==2?'F':i==1?'T':'Q')),utilisateur);
            switch (niveau)
            {
                case 0:
                    suggestion+= "vous vous en tirez très bien, continuer comme ça!\n";
                    break;
                case 1:
                    suggestion+= "vous avez beaucoup de difficulté à les garder en vie.\n";
                    break;
                case 2:
                    suggestion+= "vous avez de la difficulté à les gèrer, même s'ils sont faibles, ils peuvent vous faire gagner une partie.\n";
                    break;
                case 3:
                    suggestion+= "vous pourriez vous améliorer, les pions sont très important dans une partie d'échec.\n";
                    break;
                case 4:
                    suggestion+= "vous avez légèrement de la difficulté à les gérer, mais vous vous en tirez quand même bien.\n";
                    break;
                case 5:
                    suggestion+= "vous pourriez vous améliorer juste un peu. Pratiquer vous avec le défi de difficulté maximum.\n";
                    break;
                default:
                    suggestion += "ERREUR DU NIVEAU DE DÉFI DANS GENERERSUGGESTIONDEIPIECE";
                    break;
            }
            suggestions.add(suggestion);
        }


    }

    /**
     * Défini le niv de difficulté d'un défi relatif à une pièce
     * @param piece
     * @return
     */
    private int defenirNivDiffPiece(char piece, Utilisateur utilisateur)
    {
        int niveau = 0;
        ArrayList<Pair<Character,Long>> pions = new ArrayList<>();
        switch (piece)
        {
            case 'P':
                for(Pair<Character,Long> pair:pionMort)
                {
                    if(pair.first == piece)
                        pions.add(pair);
                }
                if(pions.size() > 7)
                    niveau = 1;
                else if(pions.size() > 6)
                niveau = 2;
                else if(pions.size() > 5)
                    niveau = 3;
                else if(pions.size() > 4 && pions.get(3).second < 300000)
                    niveau = 4;
                else if(pions.size() > 3 && pions.get(2).second < 200000)
                    niveau = 5;
                GestionnaireSuggestion.ajouter(utilisateur,GestionnaireSuggestion.Type_Defi.PION,niveau);
                break;
            case 'T':
            case 'F':
            case 'C':
                for(Pair<Character,Long> pair:pionMort)
                {
                    if(pair.first == piece)
                        pions.add(pair);
                }
                if(pions.size() > 1)
                {
                    if(pions.get(1).second > 600000)
                        niveau=3;
                    else if(pions.get(1).second > 500000)
                        niveau=4;
                    else if(pions.get(1).second > 400000)
                        niveau=5;
                }
                else if (pions.size() == 1)
                {
                    if(pions.get(0).second > 600000)
                        niveau=0;
                    else if(pions.get(0).second > 500000)
                        niveau=1;
                    else if(pions.get(0).second > 400000)
                        niveau=2;
                }
                else
                    niveau = 0;
                GestionnaireSuggestion.ajouter(utilisateur,(piece=='T'?GestionnaireSuggestion.Type_Defi.TOUR:piece=='F'?GestionnaireSuggestion.Type_Defi.FOU:GestionnaireSuggestion.Type_Defi.CAVALIER),niveau);
                break;
            case 'Q':
                for(Pair<Character,Long> pair:pionMort)
                {
                    if(pair.first == piece)
                        pions.add(pair);
                }
                if(pions.isEmpty())
                {
                    niveau = 0;
                }
                else
                {
                    if(pions.get(0).second > 10000000)
                        niveau = 0;
                    else if(pions.get(0).second > 900000)
                        niveau = 5;
                    else if(pions.get(0).second > 800000)
                        niveau = 4;
                    else if(pions.get(0).second > 700000)
                        niveau = 3;
                    else if(pions.get(0).second > 600000)
                        niveau = 2;
                    else if(pions.get(0).second <= 600000)
                        niveau = 1;
                }
                GestionnaireSuggestion.ajouter(utilisateur,GestionnaireSuggestion.Type_Defi.REINE,niveau);
                break;
            default:
                niveau = 0;
                break;

        }
        return niveau;
    }
}

