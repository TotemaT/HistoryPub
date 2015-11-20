package be.ipl.mobile.projet.historypub;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {
    private TextView score_final;
    private TextView temps_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        score_final = (TextView) findViewById(R.id.score_final);
        temps_final = (TextView) findViewById(R.id.temps_total);
        remplir_stats_finaux();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_epreuve, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        (menu.findItem(R.id.score_menu)).setTitle("Score: " + new Utils(this).getPoints());
        (menu.findItem(R.id.reinit_menu)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Utils util = new Utils(FinalActivity.this);
                GestionEtapes g = GestionEtapes.getInstance(FinalActivity.this);
                SharedPreferences.Editor edit = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE).edit();
                edit.putInt(Config.PREF_ETAPE_COURANTE, 0);
                edit.putString(Config.PREF_EPREUVE_COURANTE, null);
                edit.putInt(Config.PREF_POINTS_TOTAUX, 0);
                edit.apply();
                util.chargerEpreuveOuEtapeSuivante(null, null);
                return false;
            }
        });
        return true;
    }

    public void remplir_stats_finaux(){
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCES,MODE_PRIVATE);
        score_final.setText("Score: "+String.valueOf(sp.getInt(Config.PREF_POINTS_TOTAUX, 0)));
        temps_final.setText("Temps:" +sp.getString(Config.PREF_TEMPS_DEBUT, null));
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(Config.PREF_POINTS_TOTAUX, 0);
        edit.putString(Config.PREF_TEMPS_DEBUT, null);
        edit.apply();
    }
}
