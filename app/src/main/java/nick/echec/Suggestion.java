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
    ArrayList<Long> tempsPasseParTour = new ArrayList<>();
    ArrayList<String> suggestions = new ArrayList<>();


    Suggestion(char couleur)
    {
        this.couleur = couleur;
    }

    public void prendreTempsPasseTour(long tempsPasseMillis)
    {
        tempsPasseParTour.add(tempsPasseMillis);
    }

    public void prendreStatPionMort(long tempsActuel, String piece)
    {
        Pair<Character,Long> pairTemp = new Pair(tempsActuel-start_time,piece);
        pionMort.add(pairTemp);
    }

}
