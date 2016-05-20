package nick.echec;


import data.Defi;
import data.GestionnaireDefi;
import data.GestionnaireUtilisateurs;
import data.Utilisateur;

/**
 * C'est seulement une class pour faire des tests avec la bd local
 * Created by Keven on 2016-05-09.
 */
public class JeSuisLache {
    String pieces1[] = {"TN100", "CN101", "FN102", "KN103", "QN104", "FN205", "CN206", "TN207", "PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
            "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867", "TB170", "CB171", "FB172", "KB173", "QB174", "FB275", "CB276", "TB277"};
    String pieces2[] = {"KN100", "KB101"};

    /**
     * Constructuer de defi et user pour test
     */
    JeSuisLache(){
        GestionnaireUtilisateurs.ajouter("Bob", "abc123");
        GestionnaireUtilisateurs.ajouter("Real", "creal");
        GestionnaireUtilisateurs.ajouter("BobLeHobo", "test");

        GestionnaireDefi.ajouter("Defi1", 25, buildTonString(pieces1));
        Defi bob = GestionnaireDefi.get("Defi1");
        bob.setDifficulte(1);

        GestionnaireDefi.ajouter("Defi2", 1, buildTonString(pieces2));
        bob = GestionnaireDefi.get("Defi2");
        bob.setDifficulte(1);

        GestionnaireDefi.ajouter("Defi3", 1, buildTonString(pieces2));
        bob = GestionnaireDefi.get("Defi3");
        bob.setDifficulte(2);

        GestionnaireDefi.ajouter("Defi4", 1, buildTonString(pieces2));
        bob = GestionnaireDefi.get("Defi4");
        bob.setDifficulte(3);

        GestionnaireDefi.ajouter("Defi5", 1, buildTonString(pieces2));
        bob = GestionnaireDefi.get("Defi5");
        bob.setDifficulte(4);

        GestionnaireDefi.ajouter("Defi6", 1, buildTonString(pieces2));
        bob = GestionnaireDefi.get("Defi6");
        bob.setDifficulte(5);

        Utilisateur him = GestionnaireUtilisateurs.get("Bob");
        him = GestionnaireUtilisateurs.get("Bob");
        him.ajouterResultat(GestionnaireDefi.get("Defi1"),2,true);
    }

    /**
     * Méthode pour convertire la string[] en une seule string pour la bd local
     * @param s le string[] à convertire
     * @return une seule string
     */
    public String buildTonString(String s[]){
        StringBuilder b = new StringBuilder();
        for (String grille : s){
            b.append(grille);
            b.append(',');
        }
        return b.toString();
    }
}
