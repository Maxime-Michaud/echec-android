package data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestsDBUtilisateur {
    MoteurBD moteurBD;

    @Before
    public void setUp() throws Exception {
        MoteurBD.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        MoteurBD.getMoteurBD().dropAll();
        GestionnaireUtilisateurs.effacerEnregistrer();
    }

    /**
     * Test qu'un utilisateur peut s'authentifier
     */
    @Test
    public void Authentification(){
        final String password = "1234",
                username = "Abcd";

        GestionnaireUtilisateurs.ajouter(username, password);
        Assert.assertTrue(GestionnaireUtilisateurs.authentifier(username, password));
    }

    /**
     * Test que l'authentification fail si l'utilisateur n'a pas le bon username
     */
    @Test
    public void AuthentificationFail(){
        final String password = "1234",
                username = "Abcd";

        GestionnaireUtilisateurs.ajouter(username, password);
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
        Assert.assertTrue(GestionnaireUtilisateurs.ajouter("Abcd", "1234"));
    }

    /**
     * Test qu'on ne peut pas ajouter deux user avec le meme username
     */
    @Test
    public void AjouterUtilisateurMemeLogin(){
        final String password = "1234",
                username = "Abcd";

        GestionnaireUtilisateurs.ajouter(username, password);
        Assert.assertFalse(GestionnaireUtilisateurs.ajouter(username, "NOT " + password));
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

        GestionnaireUtilisateurs.ajouter(username, password);
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
        GestionnaireUtilisateurs.ajouter("Username", "Password");
        Utilisateur u = GestionnaireUtilisateurs.get("Username");

        u.setEmail("email@email.email");

        Assert.assertTrue(GestionnaireUtilisateurs.update(u));
        Assert.assertEquals(u, GestionnaireUtilisateurs.get("Username"));
    }

    /**
     * Teste que seulement l'utilisateur qui doit etre updaté est updaté
     */
    @Test
    public void UpdateSingleUser(){
        GestionnaireUtilisateurs.ajouter("usr1", "");
        GestionnaireUtilisateurs.ajouter("usr2", "");

        Utilisateur u = GestionnaireUtilisateurs.get("usr1");
        u.setEmail("email@email.email");

        GestionnaireUtilisateurs.update(u);
        Assert.assertNotEquals(GestionnaireUtilisateurs.get("usr1").getEmail(),
                GestionnaireUtilisateurs.get("usr2").getEmail());
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void amis(){
        GestionnaireUtilisateurs.ajouter("usr1", "");
        GestionnaireUtilisateurs.ajouter("usr2", "");
        GestionnaireUtilisateurs.ajouter("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.get("usr1"),
                    u2 = GestionnaireUtilisateurs.get("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Ami);

        Assert.assertTrue(u1.getAmis().contains(u2));
        Assert.assertTrue(u2.getAmis().contains(u1));
        Assert.assertTrue(GestionnaireUtilisateurs.get("usr2").getAmis().contains(u1));
        Assert.assertFalse(GestionnaireUtilisateurs.get("usr3").getAmis().contains(u2));
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void demandes(){
        GestionnaireUtilisateurs.ajouter("usr1", "");
        GestionnaireUtilisateurs.ajouter("usr2", "");
        GestionnaireUtilisateurs.ajouter("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.get("usr1"),
                    u2 = GestionnaireUtilisateurs.get("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Attente_de_demande_dami);

        Assert.assertTrue(GestionnaireUtilisateurs.get("usr2").getDemandesAmis().contains(u1));
        Assert.assertFalse(GestionnaireUtilisateurs.get("usr1").getDemandesAmis().contains(u2));
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void amiRefuse(){
        GestionnaireUtilisateurs.ajouter("usr1", "");
        GestionnaireUtilisateurs.ajouter("usr2", "");
        GestionnaireUtilisateurs.ajouter("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.get("usr1"),
                u2 = GestionnaireUtilisateurs.get("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Refusé);

        Assert.assertTrue(u1.getDemandesRefuse().contains(u2));
        Assert.assertTrue(GestionnaireUtilisateurs.get("usr1").getDemandesRefuse().contains(u2));
        Assert.assertFalse(GestionnaireUtilisateurs.get("usr3").getDemandesRefuse().contains(u2));
    }

    /**
     * Teste que les relations sont bien ajoutées
     */
    @Test
    public void bloque(){
        GestionnaireUtilisateurs.ajouter("usr1", "");
        GestionnaireUtilisateurs.ajouter("usr2", "");
        GestionnaireUtilisateurs.ajouter("usr3", "");

        Utilisateur u1 = GestionnaireUtilisateurs.get("usr1"),
                u2 = GestionnaireUtilisateurs.get("usr2");

        GestionnaireUtilisateurs.ajouterRelation(u1, u2, Utilisateur.RELATION.Bloqué);

        Assert.assertTrue(u1.getBloques().contains(u2));
        Assert.assertTrue(GestionnaireUtilisateurs.get("usr1").getBloques().contains(u2));
        Assert.assertFalse(GestionnaireUtilisateurs.get("usr3").getBloques().contains(u2));
    }


}