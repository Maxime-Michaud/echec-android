package nick.echec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import data.Defi;
import data.GestionnaireDefi;
import data.GestionnaireUtilisateurs;
import data.ResultatDefi;
import data.Utilisateur;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe qui permet à l'utilisateur de choisir le défi qu'il veux faire.
 * Affiche les catégories de défi ainsi que les défis de chaques catégories
 * une fois sélectionné.
 * Created by Keven on 2016-05-04.
 */
public class DefiActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button          retour;
    private Button          jouer;
    private Spinner         spinD;
    private ListView        lv;
    private List            niveau;
    private List<Defi>      listDefi;
    private String          nomDefi;
    private int             ndif;     //niveau de difficulté des défis
    private Utilisateur     utilisateur;
    private ResultatDefi    r;
    SharedPreferences       pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defi);

        pref = this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        utilisateur = GestionnaireUtilisateurs.get(pref.getString(getString(R.string.UTILISATEUR),"BobLeHobo"));
        ndif = pref.getInt(getString(R.string.NIVEAU_DEFI),1);

        listDefi = GestionnaireDefi.get(ndif);
        setSpinner();
        setListView();

        retour = (Button)findViewById(R.id.btnDretour);
        jouer = (Button) findViewById(R.id.btnd_jouer);

        retour.setOnClickListener(this);
        jouer.setOnClickListener(this);

        spinD.setOnItemSelectedListener(this);

    }

    /**
     * Gère les click sur les différents boutons
     * @param v est le bouton cliqué
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDretour:
                finish();
                break;

            case R.id.btnd_jouer:
                jouer();
                break;

            default:
                break;
        }
    }

    /**
     * Méthode qui démarre une partie selon défi selectionner;
     */
    public void jouer(){
        Intent secondeActivite = new Intent(DefiActivity.this, MainActivity.class);
        secondeActivite.putExtra("defi",nomDefi);
        startActivity(secondeActivite);
    }

    /**
     * Set le spinner
     */
    public void setSpinner(){
        //Récupération du Spinner déclaré dans le fichier activity_defi.xml
        spinD = (Spinner) findViewById(R.id.spinType);

        /*
         * Création d'une liste d'élément à mettre dans le Spinner
         */
        niveau = new ArrayList();
        niveau.add("1");
        niveau.add("2");
        niveau.add("3");
        niveau.add("4");
        niveau.add("5");

        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                niveau
        );

        //On definit une présentation du spinner quand il est déroulé
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinD.setAdapter(adapter);
    }

    /**
     * set la ListView
     */
    public void setListView(){

        //ArrayAdapter<Defi> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDefi);
        ArrayAdapter<Defi> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDefi);

        //Récupération du Spinner déclaré dans le fichier activity_defi.xml
        lv = (ListView) findViewById(R.id.lvDefi);

        //On definit une présentation du spinner quand il est déroulé
        lv.setAdapter(new DefiAdapter());

        /**
         * Méhode qui selectionne un défi dans la liste des défis
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Defi d = listDefi.get(position);
                nomDefi = d.getNom().toString();
            }
        });
    }

    /**
     * Méthode qui réagi lorsqu'une catégorie de défi est sélectionner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        listDefi = GestionnaireDefi.get(Integer.parseInt(spinD.getSelectedItem().toString()));
        setListView();
    }

    /**
     * méthode qui fait rien, mais doit être la pareil
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Rien
    }

    /**
     * Permet d'adapter l'arrayliste pour quelle contienne des défis
     */
    class DefiAdapter extends ArrayAdapter<Defi> {
        DefiAdapter() {
            super(DefiActivity.this, R.layout.row_defi, R.id.lblnomDefi, listDefi);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            DefiWrapper wrapper;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_defi, null);
                wrapper = new DefiWrapper(convertView);
                convertView.setTag(wrapper);
            } else {
                wrapper = (DefiWrapper) convertView.getTag();
            }
            wrapper.setDefi(listDefi.get(position), position);
            return convertView;
        }
    }

    /**
     * Permet d'afficher le contenu de chaque défi dans une row
     */
    class DefiWrapper {
        private TextView nom = null;
        private CheckBox reussi = null;
        private RatingBar diff = null;
        private View row = null;

        DefiWrapper(View row) {
            this.row = row;
        }

        /**
         * Getteur du TextView pour le nom du défi
         * @return le TextView en question
         */
        public TextView getNom() {
            //if (nom == null)  nom = (TextView) row.findViewById(R.id.lblnom);
            if (nom == null)  nom = (TextView) row.findViewById(R.id.lblnomDefi);
            return nom;
        }

        /**
         * Getteur du CheckBox qui dit si le défi est réussi
         * @return le CheckBox en question
         */
        public CheckBox getReussi() {
            //if (reussi == null)  reussi = (CheckBox) row.findViewById(R.id.cb_reussi);
            if (reussi == null)  reussi = (CheckBox) row.findViewById(R.id.cb_defi_reussi);
            return reussi;
        }

        /**
         * Méthode qui sette les donnée dans les éléments du layout row_defi.xml
         * @param d le Defi duquel vien les données
         */
        public void setDefi(Defi d, int p) {
            getNom().setText(d.getNom());
            getReussi().setChecked(trouverResultat(p));
        }

        /**
         * Méthode qui retourne un boolean true si le defi i est réussie
         * @param i la position du défi dans la List<ResultatDefi>
         * @return un boolean  true si le defi est réussie, false si il est échoué.
         */
        public boolean trouverResultat(int i){
            List<ResultatDefi> resultatDefi = utilisateur.getResultats(listDefi);
            return resultatDefi.get(i).isReussi();
        }
    }
}
