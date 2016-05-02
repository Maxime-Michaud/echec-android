package nick.echec.test.bd;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import data.GestionnaireBD;

@RunWith(AndroidJUnit4.class)
public class dbGestionnaireBDTest {
    GestionnaireBD gestionnaireBD;

    @Before
    public void setUp() throws Exception {
        gestionnaireBD = GestionnaireBD.getGestionnaireBD();
        gestionnaireBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        gestionnaireBD.dropAll();
    }

    /**
     * Test qu'un utilisateur peut s'authentifier
     */
    @Test
    public void Authentification(){
        final String password = "1234",
                username = "Abcd";

        gestionnaireBD.ajouterUtilisateur(username,password);
        Assert.assertTrue(gestionnaireBD.authentifier(username, password));
    }

    /**
     * Test que l'authentification fail si l'utilisateur n'a pas le bon username
     */
    @Test
    public void AuthentificationFail(){
        final String password = "1234",
                username = "Abcd";

        gestionnaireBD.ajouterUtilisateur(username, password);
        Assert.assertFalse(gestionnaireBD.authentifier(username, "NOT " + password));
    }

    /**
     * Test que l'authentification fail si l'utilisateur n'existe pas
     */
    @Test
    public void AuthentificationNoUser(){
        final String password = "1234",
                username = "Abcd";
        Assert.assertFalse(gestionnaireBD.authentifier(username,password));
    }

    /**
     * Test que l'on peut ajouter un utilisateur
     */
    @Test
    public void AjouterUtilisateur(){
        Assert.assertTrue(gestionnaireBD.ajouterUtilisateur("Abcd", "1234"));
    }

    /**
     * Test qu'on ne peut pas ajouter deux user avec le meme username
     */
    @Test
    public void AjouterUtilisateurMemeLogin(){
        final String password = "1234",
                username = "Abcd";

        gestionnaireBD.ajouterUtilisateur(username, password);
        Assert.assertFalse(gestionnaireBD.ajouterUtilisateur(username, "NOT " + password));
    }

    /**
     * Teste qie la fonction DropAll du gestionnaire de bd drop toutes les données
     */
    @Test
    public void DropAllTest(){
        final String password = "1234",
                username = "Abcd";

        gestionnaireBD.dropAll();

        gestionnaireBD = GestionnaireBD.getGestionnaireBD();
        gestionnaireBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
        gestionnaireBD.ajouterUtilisateur(username, password);
        Assert.assertTrue(gestionnaireBD.authentifier(username, password));
        gestionnaireBD.dropAll();

        gestionnaireBD = GestionnaireBD.getGestionnaireBD();
        gestionnaireBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertFalse(gestionnaireBD.authentifier(username, password));
    }
}