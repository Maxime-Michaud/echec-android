package nick.echec;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.GestionnaireUtilisateurs;

/**
 * Classe qui permet à l'utilisateur de se créer un compte ou de se connecter à un compte existant,
 * en utilisant les informations dans la bd.
 * Created by Keven on 2016-05-11.
 */
public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button      connexion;
    private Button      inscrire;
    private Button      retour;
    private EditText    etNom;
    private EditText    etMDP;
    private String      erreur;
    SharedPreferences   pref;
    private String      prefNom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        pref = ConnexionActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        prefNom = pref.getString(getString(R.string.UTILISATEUR),"");

        connexion = (Button) findViewById(R.id.btn_log);
        inscrire = (Button) findViewById(R.id.btn_ins);
        retour = (Button) findViewById(R.id.btn_cRetour);
        etNom = (EditText)findViewById(R.id.et_user);
        etMDP = (EditText) findViewById(R.id.et_mdp);

        connexion.setOnClickListener(this);
        inscrire.setOnClickListener(this);
        retour.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_log:
                authentification();
                break;

            case R.id.btn_ins:
                inscription();
                break;

            case R.id.btn_cRetour:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     *  Méthode qui fait l'authentification de l'utilisateur et envoie un Toast si il y a une erreur
     *  ou un Toast pour confirmer si tout c'est bien passer.
     */
    public void authentification(){
        Toast t;
        Context c = getApplicationContext();
        erreur = "Erreur!";
        String nom = etNom.getText().toString();
        String motDePasse = etMDP.getText().toString();
        if (!champVide()){
            if (!champInexistant()) {
                //todo adder utilisateur au preference
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(getString(R.string.UTILISATEUR), nom);
                editor.commit();

                //Todo: L'enlever le toast XD
                Toast k = Toast.makeText(getApplicationContext(), pref.getString(getString(R.string.UTILISATEUR),null),Toast.LENGTH_LONG);
                k.show();
                erreur = getString(R.string.c_connexion_ok).toString();
            }
        }
        t = Toast.makeText(c,erreur,Toast.LENGTH_SHORT);
        t.show();
    }

    /**
     *  Méthode qui fait l'inscription de l'utilisateur et envoie un Toast si il y a une erreur
     *  ou un Toast pour confirmer si tout c'est bien passer.
     */
    public void inscription(){
        Toast t;
        Context c = getApplicationContext();
        erreur = "Erreur!";
        String nom = etNom.getText().toString();
        String motDePasse = etMDP.getText().toString();
        if (!champVide()){
            if (!nomUtilisateurExistant()) {
                GestionnaireUtilisateurs.ajouter(nom,motDePasse);
                erreur = getString(R.string.c_inscription_ok).toString();
            }
        }
        t = Toast.makeText(c,erreur,Toast.LENGTH_SHORT);
        t.show();
    }

    /**
     * Méthode qui vérifie si les champs sont bien remplis.
     * @return un boolean qui sera "true" si un champ est vide ou false si tout est rempli.
     */
    public boolean champVide(){
        boolean b = false;
        if (champsUtilisateurVide() && champsMdpVide()){
            erreur = getString(R.string.c_mDeux).toString();
            b = true;
        }
        else if (champsUtilisateurVide()){
            erreur = getString(R.string.c_mUser).toString();
            b = true;
        }
        else if (champsMdpVide()){
            erreur = getString(R.string.c_mMdp).toString();
            b = true;
        }
        return b;
    }

    /**
     * Méthode qui vérifie si information saisi son dans la bd et valide.
     * @return un boolean qui sera "true" si les champs ne figurent dans la bd ou false si les info
     * sont bonnes.
     */
    public boolean champInexistant(){
        boolean b = false;
        String n = etNom.getText().toString();
        String p = etMDP.getText().toString();
        if(!GestionnaireUtilisateurs.authentifier(n,p)){
            erreur = getString(R.string.c_erreur_log).toString();
            b = true;
        }
        return b;
    }

    /**
     * Méthode qui vérifie si le nom d'utilisateur est déjà utiliser dans la bd.
     * @return un boolean qui sera "true" si les champs ne figurent dans la bd ou false si les info
     * sont bonnes.
     */
    public boolean nomUtilisateurExistant(){
        boolean b = false;
        String n = etNom.getText().toString();
        if(GestionnaireUtilisateurs.get(n) != null){
            erreur = getString(R.string.c_user_existant).toString();
            b = true;
        }
        return b;
    }

    /**
     * Méthode qui vérifie que le champ "Nom d'utilisateur" est vide
     * @return un boolean qui sera "true" si le champ est vide ou "false" si il est remplis
     */
    public boolean champsUtilisateurVide(){
        boolean b = false;
        String s = etNom.getText().toString();
        if (s.equals("")){
            b = true;
        }
        return b;
    }

    /**
     * Méthode qui vérifie que le champ "Mot de passe" est vide
     * @return un boolean qui sera "true" si le champ est vide ou "false" si il est remplis
     */
    public boolean champsMdpVide(){
        boolean b = false;
        String s = etMDP.getText().toString();
        if (s.equals("")){
            b = true;
        }
        return b;
    }
}
