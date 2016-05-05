package data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;

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
    private final static String username = "USTWEQTGEWRBEWAQHBRAWQEQWARGWQE";

    @BeforeClass
    public static void SetUp()
    {
        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());

        GestionnaireUtilisateurs.ajouter(username, "");
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

    /**
     * Teste que les défis ajouter a l'utilisateur sont ajoutés a l'utilisateur
     */
    @Test
    public void defisCompletes()
    {
        Utilisateur u = GestionnaireUtilisateurs.get(username);

        String nom = "defisCompletes";
        int nbTours = 0;
        String grille = "PB777,PN000";
        Assert.assertTrue(GestionnaireDefi.ajouter(nom, nbTours, grille));

        Defi d = GestionnaireDefi.get(nom);

        u.ajouterResultat(d, 5, true);

        Assert.assertTrue(Stream.of(u.getDefisEssaye())
                .anyMatch(r -> r.getDefi().equals(d)));
    }

    /**
     * Teste que les defis pas ajoutés a l'utilisateur n'est pas ajouter a l'utilisateur
     */
    /**
     * Teste que les défis ajouter a l'utilisateur sont ajoutés a l'utilisateur
     */
    @Test
    public void defisNonCompletes()
    {
        Utilisateur u = GestionnaireUtilisateurs.get(username);

        String nom = "defisNonCompletes";
        int nbTours = 0;
        String grille = "PB777,PN000";
        Assert.assertTrue(GestionnaireDefi.ajouter(nom, nbTours, grille));

        Defi d = GestionnaireDefi.get(nom);

        Assert.assertFalse(Stream.of(u.getDefisEssaye())
                .anyMatch(r -> r.getDefi().equals(d)));
    }
}
