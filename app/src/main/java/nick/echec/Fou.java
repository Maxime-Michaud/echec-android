package nick.echec;

import java.util.ArrayList;

/**
 * Created by Nick on 2016-05-01.
 */
public class Fou {
    public ArrayList<String> mouvement(int x, int y)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        //Par en haut à gauche
        int tempX, tempY;
        tempX = x - 1;
        tempY = y - 1;
        while(tempX >= 0 && tempY >= 0)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "XHG");
            tempX -= 1;
            tempY -= 1;
        }
        //Par en haut à droite
        tempX = x + 1;
        tempY = y - 1;
        while(tempX <= 7 && tempY >= 0)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "XHD");
            tempX += 1;
            tempY -= 1;
        }
        //Par en bas à gauche
        tempX = x - 1;
        tempY = y + 1;
        while(tempX >= 0 && tempY <= 7)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "XBG");
            tempX -= 1;
            tempY += 1;
        }
        //Par en bas à droite
        tempX = x + 1;
        tempY = y + 1;
        while(tempX <= 7 && tempY <= 7)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "XBD");
            tempX += 1;
            tempY += 1;
        }
        return arrayList;
    }
}
