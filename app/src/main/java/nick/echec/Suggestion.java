package nick.echec;

import android.util.Pair;

import java.util.ArrayList;

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

    public void prendreTempsPasseTour(long tempsPasseMillis, char couleur)
    {
        tempsPasseParTour.add(new Pair(tempsPasseMillis, couleur));
    }

    public void prendreStatPionMort(long tempsActuel, String piece)
    {
        Pair<Character,Long> pairTemp = new Pair(tempsActuel-start_time,piece);
        pionMort.add(pairTemp);
    }

    public String genererCommentaireTemps()
    {
        String suggestions = "Vous avez prit au total ";
        long tempsTotalBlanc = 0, tempsMoyenBlanc = 0, tempsTotalNoir = 0, tempsMoyenNoir = 0;
        int nombreTourBlanc = 0, nombreTourNoir = 0;
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
        tempsMoyenBlanc = tempsTotalBlanc/nombreTourBlanc;
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
        return suggestions;
    }

}
