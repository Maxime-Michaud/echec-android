package nick.echec;

/**
 * La classe qui représente un défi. Celui-ci est composer d'un nom et d'un niveau
 * de difficulté.
 * Created by Keven on 2016-05-04.
 */
public class Defi {
    String nom;
    int niveau;         //niveau de difficulté
    boolean reussi;

    /**
     * Constructeur de défi
     * @param n est le nom du défi
     * @param niv est le niveau de difficulté du défi
     * @param r dit si le défi est réussi ou non;
     */
    public Defi(String n, int niv, boolean r) {
        nom = n;
        niveau = niv;
        reussi = r;
    }

    /**
     * Setteur du nom du défi.
     * Exemple: Tour 1
     * @param n est le nom du défi
     */
    public void setNom(String n){
        if (n == null){
            throw new AssertionError("Le nom ne peux être null");
        }
        else{
            nom = n;
        }
    }

    /**
     * Getteur du nom du défi
     * @return un string qui représente le nom du défi
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setteur du niveau de difficulté du défi.
     * Exemple: facile serait "1"
     * @param n est le niveau de difficulté du défi
     */
    public void setNiveau(int n){
            niveau = n;
    }

    /**
     * Getteur du niveau du défi
     * @return un int qui représente le niveau de difficulté du défi
     */
    public int getNiveau() {
        return niveau;
    }

    /**
     * Setteur pour dire si le défi .
     * Exemple: facile serait "1"
     * @param r est le niveau de difficulté du défi
     */
    public void setNiveau(boolean r){
        if (reussi == false){
            reussi = r;
        }
    }

    /**
     * Getteur du niveau du défi
     * @return un int qui représente le niveau de difficulté du défi
     */
    public boolean getReussi() {
        return reussi;
    }

}
