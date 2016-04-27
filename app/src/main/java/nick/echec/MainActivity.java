package nick.echec;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    //Button arrayBtn[] =new Button[64];
    RelativeLayout layout;
    ArrayList<MoveListener> lstMoveListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        lstMoveListener = new ArrayList<>();
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.relative);
        int test = layout.getChildCount();
        for (int j = 0; j < layout.getChildCount(); j++)
        {
            View v = layout.getChildAt(j);
            if (v instanceof  Button)
            {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnClickListener(this);
            }
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
