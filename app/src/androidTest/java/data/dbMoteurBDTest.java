package data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class dbMoteurBDTest {
    MoteurBD moteurBD;

    @Before
    public void setUp() throws Exception {
        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        MoteurBD.getMoteurBD().dropAll();
    }

    /**
     * Test qu'un utilisateur peut s'authentifier
     */
    @Test
    public void Authentification(){
        final String password = "1234",
                username = "Abcd";

        GestionnaireUtilisateurs.ajouterUtilisateur(username, password);
        Assert.assertTrue(GestionnaireUtilisateurs.authentifier(username, password));
    }

    /**
     * Test que l'authentification fail si l'utilisateur n'a pas le bon username
     */
    @Test
    public void AuthentificationFail(){
        final String password = "1234",
                username = "Abcd";

        GestionnaireUtilisateurs.ajouterUtilisateur(username, password);
        Assert.assertFalse(GestionnaireUtilisateurs.authentifier(username, "NOT " + password));
    }

    /**
     * Test que l'authentification fail si l'utilisateur n'existe pas
     */
    @Test
    public void AuthentificationNoUser(){
        final String password = "1234",
                username = "Abcd";
        Assert.assertFalse(GestionnaireUtilisateurs.authentifier(username,password));
    }

    /**
     * Test que l'on peut ajouter un utilisateur
     */
    @Test
    public void AjouterUtilisateur(){
        Assert.assertTrue(GestionnaireUtilisateurs.ajouterUtilisateur("Abcd", "1234"));
    }

    /**
     * Test qu'on ne peut pas ajouter deux user avec le meme username
     */
    @Test
    public void AjouterUtilisateurMemeLogin(){
        final String password = "1234",
                username = "Abcd";

        GestionnaireUtilisateurs.ajouterUtilisateur(username, password);
        Assert.assertFalse(GestionnaireUtilisateurs.ajouterUtilisateur(username, "NOT " + password));
    }

    /**
     * Teste qie la fonction DropAll du gestionnaire de bd drop toutes les données
     */
    @Test
    public void DropAllTest(){
        final String password = "1234",
                username = "Abcd";

        moteurBD = MoteurBD.getMoteurBD();

        moteurBD.dropAll();

        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
        moteurBD = MoteurBD.getMoteurBD();

        GestionnaireUtilisateurs.ajouterUtilisateur(username, password);
        Assert.assertTrue(GestionnaireUtilisateurs.authentifier(username, password));
        moteurBD.dropAll();

        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
        moteurBD = MoteurBD.getMoteurBD();

        Assert.assertFalse(GestionnaireUtilisateurs.authentifier(username, password));
    }

    /**
     * Teste que les utilisateurs sont bien updaté
     */
    @Test
    public void UpdateUser(){
        GestionnaireUtilisateurs.ajouterUtilisateur("Username", "Password");
        Utilisateur u = GestionnaireUtilisateurs.getUtilisateur("Username");

        u.setEmail("email@email.email");

        Assert.assertTrue(GestionnaireUtilisateurs.update(u));
        Assert.assertEquals(u, GestionnaireUtilisateurs.getUtilisateur("Username"));
    }

    /**
     * Teste que seulement l'utilisateur qui doit etre updaté est updaté
     */
    @Test
    public void UpdateSingleUser(){
        GestionnaireUtilisateurs.ajouterUtilisateur("usr1", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr2", "");

        Utilisateur u = GestionnaireUtilisateurs.getUtilisateur("usr1");
        u.setEmail("email@email.email");

        GestionnaireUtilisateurs.update(u);
        Assert.assertNotEquals(GestionnaireUtilisateurs.getUtilisateur("usr1").getEmail(),
                GestionnaireUtilisateurs.getUtilisateur("usr2").getEmail());
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void amis(){
        GestionnaireUtilisateurs.ajouterUtilisateur("usr1", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr2", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.getUtilisateur("usr1"),
                    u2 = GestionnaireUtilisateurs.getUtilisateur("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Ami);

        Assert.assertTrue(u1.getAmis().contains(u2));
        Assert.assertTrue(u2.getAmis().contains(u1));
        Assert.assertTrue(GestionnaireUtilisateurs.getUtilisateur("usr2").getAmis().contains(u1));
        Assert.assertFalse(GestionnaireUtilisateurs.getUtilisateur("usr3").getAmis().contains(u2));
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void demandes(){
        GestionnaireUtilisateurs.ajouterUtilisateur("usr1", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr2", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.getUtilisateur("usr1"),
                    u2 = GestionnaireUtilisateurs.getUtilisateur("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Attente_de_demande_dami);

        Assert.assertTrue(u1.getDemandesAmis().contains(u2));
        Assert.assertTrue(GestionnaireUtilisateurs.getUtilisateur("usr1").getDemandesAmis().contains(u2));
        Assert.assertFalse(GestionnaireUtilisateurs.getUtilisateur("usr3").getDemandesAmis().contains(u2));
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void amiRefuse(){
        GestionnaireUtilisateurs.ajouterUtilisateur("usr1", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr2", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.getUtilisateur("usr1"),
                u2 = GestionnaireUtilisateurs.getUtilisateur("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Refusé);

        Assert.assertTrue(u1.getDemandesRefuse().contains(u2));
        Assert.assertTrue(GestionnaireUtilisateurs.getUtilisateur("usr1").getDemandesRefuse().contains(u2));
        Assert.assertFalse(GestionnaireUtilisateurs.getUtilisateur("usr3").getDemandesRefuse().contains(u2));
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void bloque(){
        GestionnaireUtilisateurs.ajouterUtilisateur("usr1", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr2", "");
        GestionnaireUtilisateurs.ajouterUtilisateur("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.getUtilisateur("usr1"),
                u2 = GestionnaireUtilisateurs.getUtilisateur("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Bloqué);

        Assert.assertTrue(u1.getBloques().contains(u2));
        Assert.assertTrue(GestionnaireUtilisateurs.getUtilisateur("usr1").getBloques().contains(u2));
        Assert.assertFalse(GestionnaireUtilisateurs.getUtilisateur("usr3").getBloques().contains(u2));
    }


}