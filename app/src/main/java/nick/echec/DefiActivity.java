package nick.echec;

import android.os.Bundle;
import data.Defi;
import data.GestionnaireDefi;
import data.ResultatDefi;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui permet à l'utilisateur de choisir le défi qu'il veux faire.
 * Affiche les catégories de défi ainsi que les défis de chaques catégories
 * une fois sélectionné.
 * Created by Keven on 2016-05-04.
 */
public class DefiActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button retour;
    private Spinner spinD;
    private ListView lv;
    private List niveau;
    private ArrayList<Defi> listDefi;
    private ResultatDefi r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defi);
        listDefi = getListeDeDefi("Pion");
        setSpinner();
        setListView(); //a changer
        retour = (Button)findViewById(R.id.btnDretour);
        retour.setOnClickListener(this);

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

            default:
                break;
        }
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
        niveau.add("Pion");
        niveau.add("Tour");
        niveau.add("Fou");
        niveau.add("Cavalier");
        niveau.add("Reine");
        niveau.add("Roi");

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
        //Création de l'adapter
        ArrayAdapter<Defi> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDefi);

        //Récupération du ListView
        ListView list = (ListView)findViewById(R.id.lvDefi);

        //On passe nos données au composant ListView
        list.setAdapter(new DefiAdapter());

        //TODO ouvrire la partie avec la bonne map et +
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * Crée la liste de défi //TODO c'est temporaire et devrait être changer pour le chercher dans la bd
     * @param s est la catégorie
     * @return la liste de défi
     */
    public ArrayList<Defi> getListeDeDefi(String s) {
        ArrayList<Defi> listDefi = new ArrayList<Defi>();
        String pieces[] = {"TN100", "CN101", "FN102", "KN103", "QN104", "FN205", "CN206", "TN207", "PN110", "PN211", "PN312", "PN413", "PN514", "PN615", "PN716","PN817",
                "PB160", "PB261", "PB362", "PB463", "PB564", "PB665", "PB766","PB867", "TB170", "CB171", "FB172", "KB173", "QB174", "FB275", "CB276", "TB277"};
        StringBuilder b = new StringBuilder();
        for (String grille : pieces){
            b.append(grille);
            b.append(',');
        }
        GestionnaireDefi.ajouter("defi", 10, b.toString());
        listDefi.add(GestionnaireDefi.get("defi"));
        return listDefi;
    }

    /**
     * Méthode qui réagi lorsqu'une catégorie de défi est sélectionner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        listDefi = getListeDeDefi(spinD.getSelectedItem().toString());
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
     * Permet d'adapter l'arrayliste pour quelle contienne des journées
     */
    class DefiAdapter extends ArrayAdapter<Defi> {
        DefiAdapter() {
            super(DefiActivity.this, R.layout.row, R.id.lblnom, listDefi);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            DefiWrapper wrapper;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row, null);
                wrapper = new DefiWrapper(convertView);
                convertView.setTag(wrapper);
            } else
                wrapper = (DefiWrapper) convertView.getTag();

            wrapper.setDefi(getItem(position),r);

            return convertView;
        }
    }

    /**
     * Permet d'afficher le contenu de chaque journée dans une row
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
            if (nom == null)  nom = (TextView) row.findViewById(R.id.lblnom);
            return nom;
        }

        /**
         * Getteur du CheckBox qui dit si le défi est réussi
         * @return le CheckBox en question
         */
        public CheckBox getReussi() {
            if (reussi == null)  reussi = (CheckBox) row.findViewById(R.id.cb_reussi);
            return reussi;
        }

        /**
         * Getteur du RatingBar qui dit la difficulté du défi
         * @return le RatingBar en question
         */
        public RatingBar getDiff(){
            if (diff == null) diff = (RatingBar) row.findViewById(R.id.rb_dif);
            return diff;
        }

        /**
         * Méthode qui sette les donnée dans les éléments du layout row.xml
         * @param d le Defi duquel vien les données
         */
        public void setDefi(Defi d, ResultatDefi r) {
            getNom().setText(d.getNom());
            getReussi().setChecked(r.isReussi());
            getDiff().setRating(d.getDifficulte());
        }
    }
}
