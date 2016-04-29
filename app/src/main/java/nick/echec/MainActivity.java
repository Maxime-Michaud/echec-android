package nick.echec;

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
    ImageView imgPN1, imgPN2, imgPN3, imgPN4, imgPN5, imgPN6, imgPN7, imgPN8;
    ImageView imgPB1, imgPB2, imgPB3, imgPB4, imgPB5, imgPB6, imgPB7, imgPB8;
    ArrayList<ImageView> pionsNoirs, pionsBlancs;
    RelativeLayout.LayoutParams params;

    public static Bitmap couperImage(Bitmap bitmap, int posx, int posy) {
        int topLx, topLy;
        topLx = posx * (bitmap.getWidth() / 6);
        topLy =  posy * (bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, topLx, topLy, bitmap.getWidth() / 6, bitmap.getHeight() / 2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        lstMoveListener = new ArrayList<>();
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relative);
        for (int j = 0; j < layout.getChildCount(); j++)
        {
            View v = layout.getChildAt(j);
            if (v instanceof  Button)
            {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnClickListener(this);
            }
        }
        //top left == 5 left, 240 top; 2rangée ==
        pionsNoirs = new ArrayList<>();
        imgPN1 =new ImageView(this);imgPN2 =new ImageView(this);imgPN3 =new ImageView(this);imgPN4 =new ImageView(this);imgPN5 =new ImageView(this);imgPN6 =new ImageView(this);imgPN7 =new ImageView(this);imgPN8 =new ImageView(this);
        pionsNoirs.add(imgPN1);pionsNoirs.add(imgPN2);pionsNoirs.add(imgPN3);pionsNoirs.add(imgPN4);
        pionsNoirs.add(imgPN5);pionsNoirs.add(imgPN6);pionsNoirs.add(imgPN7);pionsNoirs.add(imgPN8);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawns);
        Bitmap bitmapTemp = couperImage(bitmap, 5, 0);
        for(int i = 0; i < 8;i++)
        {
            params = new RelativeLayout.LayoutParams(180, 180);
            pionsNoirs.get(i).setImageBitmap(bitmapTemp);
            params.leftMargin = 5 + (112 * i);
            params.topMargin = 240 + (112);
            layout.addView(pionsNoirs.get(i), params);
        }

        bitmapTemp = couperImage(bitmap, 5, 1);
        pionsBlancs = new ArrayList<>();
        imgPB1 =new ImageView(this);imgPB2 =new ImageView(this);imgPB3 =new ImageView(this);imgPB4 =new ImageView(this);imgPB5 =new ImageView(this);imgPB6 =new ImageView(this);imgPB7 =new ImageView(this);imgPB8 =new ImageView(this);
        pionsBlancs.add(imgPB1);pionsBlancs.add(imgPB2);pionsBlancs.add(imgPB3);pionsBlancs.add(imgPB4);
        pionsBlancs.add(imgPB5);pionsBlancs.add(imgPB6);pionsBlancs.add(imgPB7);pionsBlancs.add(imgPB8);
        for(int i = 0; i < 8;i++)
        {
            params = new RelativeLayout.LayoutParams(180, 180);
            pionsBlancs.get(i).setImageBitmap(bitmapTemp);
            params.leftMargin = 5 + (112 * i);
            params.topMargin = 240 + (112 * 6);
            layout.addView(pionsBlancs.get(i), params);
        }


    }

    @Override
    public void onClick(View v) {
        cliqueY = Character.getNumericValue(v.getResources().getResourceEntryName(v.getId()).charAt(3));
        cliqueX = Character.getNumericValue(v.getResources().getResourceEntryName(v.getId()).charAt(4));
        Toast.makeText(getApplicationContext(),"btn" + cliqueY + cliqueX +" appuyer",Toast.LENGTH_SHORT).show();
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
