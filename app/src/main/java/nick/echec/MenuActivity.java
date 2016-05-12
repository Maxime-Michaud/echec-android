package nick.echec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import data.MoteurBD;

/**
 * Le menu principale du jeu. Il permet d'accèder aux autres éléments de l'application: le jeu,
 * les défis et les options
 * Created by Keven on 2016-05-02.
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button connexion;
    private Button jouer;
    private Button defi;
    private Button option;
    JeSuisLache    j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        MoteurBD.init(this);
        j = new JeSuisLache();

        connexion = (Button) findViewById(R.id.btnConnexion);
        jouer = (Button) findViewById(R.id.btnJouer);
        defi = (Button) findViewById(R.id.btnEntrainement);
        option = (Button) findViewById(R.id.btnOption);

        connexion.setOnClickListener(this);
        jouer.setOnClickListener(this);
        defi.setOnClickListener(this);
        option.setOnClickListener(this);
    }

    /**
     * Méthode qui gère les évènements "onClick" du projet.
     * @param v est l'object qui est cliquer / appele la méthode. Exemple: un Button.
     */
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnConnexion:
                connexion();
                break;

            case R.id.btnJouer:
                demarerPartie();
                break;

            case R.id.btnEntrainement:
                ouvrireDefi();
                break;

            case R.id.btnOption:
                ouvrireOption();
                break;

            default:
                break;
        }
    }

    public void connexion(){
        Intent secondeActivite = new Intent(MenuActivity.this, ConnexionActivity.class);
        startActivity(secondeActivite);
    }

    /**
     * Méthode qui démarre "MainActivity" qui est la classe pour jouer la
     * partie d'échec.
     */
    public void demarerPartie(){
            Intent secondeActivite = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(secondeActivite);
    }

    /**
     * Méthodes qui démarre "DefiActivity" qui est la classe qui gère les défis.
     */
    public void ouvrireDefi(){
        Intent secondeActivite = new Intent(MenuActivity.this, DefiActivity.class);
        startActivity(secondeActivite);
    }

    /**
     * Méthode qui démarre "OptionActivity" qui est la classe qui gère
     * les options du jeu.
     */
    public void ouvrireOption(){
        Intent secondeActivite = new Intent(MenuActivity.this, OptionActivity.class);
        startActivity(secondeActivite);
    }

}
