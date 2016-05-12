package nick.echec;

import data.GestionnaireDefi;
import data.GestionnaireUtilisateurs;

/**
 * Created by Keven on 2016-05-09.
 */
public class JeSuisLache {
    String pieces1[] = {"TN100", "CN101", "FN102", "KN103", "QN104", "FN205", "CN206", "TN207", "PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
            "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867", "TB170", "CB171", "FB172", "KB173", "QB174", "FB275", "CB276", "TB277"};
    String pieces2[] = {"KN100", "KB101"};

    JeSuisLache(){
        GestionnaireUtilisateurs.ajouter("Bob", "abc123");
        GestionnaireUtilisateurs.ajouter("Real", "creal");
        GestionnaireDefi.ajouter("Defi trop facile", 1, buildTonString(pieces1));
        GestionnaireDefi.ajouter("Defi moins facile", 25, buildTonString(pieces2));
    }

    public String buildTonString(String s[]){
        StringBuilder b = new StringBuilder();
        for (String grille : s){
            b.append(grille);
            b.append(',');
        }
        return b.toString();
    }
}
