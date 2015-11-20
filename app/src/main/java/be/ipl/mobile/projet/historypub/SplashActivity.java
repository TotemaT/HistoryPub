/*
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, Intent>() {

            @Override
            protected Intent doInBackground(Void... voids) {
                SharedPreferences pref = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE);
                /* Numerotation des etapes et épreuves commence à 0 */
                int etape = pref.getInt(Config.PREF_ETAPE_COURANTE, 0);
                String epreuve = pref.getString(Config.PREF_EPREUVE_COURANTE, null);
                int pointTotaux = pref.getInt(Config.PREF_POINTS_TOTAUX,0);
                
                /* Charge le fichier XML */
                GestionEtapes.getInstance(SplashActivity.this);
                Utils.getInstance(SplashActivity.this).augmenterPoints(0);

                Intent intent;
                if (epreuve == null) {
                    intent = new Intent(SplashActivity.this, EtapeActivity.class);
                    intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etape);
                } else {
                    //TODO vérifier le type
                    intent = new Intent(SplashActivity.this, QcmActivity.class);
                    intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etape);
                    intent.putExtra(Config.EXTRA_EPREUVE, epreuve);
                }

                return intent;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                startActivity(intent);
                finish();
            }
        }.execute();
    }
}
