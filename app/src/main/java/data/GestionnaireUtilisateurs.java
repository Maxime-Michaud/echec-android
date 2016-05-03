package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Crée et gère les modifications des utilisateurs
 * Created by Maxime on 5/2/2016.
 */
public class GestionnaireUtilisateurs {

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

    /**
     * Get l'id du dernier utilisateur
     * @return
     */
    private static int selectDernierIdUtilisateur() {
        SQLiteDatabase db = MoteurBD.getMoteurBD().getDb();
        //TODO devrais getter sur les interwebs
        Cursor c = db.rawQuery("select id from utilisateur order by id desc limit 1;", null);
        return c.moveToFirst() ? c.getInt(0) : 1;
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

        return db.rawQuery(     "SELECT u.id, u.login, u.nom, u.prenom, u.email, u.type_compte " +
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
    public static boolean ajouterUtilisateur(String username, String password){
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

    public static Utilisateur getUtilisateur(String login)
    {
        Cursor c = selectUtilisateur(login);
        return c.moveToFirst() ? new Utilisateur(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5)) : null;
    }


}
