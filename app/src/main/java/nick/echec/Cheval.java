package nick.echec;

import java.util.ArrayList;

/**
 * Created by Nicholas1 on 2016-05-02.
 */
public class Cheval {
    public void mouvement(int x, int y, final ArrayList<String> arrayList)
    {
        //Par en haut à gauche
        if(y - 1 >= 0 && x - 2 >= 0)
            arrayList.add(Integer.toString(y-1) +  Integer.toString(x -2) + "E");
        if(y - 2 >= 0 && x - 1 >= 0)
            arrayList.add(Integer.toString(y-2) +  Integer.toString(x -1) + "E");
        //Par en haut à droite
        if(y - 1 >= 0 && x + 2 <= 7)
            arrayList.add(Integer.toString(y-1) +  Integer.toString(x + 2) + "E");
        if(y - 2 >= 0 && x + 1 <= 7)
            arrayList.add(Integer.toString(y-2) +  Integer.toString(x + 1) + "E");
        //Par en bas à gauche
        if(y + 1 <= 7 && x - 2 >= 0)
            arrayList.add(Integer.toString(y+1) +  Integer.toString(x - 2) + "E");
        if(y + 2 <= 7 && x - 1 >= 0)
            arrayList.add(Integer.toString(y+2) +  Integer.toString(x - 1) + "E");
        //Par en bas à droite
        if(y + 1 <= 7 && x + 2 <= 7)
            arrayList.add(Integer.toString(y+1) +  Integer.toString(x + 2) + "D");
        if(y + 2 <= 7 && x + 1 <= 7)
            arrayList.add(Integer.toString(y+2) +  Integer.toString(x + 1) + "D");
    }
}
