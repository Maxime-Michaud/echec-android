package db;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ResourceBundle;
import java.util.Scanner;

import nick.echec.R;

/**
 * Created by Maxime on 4/29/2016.
 */
public class Manager {
    private static Manager manager = new Manager();
    SQLiteDatabase db;

    public static Manager getManager() {
        return manager;
    }

    private Manager() {
        db = SQLiteDatabase.openOrCreateDatabase("EchecDB", null);

        String sql = Resources.getSystem().getString(R.string.SQL);

        db.execSQL(sql);
    }
}
