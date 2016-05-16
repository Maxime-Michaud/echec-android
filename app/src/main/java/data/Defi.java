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
     * @param toursMax Nombres de tours maximum pour réussir le défi. 0 = pas de maximum
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
        GestionnaireDefi.update(this);
    }

    /**
     * Obtiens le nombre de tours maximum pour le defi
     *
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
        GestionnaireDefi.update(this);
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

    public void setDifficulte(float difficulte) {
        nbEvaluations = 1;
        this.difficulte = difficulte;
        GestionnaireDefi.update(this);
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
        GestionnaireDefi.update(this);
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
        GestionnaireDefi.update(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Defi)) return false;

        Defi defi = (Defi) o;

        if (getId() != defi.getId()) return false;
        if (getToursMax() != defi.getToursMax()) return false;
        if (getNbEvaluations() != defi.getNbEvaluations()) return false;
        if (!getNom().equals(defi.getNom())) return false;
        return getGrille().equals(defi.getGrille());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getNom().hashCode();
        result = 31 * result + getToursMax();
        result = 31 * result + getGrille().hashCode();
        result = 31 * result + getNbEvaluations();
        return result;
    }

    /**
     * Obtiens le nombre d'évaluations du défi
     * @return
     */
    public int getNbEvaluations() {
        return nbEvaluations;
    }
}
