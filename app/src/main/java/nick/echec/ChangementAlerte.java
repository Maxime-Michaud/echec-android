package nick.echec;

/**
 * Created by Keven on 2016-05-08.
 */
public class ChangementAlerte{

    private boolean confirme;
    private char choix;

    ChangementAlerte(){
        confirme = false;
    }

    /**
     * Getteur du choix
     * @return
     */
    public char getChoix(){
        return choix;
    }

    public void setChoix(char c){
        choix = c;
    }

    public boolean getConfirme(){
        return confirme;
    }

    public void setConfirme(boolean b){
        confirme = b;
    }
}
