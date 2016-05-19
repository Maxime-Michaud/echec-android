package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire pour les parties
 * Created by Maxime on 5/18/2016.
 */
public class GestionnairePartie {

    public static List<Partie> get(Utilisateur utilisateur) {
        Cursor c = selectParties(utilisateur.getId());

        List<Partie> parties = new ArrayList<>();

        while (c.moveToNext()) {
            parties.add(new Partie(GestionnaireUtilisateurs.get(c.getInt(0)),
                    GestionnaireUtilisateurs.get(c.getInt(1)),
                    GestionnaireUtilisateurs.get(c.getInt(2))));
        }

        c.close();

        return parties;
    }

    private static Cursor selectParties(int userID) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        if (db == null)
            throw new DbNonInitialiseeException();

        return db.rawQuery("SELECT blanc, noir, gagnant FROM partie WHERE blanc = ? OR noir = ?", new String[] {Integer.toString(userID), Integer.toString(userID)});
    }

    private boolean insertPartie(Utilisateur blanc, Utilisateur noir) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        if (db == null)
            throw new DbNonInitialiseeException();

        ContentValues cv = new ContentValues();
        cv.put("noir", noir.getId());
        cv.put("blanc", blanc.getId());
        cv.put("id", getNextID());
        return db.insert("partie", null, cv) != -1;
    }

    /**
     * Obtiens l'id de la prochaine partie dans la bd locale
     *
     * @return id de la prochaine partie
     */
    private int getNextID() {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        Cursor c = db.rawQuery("select id from partie order by id asc limit 1;", null);
        try {
            return (c.moveToFirst() ? c.getInt(0) : 0) - 1;
        } finally {
            c.close();
        }
    }

    /**
     * Ajoute une partie a la bd
     *
     * @param blanc joueur blanc
     * @param noir  joueur noir
     * @return Si l'ajout a été fait correctement
     */
    public boolean ajouter(Utilisateur blanc, Utilisateur noir) {
        if (blanc.equals(noir))
            throw new IllegalArgumentException("Les deux utilisateurs doivent etre différent");

        return insertPartie(blanc, noir);
    }
}
