package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Gère les défis offerts aux utilisateurs
 * Created by Maxime on 5/4/2016.
 */
public class GestionnaireDefi {

    /**
     * Valide une grille
     * @param g grille a valider
     * @return si la grille est une grille valide
     */
    private static boolean validerGrille(String g) {
        List<String> grille = Arrays.asList(g.split(","));

        return g.contains("B") && g.contains("N") &&
                Stream.of(grille)
                        .filter(s -> s.length() > 0)
                        .allMatch(s -> Pattern.matches("[PTFCQK][NB].[0-7][0-7]", s));
    }


    /**
     * Sélectionne tous les résultats des défis d'un utilisateur
     * @param u Utilisateur duquel on cherche les résultats
     * @return Curseur contenant les résultats
     */
    private static Cursor selectResultat(Utilisateur u) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        return db.rawQuery("SELECT id, nb_tour, reussi, defi " +
                            "FROM defi_utilisateurs " +
                            "WHERE utilisateur = ?", new String[]{Integer.toString(u.getId())});
    }

    /**
     * Insere la grille dans la bd
     * @param nom
     * @param nbTours
     * @param grille
     * @return
     */
    private static boolean insertDefi(String nom, int nbTours, String grille) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        ContentValues cv = new ContentValues();
        cv.put("id", getDernierID() + 1);
        cv.put("nom", nom);
        cv.put("nb_tours_max", nbTours);
        cv.put("grille", grille);

        return insertDefiInternet(nom, nbTours, grille) &&
                db.insert("defi", null, cv) != -1;
    }

    private static int getDernierID() {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        //TODO devrais getter sur les interwebs
        Cursor c = db.rawQuery("select id from defi order by id desc limit 1;", null);
        try {
            return c.moveToFirst() ? c.getInt(0) : 1;
        }
        finally {
            c.close();
        }
    }

    /**
     * Insere la grille sur internet
     * @param nom
     * @param nbTours
     * @param grille
     * @return
     */
    private static boolean insertDefiInternet(String nom, int nbTours, String grille) {
        //TODO comme d'habitude la. Faut faire ce que la fonction dit.
        return get(nom) == null;
    }

    /**
     * Obtiens l'id du dernier resultat de defi de la base de donnée
     * @return
     */
    private static int idDernierResultat(){
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        //TODO devrais getter sur les interwebs. Si offline utiliser des ID négatif puis updater une fois connecter
        Cursor c = db.rawQuery("select id from defi_utilisateurs order by id desc limit 1;", null);
        try {
            return c.moveToFirst() ? c.getInt(0) : 1;
        }
        finally {
            c.close();
        }
    }

    private static boolean insertResultat(int user, int defi, int nb_tour, boolean reussi){
        ContentValues cv = new ContentValues();
        cv.put("id", idDernierResultat() + 1);
        cv.put("nb_tour", nb_tour);
        cv.put("reussi", reussi ? 1 : 0);
        cv.put("utilisateur", user);
        cv.put("defi", defi);

        return MoteurBD.getMoteurBD().getDb().insert("defi_utilisateurs", null, cv) != -1;
    }

    /**
     * Ajoute un defi a la bd.
     * @param nom nom du defi
     * @param nbTours nombre de tours max pour completer le defi. Si 0, infini. Si < 0, l'objectif est de survivre minimum nbTours tours
     * @param grille Grille du défi, au format [Pion][Couleur][Tag][Y][X],
     *               Ou [Pion] est :
     *                  Pion     -> P
     *                  Tour     -> T
     *                  Fou      -> F
     *                  Cavalier -> C
     *                  Reine    -> Q
     *                  Roi      -> K
     *               [couleur] :
     *                  Noir     -> N
     *                  Blanc    -> B
     *               [Tag] est un charactere unique pour la combinaison [Pion][Couleur][Tag]
     *               [Y] La position verticale sur la grille, où 0 est la case la plus en haut et 7 la plus en bas
     *               [X] La position horizontale sur la grille, où 0 est la case la plus a gauche et 7 la plus a droite
     *               Tous les characteres doivent etre en majuscule.
     * @return Si l'ajout a bien été effectué. Le nom ne doit pas déja se trouver dans la bd et la grille doit etre valide
     */
    public static boolean ajouter(String nom, int nbTours, String grille) {
        return validerGrille(grille) && insertDefi(nom, nbTours, grille);
    }

    /**
     * Obtiens le defi avec le nom donné
     * @param nom nom du défi
     */
    public static Defi get(String nom) {
        Cursor c = selectDefi(nom);

        try {
            return c.moveToFirst() ? new Defi(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3), c.getFloat(4), c.getString(5), c.getInt(6))
                    :null;
        }
        finally {
            c.close();
        }
    }

    private static Defi get(int anInt) {
        Cursor c = selectDefi(anInt);

        try {
            return c.moveToFirst() ? new Defi(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3), c.getFloat(4), c.getString(5), c.getInt(6))
                    :null;
        }
        finally {
            c.close();
        }
    }
    /**
     * Sélectionne le défi dans la bd
     * @param nom
     * @return
     */
    private static Cursor selectDefi(String nom) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        //TODO devrais getter sur internet si possible
        if (db == null)
            throw new DbNonInitialiseeException();

        return db.rawQuery( "SELECT d.id, d.nom, d.nb_tours_max, d.score, d.difficulte, d.grille, d.nombre_evaluations " +
                            "FROM defi d " +
                            "WHERE d.nom = ?;", new String[] {
                            nom});
    }

    /**
     * Obtiens les résultats d'un utilisateur
     * @param u utilisateur
     * @return
     */
    static List<ResultatDefi> getResultats(Utilisateur u){
        Cursor c = selectResultat(u);

        ArrayList<ResultatDefi> resultats = new ArrayList<>();

        try{
            while (c.moveToNext())
            {
                /* id, nb_tour, reussi, defi*/
                resultats.add(new ResultatDefi(get(c.getInt(3)), c.getInt(1), c.getInt(2) == 1));
            }
        }
        finally {
            c.close();
        }
        return resultats
    }


    /**
     * Ajoute une tentative de défi a l'utilisateur
     */
    static boolean ajouterResultat(Utilisateur u, ResultatDefi r)
    {
        return insertResultat(u.getId(), r.getDefi().getId(), r.getNbTour(), r.isReussi());
    }
}
