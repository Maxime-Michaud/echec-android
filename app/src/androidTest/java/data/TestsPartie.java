package data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testes les infos des parties, vérifie qu'elles sont gettables par la bd, etc.
 * Created by Maxime on 5/10/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestsPartie {
    final static String usr1 = "User 1",
                        usr2 = "User 2";


    @BeforeClass
    static public void SetUp(){
        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
        GestionnaireUtilisateurs.ajouter(usr1, "");
        GestionnaireUtilisateurs.ajouter(usr2, "");

    }

    @AfterClass
    static public void TearDown(){
        MoteurBD.getMoteurBD().dropAll();
    }

    /**
     * Teste que les parties jouées par un utilisateur sont dans la bd
     */
    @Test
    public void TestPartiesJouees(){

    }
}
