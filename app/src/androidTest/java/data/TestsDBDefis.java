package data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests pour les defis dans la base de donnée
 * Created by Maxime on 5/4/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestsDBDefis {

    @BeforeClass
    public static void SetUp()
    {
        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        MoteurBD.getMoteurBD().dropAll();
    }

    /**
     * Teste que la création d'un defi fonctionne
     */
    @Test
    public void creationDefi(){
        String nom = "creationDefi";
        int nbTours = 0;
        String grille = "PB777,PN000";
        Assert.assertTrue(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNotNull(GestionnaireDefi.get(nom));
    }

    /**
     * Teste que la création d'un défi invalide plante
     */
    @Test
    public void creationDefiInvalide(){
        String nom = "creationDefiInvalide";
        int nbTours = 0;

        //Fail car M n'est pas une piece valide
        String grille = "MB777,PN000";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fail car j n'est pas une piece valide
        grille = "PJ777,PN000";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fail car 88 n'est pas une position valide
        grille = "PB788,PN000";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fail car il n'y a pas de pieces noires
        grille = "PB777";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fail car il n'y a pas de pieces blanches
        grille = "PN000";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fail car les pieces ne sont pas séparées par des virgules
        grille = "PB777PN000";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fail car les description de piece manque une lettre
        grille = "PB777PN000";
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNull(GestionnaireDefi.get(nom));

        //Fonctionne
        grille = "PB777,PN000";
        Assert.assertTrue(GestionnaireDefi.ajouter(nom, nbTours, grille));
        Assert.assertNotNull(GestionnaireDefi.get(nom));
        //Fail car la grille est déja présente dans la bd
        Assert.assertFalse(GestionnaireDefi.ajouter(nom, nbTours, grille));
    }
}
