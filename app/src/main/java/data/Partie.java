package data;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente une partie
 * Created by Maxime on 4/30/2016.
 */
public class Partie {
    @Nullable
    private Utilisateur blanc, noir;
    @Nullable
    private String grille;
    @Nullable
    private Utilisateur gagnant;

    private Map<Integer, String> tours;

    /**
     * Crée une partie.
     *
     * @param blanc   Joueur blanc. Null représente l'IA
     * @param noir    Joueur noir. Null représente l'IA
     * @param gagnant Gagnant de la partie. Doit être le joueur blanc ou le noir.
     * @param grille  Grille de la partie. Null représente la grille par défaut
     */
    Partie(@Nullable Utilisateur blanc, @Nullable Utilisateur noir, @Nullable Utilisateur gagnant, @Nullable String grille) {
        if (blanc != null && blanc.equals(noir))
            throw new IllegalArgumentException();

        if (blanc != gagnant && gagnant != noir)
            throw new IllegalArgumentException();

        this.blanc = blanc;
        this.noir = noir;
        this.gagnant = gagnant;
        this.grille = grille;
        tours = new HashMap<>();
    }

    /**
     * Obtiens le joueur blanc
     *
     * @return Joueur blanc
     */
    @Nullable
    public Utilisateur getBlanc() {
        return blanc;
    }

    /**
     * Obtiens le joueur noir
     *
     * @return Joueur noir
     */
    @Nullable
    public Utilisateur getNoir() {
        return noir;
    }

    /**
     * Obtiens la grille
     *
     * @return La grille de la partie.
     */
    @Nullable
    public String getGrille() {
        return grille;
    }

    /**
     * Obtiens le gagnant
     *
     * @return Gagnant
     */
    @Nullable
    public Utilisateur getGagnant() {
        return gagnant;
    }

    /**
     * Obtiens les tours
     *
     * @return les tours
     */
    public Map<Integer, String> getTours() {
        return Collections.unmodifiableMap(tours);
    }
}
