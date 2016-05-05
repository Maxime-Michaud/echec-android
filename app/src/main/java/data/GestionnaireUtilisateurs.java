package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Stream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Crée et gère les modifications des utilisateurs
 * Created by Maxime on 5/2/2016.
 */
public class GestionnaireUtilisateurs {
    static private ArrayList<Utilisateur> users = new ArrayList<>();


    /**
     * Selectionne le hash du password dans la base de donné pour l'utilisateur
     * @param username
     * @return
     */
    private static Cursor selectPassword(String username){
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        if (db == null)
            throw new DbNonInitialiseeException();

        /*Ah, java, déjà en retard de 10 ans lorsqu'il a été dévellopé. Pas de support pour les arguments par défaut?
        Des arguments nommés seraient appréciés aussi, parce que comme ca c'est un gros clusterfuck*/
        return db.query("utilisateur", new String[]{"password"}, "login = ?", new String[]{username}, null, null, null);
    }

    /**
     * Ajoute un utilisateur a la bd.
     *
     * L'utilisateur doit etre connecté a internet puisqu'il faut valider que le nom qu'il choisit n'existe pas actuellement
     * @param username Nom de l'utilisateur
     * @param password Password de l'utilisateur (plaintext)
     * @return Si l'utilisateur est créé.
     * False si le username est déjà pris, true sinon
     * @throws DbNonInitialiseeException Lorsque le gestionnaire n'a pas été initialisé
     */
    private static boolean insertUtilisateur(int id, String username, String password){
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        if (db == null)
            throw new DbNonInitialiseeException();

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("login", username);
        cv.put("password", password);
        cv.put("type_compte", 1);   //Compte publique

        try {
            return db.insert("utilisateur", null, cv) != -1;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static boolean insertRelation(int usr1, int usr2, int rel)
    {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        ContentValues cv = new ContentValues();
        cv.put("user_1", usr1);
        cv.put("user_2", usr2);
        cv.put("status_relation", rel);

        return db.insert("relation", null, cv) != -1;
    }

    /**
     * Get l'id du dernier utilisateur
     * @return
     */
    private static int selectDernierIdUtilisateur() {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        //TODO devrais getter sur les interwebs
        Cursor c = db.rawQuery("select id from utilisateur order by id desc limit 1;", null);
        try {
            return c.moveToFirst() ? c.getInt(0) : 1;
        }
        finally {
            c.close();
        }
    }

    /**
     * Obtiens un utilisateur de la base de donnée.
     * @param username nom de l'utilisateur a getter
     * @return L'utilisateur ou null si il n'existe pas
     */
    private static Cursor selectUtilisateur(String username) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();

        //TODO devrais getter sur internet si possible
        if (db == null)
            throw new DbNonInitialiseeException();

        return db.rawQuery( "SELECT u.id, u.login, u.nom, u.prenom, u.email, u.type_compte " +
                            "FROM utilisateur u " +
                            "WHERE u.login = ?;", new String[] {
                            username});
    }

    /**
     * Update un utilisateur
     * @param id id de l'utilisateur a updater
     * @param cv
     * @return Si l'update a fonctionner
     */
    private static boolean updateUtilisateur(int id, ContentValues cv){
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        return db.update("utilisateur", cv, "id = ?", new String []{Integer.toString(id)}) == 1;
    }


    /**
     * Hash un string en md5.
     * @param s String a hasher
     * @return Hash md5 du string
     * Code extrait de http://stackoverflow.com/questions/4846484/md5-hashing-in-android#4846511
     */
    private static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Authentifie un utilisateur.
     *
     * Si l'utilisateur ne s'est jamais connecté sur cet appareil, il doit avoir un accès internet pour vérifier les infos auprès de la BD
     * @param username Nom de l'utilisateur
     * @param password Password de l'utilisateur (plaintext)
     * @return Si la combinaison username/password est valide
     * @throws DbNonInitialiseeException Lorsque le gestionnaire n'a pas été initialisé
     */
    public static boolean authentifier(String username, String password) throws DbNonInitialiseeException {

        Cursor cursor = selectPassword(username);

        return cursor.moveToFirst() && cursor.getString(0).equals(md5(password))
                || internetAuth(username, password);
    }

    /**
     * Authentifie un utilisateur.
     *
     * Si l'utilisateur ne s'est jamais connecté sur cet appareil, il doit avoir un accès internet pour vérifier les infos auprès de la BD
     * @param username Nom de l'utilisateur
     * @param password Password de l'utilisateur (plaintext)
     * @return Si la combinaison username/password est valide
     * @throws DbNonInitialiseeException Lorsque le gestionnaire n'a pas été initialisé
     */
    private static boolean internetAuth(String username, String password)
    {
        /* TODO En attendant que l'API pour l'accès a la bd du serveur soit dévellopé on return false;,
         mais après ça ça serais pertinent que la fonction fasse ce que son nom suggère */
        return false;
    }

    /**
     * Ajoute un utilisateur a la bd.
     *
     * L'utilisateur doit etre connecté a internet puisqu'il faut valider que le nom qu'il choisit n'existe pas actuellement
     * @param username Nom de l'utilisateur
     * @param password Password de l'utilisateur (plaintext)
     * @return Si l'utilisateur est créé.
     * False si le username est déjà pris, true sinon
     * @throws DbNonInitialiseeException Lorsque le gestionnaire n'a pas été initialisé
     */
    public static boolean ajouter(String username, String password){
        if (!internetAjouterUtilisateur(username, password))
            return false;

        int id = selectDernierIdUtilisateur() + 1;

        return insertUtilisateur(id, username, md5(password));
    }

    /**
     * Ajoute un utilisateur a la bd.
     *
     * @param username Nom de l'utilisateur
     * @param password Password de l'utilisateur (plaintext)
     * @return Si l'utilisateur est créé.
     * False si le username est déjà pris ou si il y a une erreur quelconque, true sinon
     * @throws DbNonInitialiseeException Lorsque le gestionnaire n'a pas été initialisé
     */
    private static boolean internetAjouterUtilisateur(String username, String password) {
        /* TODO En attendant que l'API pour l'accès a la bd du serveur soit dévellopé on vérifie dans la bd locale,
         mais après ça ça serais pertinent que la fonction fasse ce que son nom suggère */

        return selectUtilisateur(username).getCount() == 0;
    }

    /**
     * Update un utilisateur
     * @param u utilisateur a updater
     */
    public static boolean update(Utilisateur u)
    {
        ContentValues cv = new ContentValues();
        cv.put("email", u.getEmail());
        cv.put("nom", u.getNom());
        cv.put("prenom", u.getPrenom());
        cv.put("type_compte", u.getType().toInt());
        cv.put("updated", 1);

        return updateUtilisateur(u.getId(), cv);
    }

    /**
     * Obtiens l'utilisateur ayant le username donné
     * @param login
     * @return
     */
    public static Utilisateur get(String login)
    {
        Utilisateur u = Stream.of(users).filter(usr -> usr.getUsername().equals(login)).findFirst().orElse(null);

        if (u != null)
            return u;

        Cursor c = selectUtilisateur(login);
        if(c.moveToFirst())
           u = new Utilisateur(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5));

        users.add(u);
        return u;
    }

    /**
     * Ajoute une relation a l'utilisateur
     * @param u1
     * @param u2
     * @param r
     * @return
     */
    public static boolean ajouterRelation(Utilisateur u1, Utilisateur u2, Utilisateur.RELATION r) {
        if (r == Utilisateur.RELATION.Ami)
            return insertRelation(u1.getId(), u2.getId(), r.toInt()) && insertRelation(u2.getId(), u1.getId(), r.toInt());

        return insertRelation(u1.getId(), u2.getId(), r.toInt());
    }

    /**
     * Cherche les relations d'un utilisateur dans la bd
     * @param id id de l'utilisateur
     * @return
     */
    static Map<Utilisateur,Utilisateur.RELATION> getRelations(int id) {
        Map<Utilisateur, Utilisateur.RELATION> rels = new HashMap<>();
        Cursor c = selectRelations(id);

        try {
            while (c.moveToNext())
            {
                if (c.getInt(0) == 2)
                    rels.put(get(c.getString(1)), Utilisateur.RELATION.fromInt(c.getInt(0)));
                else
                    rels.put(get(c.getString(2)), Utilisateur.RELATION.fromInt(c.getInt(0)));
            }
        }
        finally {
            c.close();
        }
        return rels;
    }

    private static Cursor selectRelations(int id) {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        return db.rawQuery( "SELECT r.status_relation, u1.login, u2.login " +
                            "FROM relation r INNER JOIN utilisateur u1 ON r.user_1 = u1.id " +
                                            "INNER JOIN utilisateur u2 ON r.user_2 = u2.id " +
                            "WHERE r.user_1 = ? AND r.status_relation != 2 OR " +       //Tout sauf les demandes d'amis par l'utilisateur
                                  "r.user_2 = ? AND r.status_relation == 2",            //Demandes d'amis recu par l'utilisateur
                            new String[]{Integer.toString(id), Integer.toString(id)});
    }

    /**
     * Efface les utilisateurs enregistrer dans l'arraylist. Utile pour les tests unitaire qui clear la bd, pas grand chose d'autre
     */
    static void effacerEnregistrer(){
        users.clear();
    }
}
