package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nick.echec.R;

/**
 * Gestionnaire de base de donnée pour le jeu d'échec. Donne accès aux données de la bd.
 * C'est vraiment juste ça. Oui, c'est quand même plate comme description.
 * Created by Maxime on 4/29/2016.
 */
public class GestionnaireBD {
    private static GestionnaireBD gestionnaireBD = new GestionnaireBD();
    private Context context;
    private SQLiteDatabase db;
    private static final String dbName = "EchecDB";

    public static GestionnaireBD getGestionnaireBD() {
        return gestionnaireBD;
    }

    protected GestionnaireBD() {
        context = null;
        db = null;
    }

    /**
     * Initialise le gestionnaire de bd
     * @param context Contexte pour les données.
     */
    public void init(Context context)
    {
        if (this.context != null || db != null)
            throw new IllegalStateException("Le dbmanager ne doit pas etre réinitialisé");

        this.context = context.getApplicationContext();
        db = this.context.openOrCreateDatabase(dbName, 0, null);

        String sql = context.getResources().getString(R.string.SQL);

        for(String s: sql.split(";"))
            db.execSQL(s);
    }

    /**
     * Initialise la bd a partir de son nom.
     * @param dbName Nom complet de la BD
     */
    protected void init(String dbName){
        db = SQLiteDatabase.openOrCreateDatabase(dbName, null);

        String sql = context.getResources().getString(R.string.SQL);
        for(String s: sql.split(";"))
            db.execSQL(s + ";");
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
    public boolean authentifier(String username, String password) throws DbNonInitialiseeException {
        if (db == null)
            throw new DbNonInitialiseeException();

        /*Ah, java, déjà en retard de 10 ans lorsqu'il a été dévellopé. Pas de support pour les arguments par défaut?
        Des arguments nommés seraient appréciés aussi, parce que comme ca c'est un gros clusterfuck*/
        Cursor cursor = db.query("utilisateur", new String[]{"password"}, "login = ?", new String[]{username}, null, null, null);

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
    private boolean internetAuth(String username, String password)
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
    public boolean ajouterUtilisateur(String username, String password){
        if (db == null)
            throw new DbNonInitialiseeException();


        if (!internetAjouterUtilisateur(username, password))
            return false;

        int id = getDernierIdUtilisateur() + 1;

        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("login", username);
        cv.put("password", md5(password));
        cv.put("type_compte", 4);   //Compte anonyme

        try {
            return db.insertOrThrow("utilisateur", null, cv) != -1;
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
    private int getDernierIdUtilisateur() {
        //TODO devrais getter sur les interwebs
        Cursor c = db.rawQuery("select id from utilisateur order by id desc limit 1;", null);
        return c.moveToFirst() ? c.getInt(0) : 1;
    }

    /**
     * Obtiens un utilisateur de la base de donnée.
     * @param username nom de l'utilisateur a getter
     * @return L'utilisateur ou null si il n'existe pas
     */
    public Utilisateur GetUtilisateur(String username) {
        //TODO devrais getter sur internet
        if (db == null)
            throw new DbNonInitialiseeException();

        Cursor c = db.rawQuery( "SELECT u.id, u.login, u.nom, u.prenom, u.email, u.type_compte " +
                                "FROM utilisateur u " +
                                "WHERE u.login = ?;", new String[] {
                                username});
        if (c.moveToFirst())
            return new Utilisateur(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5));
        else
            return null;
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
    private boolean internetAjouterUtilisateur(String username, String password) {
        /* TODO En attendant que l'API pour l'accès a la bd du serveur soit dévellopé on vérifie dans la bd locale,
         mais après ça ça serais pertinent que la fonction fasse ce que son nom suggère */

        return db.query("utilisateur", new String[]{"login"}, "login = ?", new String[]{username}, null, null, null).getCount() == 0;
    }

    /**
     * Ferme la connection a la base de donnée. La connection peut ensuite etre réinitialisée si nécéssaire.
     */
    public void close()
    {
        db.close();
        db = null;
        context = null;
    }

    /**
     * Drop toutes les données de la db locale
     */
    public void dropAll() {
        db.close();
        context.deleteDatabase(dbName);
        db = null;
        context = null;
    }

    /**
     * Met a jour des donnés dans la BD.
     * @param data Objet a mettre a jour dans la bd
     * @return True si l'update se produit,
     *         False si aucune update ne se produit,
     */
    public <T extends DBModel> boolean update(T data) {
        if (data instanceof Utilisateur)
            return updateUtilisateur((Utilisateur) data);

        return false;
    }

    /**
     * Update l'utilisateur dans la bd
     * @param u utilisateur a updater
     * @return True si l'update se produit,
     *         False si aucune update ne se produit,
     */
    private boolean updateUtilisateur(Utilisateur u) {
        ContentValues cv = new ContentValues();
        cv.put("updated", 1);
    }
}
