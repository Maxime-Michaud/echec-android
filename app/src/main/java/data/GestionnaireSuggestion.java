package data;

/**
 * Gère l'ajout des suggestions a la bd
 * Created by Maxime on 5/10/2016.
 */
public class GestionnaireSuggestion {
    /**
     * Ajoute une suggestion a la base de donnée
     *
     * @param u    Utilisateur qu'on ajoute a la bd
     * @param type type de défi a ajouter
     * @param diff niveau de difficulté
     * @return si la suggestion a été ajoutée
     */
    public static boolean ajouter(Utilisateur u, Type_Defi type, float diff) {
        //TODO
        throw new UnsupportedOperationException("lol fuck you");
    }

    public enum Type_Defi {
        TEMPS,
        PION,
        TOUR,
        CAVALIER,
        FOU,
        REINE,
        ROI
    }
}
