package nick.echec;

import data.GestionnaireUtilisateurs;

/**
 * Created by Keven on 2016-05-09.
 */
public class JeSuisLache {

    JeSuisLache(){
        GestionnaireUtilisateurs.ajouter("Bob", "abc123");
        GestionnaireUtilisateurs.ajouter("Real", "creal");
    }
}
