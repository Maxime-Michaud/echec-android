package nick.echec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Keven on 2016-05-11.
 */
public class SuggestionActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView            lv;
    private Button              rejouer;
    private Button              menu;
    private String              nomDefi;
    private ArrayList<String>   suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        lv = (ListView) findViewById(R.id.lv_suggestion);
        rejouer = (Button) findViewById(R.id.btns_rejouer);
        menu = (Button) findViewById(R.id.btns_menu);

        rejouer.setOnClickListener(this);
        menu.setOnClickListener(this);

        Intent monIntent = getIntent();
        nomDefi = monIntent.getStringExtra("defi");
        suggestions = monIntent.getStringArrayListExtra("suggestion");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                suggestions );

        lv.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btns_rejouer:
                rejouer();
                break;

            case R.id.btns_menu:
                menu();
                break;

            default:
                break;
        }
    }

    public void rejouer(){
        Intent secondeActivite = new Intent(SuggestionActivity.this, MainActivity.class);
        secondeActivite.putExtra("defi",nomDefi);
        startActivity(secondeActivite);
    }

    public void menu(){
        Intent secondeActivite = new Intent(SuggestionActivity.this, MenuActivity.class);
        startActivity(secondeActivite);
    }
}
