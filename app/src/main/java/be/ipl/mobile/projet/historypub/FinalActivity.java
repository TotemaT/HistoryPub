/*
    History Pub est une application de jeu de piste proposant de découvrir la ville de Soignies,
    en parcourant cette dernière de bar en bar.

    Copyright (C) 2015
        Matteo Taroli <contact@matteotaroli.be>
        Nathan Raspe <raspe_nathan@live.be>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package be.ipl.mobile.projet.historypub;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activité présentant un résumé de la partie à l'utilisateur.
 */
public class FinalActivity extends AppCompatActivity {
    private TextView mScoreFinalTv;
    private TextView mTempsFinalTv;
    private Utils util;

    private String mScore;
    private String mDuree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        util = new Utils(this);

        mScoreFinalTv = (TextView) findViewById(R.id.score_final);
        mTempsFinalTv = (TextView) findViewById(R.id.temps_total);

        Button partagerBtn = (Button) findViewById(R.id.partager_btn);
        partagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.partager(mDuree, mScore);
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

    /**
     * Affiche le score final et la durée totale du jeu.
     */
    private void remplirStatsFinales() {
        int[] duree = util.getDuree();
        Resources res = getResources();

        String heures = res.getQuantityString(R.plurals.heures, duree[0], duree[0]);
        String minutes = res.getQuantityString(R.plurals.minutes, duree[1], duree[1]);
        String secondes = res.getQuantityString(R.plurals.secondes, duree[2], duree[2]);

        mScore = res.getQuantityString(R.plurals.points, util.getPoints(), util.getPoints());
        mDuree = res.getString(R.string.duree_finale, heures, minutes, secondes);

        mScoreFinalTv.setText(mScore);
        mTempsFinalTv.setText(mDuree);
    }

}
