package nick.echec;

import java.util.ArrayList;

/**
 * Created by Nicholas1 on 2016-05-02.
 */
public class Reine {
    public void mouvement(int x, int y,  final ArrayList<String> arrayList) {
        //Par à gauche
        int tempX, tempY;
        tempX = x - 1;
        tempY = y;
        while (tempX >= 0) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "G");
            tempX -= 1;
        }
        //Par à droite
        tempX = x + 1;
        tempY = y;
        while (tempX <= 7 && tempY >= 0) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "D");
            tempX += 1;
        }
        //Par en bas
        tempX = x;
        tempY = y + 1;
        while (tempY <= 7) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "B");
            tempY += 1;
        }
        //Par en haut
        tempX = x;
        tempY = y - 1;
        while (tempY >= 0) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "H");
            tempY -= 1;
        }
        //Par en haut à gauche
        tempX = x - 1;
        tempY = y - 1;
        while (tempX >= 0 && tempY >= 0) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "XHG");
            tempX -= 1;
            tempY -= 1;
        }
        //Par en haut à droite
        tempX = x + 1;
        tempY = y - 1;
        while (tempX <= 7 && tempY >= 0) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "XHD");
            tempX += 1;
            tempY -= 1;
        }
        //Par en bas à gauche
        tempX = x - 1;
        tempY = y + 1;
        while (tempX >= 0 && tempY <= 7) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "XBG");
            tempX -= 1;
            tempY += 1;
        }
        //Par en bas à droite
        tempX = x + 1;
        tempY = y + 1;
        while (tempX <= 7 && tempY <= 7) {
            arrayList.add(Integer.toString(tempY) + Integer.toString(tempX) + "XBD");
            tempX += 1;
            tempY += 1;
        }
    }
}
