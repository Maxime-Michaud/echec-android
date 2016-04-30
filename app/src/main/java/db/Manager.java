package db;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.Scanner;

import nick.echec.R;

/**
 * Gestionnaire de base de donnée pour le jeu d'échec. Donne accès aux données de la bd.
 * C'est vraiment juste ça. Oui, c'est quand même plate comme description.
 * Created by Maxime on 4/29/2016.
 */
public class Manager {
    private static Manager manager = new Manager();
    private Context context;
    private SQLiteDatabase db;

    public static Manager getManager() {
        return manager;
    }

    private Manager() {
        context = null;
        db = null;
    }

    /**
     * Initialise le gestionnaire de bd
     * @param context Contexte pour les données.
     */
    public void init(Context context)
    {
        this.context = context.getApplicationContext();
        db = this.context.openOrCreateDatabase("EchecDB", 0, null);

        String sql = context.getResources().getString(R.string.SQL);

        for(String s: sql.split(";"))
            db.execSQL(s);
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
        if (db == null || context == null)
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

}
