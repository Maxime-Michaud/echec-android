package nick.echec;

/**
 * Created by Keven on 2016-05-08.
 */
public class ChangementAlerte {
    private char choix;
    private boolean confirmation;

    public void setChoix(char c){
        choix = c;
    }

    public char getChoix(){
        return choix;
    }

    public void setConfirme(boolean c){
        confirmation = c;
    }

    public boolean getConfirme(){
        return confirmation;
    }
}
