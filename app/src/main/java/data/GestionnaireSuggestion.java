package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
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
     * @return si une suggestion a été ajoutée
     */
    public static boolean ajouter(Utilisateur u, Type_Defi type, int diff) {
        List<Defi> defis = GestionnaireDefi.get(diff);

        return Stream.of(defis)
                .filter(d -> d.getNom().toLowerCase().contains(type.name().toLowerCase()))
                .map(d -> insertSuggestion(u.getId(), d.getId()))
                .anyMatch(b -> b);

    }

    /**
     * Insère une suggestion de défi dans la bd
     *
     * @param user Utilisateur auquel ajouter la suggestion
     * @param defi Defi a ajouter
     * @return Si l'insertion est ok
     */
    private static boolean insertSuggestion(int user, int defi) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        if (db == null)
            throw new DbNonInitialiseeException();

        ContentValues cv = new ContentValues();
        cv.put("utilisateur", user);
        cv.put("defi", defi);

        return db.insert("suggestions", null, cv) != -1;
    }

    /**
     * Obtiens les suggestions pour un utilisateur
     *
     * @param u Utilisateur
     * @return Liste des suggestions
     */
    public static List<Defi> get(Utilisateur u) {
        Cursor c = selectSuggestion(u.getId());

        ArrayList<Defi> defis = new ArrayList<>();

        while (c.moveToNext())
            defis.add(new Defi(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3), c.getFloat(4), c.getString(6), c.getInt(5)));

        c.close();

        return Collections.unmodifiableList(defis);
    }

    private static Cursor selectSuggestion(int id) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        if (db == null)
            throw new DbNonInitialiseeException();

        return db.rawQuery(
                "SELECT id, nom, nb_tours_max, score, difficulte, nombre_evaluations, grille " +
                        "FROM suggestions s INNER JOIN defi d ON d.id = s.defi" +
                        "WHERE s.utilisateur = ?",
                new String[] {Integer.toString(id)});

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