package be.ipl.mobile.projet.historypub;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {
    private TextView mScoreFinal;
    private TextView mTempsFinal;
    private Utils util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        util = new Utils(this);

        mScoreFinal = (TextView) findViewById(R.id.score_final);
        mTempsFinal = (TextView) findViewById(R.id.temps_total);

        Button partagerBtn = (Button) findViewById(R.id.partager_btn);
        partagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.partager();
            }
        });
        remplirStatsFinales();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        util.resetPartie();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_epreuve, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        (menu.findItem(R.id.score_menu)).setTitle(getResources().getString(R.string.score, util.getPoints()));
        (menu.findItem(R.id.reinit_menu)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                util.resetPartie();
                return false;
            }
        });
        return true;
    }

    private void remplirStatsFinales() {
        mScoreFinal.setText(String.valueOf(util.getPoints()));

        Resources res = getResources();
        int[] duree = util.getDuree();
        mTempsFinal.setText(res.getString(R.string.duree_finale, duree[0], duree[1], duree[2]));
    }

}
