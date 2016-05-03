package nick.echec;

import java.util.ArrayList;

/**
 * Created by Nicholas1 on 2016-05-02.
 */
public class Roi {
    public void mouvement(int x, int y,  final ArrayList<String> arrayList)
    {
        //Par en haut
        if(y - 1 >= 0)
            arrayList.add(Integer.toString(y-1) +  Integer.toString(x) + "K");
        //Par a gauche
        if(x - 1 >= 0)
            arrayList.add(Integer.toString(y) +  Integer.toString(x - 1) + "K");
        //Par en bas
        if(y + 1 <= 7)
            arrayList.add(Integer.toString(y+1) +  Integer.toString(x) + "K");
        //Par à droite
        if(x + 1 <= 7)
            arrayList.add(Integer.toString(y) +  Integer.toString(x + 1) + "K");
        //Par en haut à gauche
        if(y - 1 >= 0 && x - 1 >= 0)
            arrayList.add(Integer.toString(y-1) +  Integer.toString(x - 1) + "K");
        //Par en haut à droite
        if(y - 1 >= 0 && x + 1 <= 7)
            arrayList.add(Integer.toString(y-1) +  Integer.toString(x + 1) + "K");
        //Par en bas à gauche
        if(y + 1 <= 7 && x - 1 >= 0)
            arrayList.add(Integer.toString(y+1) +  Integer.toString(x - 1) + "K");
        //Par en bas à droite
        if(y + 1 <= 7 && x + 1 <= 7)
            arrayList.add(Integer.toString(y+1) +  Integer.toString(x + 1) + "K");
    }
}
