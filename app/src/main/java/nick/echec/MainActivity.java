package nick.echec;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ClipDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int cliqueX, cliqueY;
    /* Button btn00, btn01, btn02, btn03, btn04, btn05, btn06, btn07,
     btn10, btn11, btn12, btn13, btn14, btn15, btn16, btn17,
     btn20, btn21, btn22, btn23, btn24, btn25, btn26, btn27,
     btn30, btn31, btn32, btn33, btn34, btn35, btn36, btn37,
     btn40, btn41, btn42, btn43, btn44, btn45, btn46, btn47,
     btn50, btn51, btn52, btn53, btn54, btn55, btn56, btn57,
     btn60, btn61, btn62, btn63, btn64, btn65, btn66, btn67,
     btn70, btn71, btn72, btn73, btn74, btn75, btn76, btn77;*/
    RelativeLayout layout;
    ArrayList<MoveListener> lstMoveListener;
    ImageView PN1, PN2, PN3, PN4, PN5, PN6, PN7, PN8;
    ImageView PB1, PB2, PB3, PB4, PB5, PB6, PB7, PB8;
    ImageView TN1, TN2, FN1, FN2, CN1, CN2, KN1, QN1;
    ImageView TB1, TB2, FB1, FB2, CB1, CB2, KB1, QB1;
    ArrayList<ImageView> pionsNoirs, pionsBlancs;
    RelativeLayout.LayoutParams params;
    Bitmap bitmap;
    Resources r;
    boolean[] premierTourPionsNoirs = new boolean[8];
    boolean premierTourPionsBlancs[] = new boolean[8];
    boolean tourBlanc = true;
    ArrayList<String> mouvDispos;
    String pieces[] = {"TN100", "CN101", "FN102", "KN103", "QN104", "FN205", "CN206", "TN207", "PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
            "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867", "TB170", "CB171", "FB172", "KB173", "QB174", "FB275", "CB276", "TB277"};

    public static Bitmap couperImage(Bitmap bitmap, int posx, int posy) {
        int topLx, topLy;
        topLx = posx * (bitmap.getWidth() / 6);
        topLy = posy * (bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, topLx, topLy, bitmap.getWidth() / 6, bitmap.getHeight() / 2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        lstMoveListener = new ArrayList<>();
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relative);
        r = getResources();
        mouvDispos = new ArrayList<>();
        for(int i = 0;i < 8;i++)
        {
            premierTourPionsBlancs[i] = true;
            premierTourPionsNoirs[i] = true;
        }
        for (int j = 0; j < layout.getChildCount(); j++) {
            View v = layout.getChildAt(j);
            if (v instanceof Button) {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnClickListener(this);
            }
        }
        pionsNoirs = new ArrayList<>();
        PN1 = new ImageView(this);
        PN2 = new ImageView(this);
        PN3 = new ImageView(this);
        PN4 = new ImageView(this);
        PN5 = new ImageView(this);
        PN6 = new ImageView(this);
        PN7 = new ImageView(this);
        PN8 = new ImageView(this);
        pionsNoirs.add(PN1);
        pionsNoirs.add(PN2);
        pionsNoirs.add(PN3);
        pionsNoirs.add(PN4);
        pionsNoirs.add(PN5);
        pionsNoirs.add(PN6);
        pionsNoirs.add(PN7);
        pionsNoirs.add(PN8);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawns);
        for (int i = 0; i < 8; i++) {
            setPawn(pionsNoirs.get(i),i,1,5,0);
        }
        pionsBlancs = new ArrayList<>();
        PB1 = new ImageView(this);
        PB2 = new ImageView(this);
        PB3 = new ImageView(this);
        PB4 = new ImageView(this);
        PB5 = new ImageView(this);
        PB6 = new ImageView(this);
        PB7 = new ImageView(this);
        PB8 = new ImageView(this);
        pionsBlancs.add(PB1);
        pionsBlancs.add(PB2);
        pionsBlancs.add(PB3);
        pionsBlancs.add(PB4);
        pionsBlancs.add(PB5);
        pionsBlancs.add(PB6);
        pionsBlancs.add(PB7);
        pionsBlancs.add(PB8);
        for (int i = 0; i < 8; i++) {
            setPawn(pionsBlancs.get(i),i,6,5,1);
        }
        TN1 = new ImageView(this);
        setPawn(TN1, 0, 0, 2, 0);
        TN2 = new ImageView(this);
        setPawn(TN2, 7, 0, 2, 0);
        FN1 = new ImageView(this);
        setPawn(FN1, 2, 0, 3, 0);
        FN2 = new ImageView(this);
        setPawn(FN2,5,0,3,0);
        CN1 = new ImageView(this);
        setPawn(CN1, 1, 0, 4, 0);
        CN2 = new ImageView(this);
        setPawn(CN2, 6, 0, 4, 0);
        KN1 = new ImageView(this);
        setPawn(KN1,3,0,1,0);
        QN1 = new ImageView(this);
        setPawn(QN1,4,0,0,0);
        
        TB1 = new ImageView(this);
        setPawn(TB1,0,7,2,1);
        TB2 = new ImageView(this);
        setPawn(TB2,7,7,2,1);
        FB1 = new ImageView(this);
        setPawn(FB1,2,7,3,1);
        FB2 = new ImageView(this);
        setPawn(FB2,5,7,3,1);
        CB1 = new ImageView(this);
        setPawn(CB1,1,7,4,1);
        CB2 = new ImageView(this);
        setPawn(CB2,6,7,4,1);
        KB1 = new ImageView(this);
        setPawn(KB1,3,7,1,1);
        QB1 = new ImageView(this);
        setPawn(QB1,4,7,0,1);


    }

    public void setPawn(final ImageView img,int posX, int posY, int imgX, int imgY)
    {
        Bitmap bitmapTemp = couperImage(bitmap, imgX, imgY);
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8 + (37 * posX), r.getDisplayMetrics());
        int py = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108 + (37 * posY), r.getDisplayMetrics());
        int test180 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        params = new RelativeLayout.LayoutParams(test180, test180);
        img.setImageBitmap(bitmapTemp);
        params.leftMargin = px;
        params.topMargin = py;
        layout.addView(img, params);
    }

    @Override
    public void onClick(View v) {
        cliqueY = Character.getNumericValue(v.getResources().getResourceEntryName(v.getId()).charAt(3));
        cliqueX = Character.getNumericValue(v.getResources().getResourceEntryName(v.getId()).charAt(4));
        for(String s : pieces)
        {
            String caseClique = String.valueOf(cliqueY) + String.valueOf(cliqueX);
            String temp = String.valueOf(s.charAt(3)) + s.charAt(4);
            if (caseClique.equals(temp))
            {
                String nomCouleur;
                switch (s.charAt(1))
                {
                    case 'N':
                        nomCouleur = "noir";
                        break;
                    case 'B':
                        nomCouleur = "blanc";
                        break;
                    default:
                        nomCouleur = "AUCUNECOULEUR";
                        break;
                }
                boolean valide = (tourBlanc && nomCouleur == "blanc") ||(!tourBlanc && nomCouleur == "noir");
                if(valide)
                {
                    String nomPiece;
                    switch (s.charAt(0))
                    {
                        case 'T':
                            nomPiece = "Tour";
                            break;
                        case 'C':
                            nomPiece = "Cheval";
                            break;
                        case 'F':
                            nomPiece = "Fou";
                            break;
                        case 'Q':
                            nomPiece = "Reine";
                            break;
                        case 'K':
                            nomPiece = "Roi";
                            break;
                        case 'P':
                            nomPiece = "Pion";
                            MouvPionPossible(cliqueX,cliqueY,s);
                            break;
                        default:
                            nomPiece = "AUCUNNOM";
                            break;

                    }
                    if(tourBlanc)
                        layout.setBackgroundColor(Color.BLACK);
                    else
                        layout.setBackgroundColor(Color.WHITE);
                    tourBlanc= !tourBlanc;
                    Toast.makeText(getApplicationContext(),"mouv dispo: " + (mouvDispos.size() > 0? mouvDispos.get(0):null) + " " + (mouvDispos.size() > 1? mouvDispos.get(1):null),Toast.LENGTH_SHORT).show();
                    mouvDispos.clear();
                    return;
                }

            }
        }
    }

    public void MouvPionPossible(int posx, int posy, String nom)
    {
        if(nom.charAt(1) == 'B')
        {
            int pos = (Character.getNumericValue(nom.charAt(2))) - 1;
            if (premierTourPionsBlancs[pos])
            {
                for(int i = 0; i < 2;i++)
                {
                    if (posy - i >= 0)
                    {
                        for(int j = 0; j < pieces.length;j++)
                        {
                            if(Character.getNumericValue(pieces[j].charAt(3)) == posx && Character.getNumericValue(pieces[j].charAt(4)) == posy)
                            {
                                continue;
                            }
                        }
                        mouvDispos.add(Integer.toString(posy - (i + 1)) + Integer.toString(posx));
                    }

                }
            }
        }
    }

    public void addMoveListener(MoveListener ml)
    {
        lstMoveListener.add(ml);
    }

    public void doMove()
    {
        for(MoveListener v : lstMoveListener)
        {
            v.onMove();
        }
    }
}
