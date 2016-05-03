package data;

/**
 * Contiens les défis pour les joueurs
 * Created by Maxime on 4/30/2016.
 */
public class Defi {
    private int id;
    private String nom;
    private int toursMax;
    private float score;
    private float difficulte;
    private String grille;
    private int nbEvaluations;

    /**
     * Initialise un défi avec toutes ses propriétés
     * @param id
     * @param nom
     * @param toursMax Nombres de tours maximum pour réussir e défi. 0 = pas de maximum
     * @param score
     * @param difficulte
     * @param grille
     * @param nbEvaluations
     */
    Defi(int id, String nom, int toursMax, float score, float difficulte, String grille, int nbEvaluations) {
        this.id = id;
        this.nom = nom;
        this.toursMax = toursMax;
        this.score = score;
        this.difficulte = difficulte;
        this.grille = grille;
        this.nbEvaluations = nbEvaluations;
    }

    /**
     * Obtiens l'id dans la bd pour le defi
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiens le nom du defi
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Assigne le nom du defi
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtiens le nombre de tours maximum pour le defi
     * @return
     */
    public int getToursMax() {
        return toursMax;
    }

    /**
     * Assigne le nombre de tours maximum
     * @param toursMax
     */
    public void setToursMax(int toursMax) {
        this.toursMax = toursMax;
    }

    /**
     * Obtiens le score moyen donné par les utilisateurs
     * @return
     */
    public float getScore() {
        return score;
    }

    /**
     * Obtiens la difficulté moyenne donné par les utilisateurs
     * @return
     */
    public float getDifficulte() {
        return difficulte;
    }

    /**
     * Modifie la difficulté du défi.
     * @param score Score donné par l'utilisateur
     * @param difficulte Difficulté donné par l'utilisateur
     */
    public void evaluer(float score, float difficulte)
    {
        ++nbEvaluations;
        this.score = (this.score * (nbEvaluations - 1) + score) / nbEvaluations;
        this.difficulte = (this.difficulte * (nbEvaluations - 1) + difficulte) / nbEvaluations;
    }

    /**
     * Obtiens la grille du defi
     * @return
     */
    public String getGrille() {
        return grille;
    }

    /**
     * Assigne la grille au defi
     * @param grille
     */
    public void setGrille(String grille) {
        this.grille = grille;
    }

    /**
     * Obtiens le nombre d'évaluations du défi
     * @return
     */
    public int getNbEvaluations() {
        return nbEvaluations;
    }
}
