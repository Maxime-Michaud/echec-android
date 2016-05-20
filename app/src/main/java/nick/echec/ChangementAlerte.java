package nick.echec;

/**
 * Classe qui permet de changer de type de pièce lorsqu'un pion se rend au bout de la
 * grille de jeu. C'est choix son: tour, cavalier, fou et la reine;
 * Created by Keven on 2016-05-08.
 */
public class ChangementAlerte {
    private char choix;
    private boolean confirmation;

    /**
     * Setteur du choix de la pièce
     * @param c un char qui représente la pièce. Exemple: F = fou
     */
    public void setChoix(char c){
        choix = c;
    }

    /**
     * Getteur du choix de la pièce
     * @return un char qui représente la pièce choisit. Exemple: F = fou
     */
    public char getChoix(){
        return choix;
    }

    /**
     * setteur de confimation du choix
     * @param c un boolean qui représente true si l'utilisateur à cliquer sur la pièce
     */
    public void setConfirme(boolean c){
        confirmation = c;
    }

    /**
     * getteur de la confirmation du choix
     * @return un boolean
     */
    public boolean getConfirme(){
        return confirmation;
    }
}
