package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import nick.echec.R;

/**
 * Gestionnaire de base de donnée pour le jeu d'échec. Donne accès aux données de la bd.
 * C'est vraiment juste ça. Oui, c'est quand même plate comme description.
 * Created by Maxime on 4/29/2016.
 */
public class MoteurBD {
    private static MoteurBD moteurBD = new MoteurBD();
    private Context context;
    private SQLiteDatabase db;
    private static final String dbName = "EchecDB";

    public static MoteurBD getMoteurBD() {
        return moteurBD;
    }

    private MoteurBD() {
        context = null;
        db = null;
    }

    /**
     * Initialise le gestionnaire de bd
     * @param context Contexte pour les données.
     */
    public static void init(Context context)
    {
        MoteurBD m = getMoteurBD();
        if (context == null)
            throw new IllegalArgumentException("Le contexte ne peut pas etre null");
        if (m.context != null || m.db != null)
            throw new IllegalStateException("Le dbmanager ne doit pas etre réinitialisé");

        m.context = context.getApplicationContext();
        m.db = m.context.openOrCreateDatabase(dbName, 0, null);

        String sql = context.getResources().getString(R.string.SQL);

        for(String s: sql.split(";"))
            m.db.execSQL(s);
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

    SQLiteDatabase getDb(){
        return db;
    }
}

