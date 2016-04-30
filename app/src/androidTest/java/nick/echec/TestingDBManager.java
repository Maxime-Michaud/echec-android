package nick.echec;

import android.content.Context;

import db.Manager;

/**
 * Fournis une base de donnée de test
 * Created by Maxime on 4/30/2016.
 */
public class TestingDBManager extends db.Manager{

    TestingDBManager(){
        super();
        init("Test.db");
    }
}
