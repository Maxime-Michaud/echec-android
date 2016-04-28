package nick.echec;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Nicholas1 on 2016-04-27.
 */
public class viewPiece extends View {

    private ImageView imgView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    viewPiece(Context c, AttributeSet attrs, int def, int def2)
    {
        super(c, attrs,def, def2);
        imgView = new ImageView(c);
        imgView.setImageResource(R.drawable.chesspawn);
    }

    @Override
    public void draw(Canvas c)
    {
        super.draw(c);
    }
}
