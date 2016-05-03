package nick.echec;

import java.util.ArrayList;

/**
 * Created by Nick on 2016-04-29.
 */
public class Pion {

    public void mouvement(int x, int y, boolean premierMove, char couleur, final ArrayList<String> arrayList)
    {
        if(couleur == 'B')
        {
            if (premierMove)
            {
                for(int i = 0; i < 2;i++)
                {
                    if (y - i >= 0)
                    {
                        arrayList.add(Integer.toString(y - (i + 1)) + Integer.toString(x) + "H");
                    }

                }
            }
            else
            {
                if (y - 1 >= 0)
                {
                    arrayList.add(Integer.toString(y - 1) + Integer.toString(x) + "C");
                }
            }
            if(y-1 >= 0)
            {
                if(x-1 >= 0)
                    arrayList.add(Integer.toString(y - 1) + Integer.toString(x -1) + "A");
                if(x+1 <= 7)
                    arrayList.add(Integer.toString(y - 1) + Integer.toString(x + 1) + "A");
            }

        }
        else if(couleur == 'N')
        {
            if (premierMove)
            {
                for(int i = 0; i < 2;i++)
                {
                    if (y + i <= 7)
                    {
                        arrayList.add(Integer.toString(y + (i + 1)) + Integer.toString(x) + "B");
                    }

                }
            }
            else
            {
                if (y + 1 <= 7)
                {
                    arrayList.add(Integer.toString(y + 1) + Integer.toString(x) + "C");
                }
            }
            if(y+1 <= 7)
            {
                if(x -1 >= 0)
                    arrayList.add(Integer.toString(y + 1) + Integer.toString(x -1) + "A");
                if(x+1 <=7)
                    arrayList.add(Integer.toString(y + 1) + Integer.toString(x + 1) + "A");
            }
        }
    }
}
