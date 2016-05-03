package nick.echec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Keven on 2016-05-02.
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button jouer;
    private Button defi;
    private Button option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        jouer = (Button) findViewById(R.id.btnJouer);
        defi = (Button) findViewById(R.id.btnEntrainement);
        option = (Button) findViewById(R.id.btnOption);

        jouer.setOnClickListener(this);
        defi.setOnClickListener(this);
        option.setOnClickListener(this);
    }

    /**
     * Méthode qui gère les évènements "onClick" du projet
     * @param v est l'object qui est cliquer / appele la méthode. Exemple: un Button
     */
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnJouer:
                demarerPartie();
                break;

            case R.id.btnEntrainement:
                //ouvrireDefi();
                break;

            case R.id.btnOption:
                //ouvrireOption();
                break;

            default:
                break;
        }
    }

    public void demarerPartie(){
            Intent secondeActivite = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(secondeActivite);
    }
/*
    public void ouvrireOption(){
        setContentView(R.layout.activity_menu_option);
    }

    public void ouvrireDefi(){
        setContentView(R.layout.activity_menu_defi);
    }*/
}
