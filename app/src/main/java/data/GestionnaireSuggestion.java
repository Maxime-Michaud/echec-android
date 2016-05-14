package data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Stream;

import java.util.List;

/**
 * Gère l'ajout des suggestions a la bd
 * Created by Maxime on 5/10/2016.
 */
public class GestionnaireSuggestion {
    /**
     * Ajoute une ou plusieurs suggestion a la base de donnée
     *
     * @param u    Utilisateur qu'on ajoute a la bd
     * @param type type de défi a ajouter
     * @param diff niveau de difficulté
     * @return si la suggestion a été ajoutée
     */
    public static boolean ajouter(Utilisateur u, Type_Defi type, int diff) {
        List<Defi> defis = GestionnaireDefi.get(diff);

        return Stream.of(defis)
                .filter(d -> d.getNom().toLowerCase().contains(type.name().toLowerCase()))
                .map(d -> insertSuggestion(u.getId(), d.getId()))
                .anyMatch(b -> b);

    }

    private static boolean insertSuggestion(int user, int defi) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        if (db == null)
            throw new DbNonInitialiseeException();

        ContentValues cv = new ContentValues();
        cv.put("utilisateur", user);
        cv.put("defi", defi);

        return db.insert("suggestions", null, cv) != -1;
    }

    public enum Type_Defi {
        TEMPS,
        PION,
        TOUR,
        CAVALIER,
        FOU,
        REINE,
        ROI;
    }
}
