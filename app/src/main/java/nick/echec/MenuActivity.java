package nick.echec;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Le menu principale du jeu. Il permet d'accèder aux autres éléments de l'application: le jeu,
 * les défis et les options
 * Created by Keven on 2016-05-02.
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button connection;
    private Button jouer;
    private Button defi;
    private Button option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        connection = (Button) findViewById(R.id.btnConnection);
        jouer = (Button) findViewById(R.id.btnJouer);
        defi = (Button) findViewById(R.id.btnEntrainement);
        option = (Button) findViewById(R.id.btnOption);

        connection.setOnClickListener(this);
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

            case R.id.btnConnection:
                //connection();
                alert();
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

    public void alert(){
        final char[] choix = new char[1];
        ChangementAlerte ca = new ChangementAlerte();

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alerte_changement, null);

        //Création de l'AlertDialog
        AlertDialog alert = new AlertDialog.Builder(this).create();

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        alert.setView(alertDialogView);

        Button tour = (Button)alertDialogView.findViewById(R.id.btnTour);//ca.getTour();
        Button cavalier = (Button)alertDialogView.findViewById(R.id.btnCheval);
        Button fou = (Button)alertDialogView.findViewById(R.id.btnFou);
        Button reine = (Button)alertDialogView.findViewById(R.id.btnReine);
        Button ok = (Button)alertDialogView.findViewById(R.id.btnACok);

        tour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ca.setChoix('T');
                ca.setConfirme(true);
            }
        });

        cavalier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.setChoix('C');
                ca.setConfirme(true);
            }
        });

        fou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.setChoix('F');
                ca.setConfirme(true);
            }
        });

        reine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ca.setChoix('Q');
                ca.setConfirme(true);
        }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ca.getConfirme()){
                    choix[0] = ca.getChoix();
                    alert.dismiss();
                }
            }
        });

        alert.show();
    }

    /**
     * Méthode qui démarre "ConnectionActivity" qui est la classe pour se
     * connecter à son compte
     */
    public void connection(){
        Intent secondeActivite = new Intent(MenuActivity.this, ConnectionActivity.class);
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
