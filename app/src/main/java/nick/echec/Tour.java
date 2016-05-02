package nick.echec;

import java.util.ArrayList;

/**
 * Created by Nicholas1 on 2016-05-02.
 */
public class Tour
{
    public ArrayList<String> mouvement(int x, int y)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        //Par à gauche
        int tempX, tempY;
        tempX = x - 1;
        tempY = y;
        while(tempX >= 0)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "G");
            tempX -= 1;
        }
        //Par à droite
        tempX = x + 1;
        tempY = y;
        while(tempX <= 7 && tempY >= 0)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "D");
            tempX += 1;
        }
        //Par en bas
        tempX = x;
        tempY = y + 1;
        while(tempY <= 7)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "B");
            tempY += 1;
        }
        //Par en haut
        tempX = x;
        tempY = y - 1;
        while(tempY >= 0)
        {
            arrayList.add(Integer.toString(tempY) +  Integer.toString(tempX) + "H");
            tempY -= 1;
        }
        return arrayList;
    }
}
