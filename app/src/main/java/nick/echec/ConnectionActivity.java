package nick.echec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.GestionnaireUtilisateurs;

/**
 * Classe qui permet à l'utilisateur de se connecter
 * Created by Keven on 2016-05-06.
 */
public class ConnectionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button connect;
    private Button inscription;
    private Button retour;
    private EditText nomUti;
    private EditText mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        connect = (Button)findViewById(R.id.btnConnect);
        inscription = (Button)findViewById(R.id.btnIns);
        retour = (Button)findViewById(R.id.btnCretour);

        nomUti = (EditText)findViewById(R.id.etUserName);
        mdp = (EditText) findViewById(R.id.etMDP);

        //Ajoute des onClickListener ou différent elements
        connect.setOnClickListener(this);
        inscription.setOnClickListener(this);
        retour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnConnection:
                connection();
                break;

            case R.id.btnIns:
                inscription();
                break;

            case R.id.btnCretour:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * Méthode qui vérifie si l'utilisateur existe dans la BD, selon les informations reçu
     * par l'utilisateur.
     */
    public void connection(){
        String psw = GestionnaireUtilisateurs.md5(mdp.getText().toString());
        if(GestionnaireUtilisateurs.authentifier(nomUti.getText().toString(), psw)){

        }
    }

    /**
     * Méthode qui permet à l'utilisateur de s'inscrire.
     */
    public void inscription(){
        String txt = "User name == empty";
        int duration = Toast.LENGTH_SHORT;

        if (!champvide(nomUti)){
            Toast toast = Toast.makeText( getApplicationContext(), txt, duration);
            toast.show();
        }
        else if (!champvide(mdp)){
            txt = "Mot de passe vide Nigga!";
            Toast toast = Toast.makeText( getApplicationContext(), txt, duration);
            toast.show();
        }
    }

    /**
     * Méthode qui renvois un
     * @param et
     * @return
     */
    public boolean champvide(EditText et){
        return !et.getText().toString().isEmpty();
    }
}
