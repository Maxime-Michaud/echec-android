package data;

import android.support.annotation.NonNull;

/**
 * Enregistre les résultats d'un défi
 * Created by Maxime on 5/4/2016.
 */
public class ResultatDefi {
    private Defi defi;
    private int nbTour;
    private boolean reussi;

    /**
     * Crée le résultat d'un défi
     * @param defi défi
     * @param nbTour nombre de tour utilisés
     * @param reussi Si l'utilisateur a réussi ou non
     */
    ResultatDefi(@NonNull Defi defi, int nbTour, boolean reussi) {
        this.defi = defi;
        this.nbTour = nbTour;
        this.reussi = reussi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultatDefi)) return false;

        ResultatDefi that = (ResultatDefi) o;

        if (getNbTour() != that.getNbTour()) return false;
        if (isReussi() != that.isReussi()) return false;
        return getDefi().equals(that.getDefi());

    }

    @Override
    public int hashCode() {
        int result = getDefi().hashCode();
        result = 31 * result + getNbTour();
        result = 31 * result + (isReussi() ? 1 : 0);
        return result;
    }

    /**
     * Obtiens le defi concerné
     * @return
     */
    public Defi getDefi() {
        return defi;
    }

    /**
     * Obtiens le nombre de tours utilisé par le joueur
     * @return
     */
    public int getNbTour() {
        return nbTour;
    }

    /**
     * Obtiense si le défi est réussi
     * @return
     */
    public boolean isReussi() {
        return reussi;
    }
}
