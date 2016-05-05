package nick.echec;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui permet de gerer les options du jeu, comme le niveau de difficulté de l'IA
 * Created by Keven on 2016-05-03.
 */
public class OptionActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private Button retour;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        setSpinner();
        retour = (Button)findViewById(R.id.btnRetour);
        retour.setOnClickListener(this);
        }

    /**
     * Méthode qui gère les évènements "onClick" de la classe.
     * @param v est l'object qui est cliquer / appele la méthode. Exemple: un Button.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRetour:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * méthode qui permet de setter le spinner
     */
    public void setSpinner(){
        //Récupération du Spinner déclaré dans le fichier activity_option.xml
        spinner = (Spinner) findViewById(R.id.choixia);

        /*Création d'une liste d'élément à mettre dans le Spinner
        * Le niveau de difficulté est représenter par un nombre, mais pourrait être changé
        * pour "facile, moyen..."
        */
        List niveau = new ArrayList();
        niveau.add("Facile");
        niveau.add("Normal");

        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                niveau
        );


        //On definit une présentation du spinner quand il est déroulé
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
