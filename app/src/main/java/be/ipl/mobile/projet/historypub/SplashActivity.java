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

    private static final String PREF_DERNIERE_ETAPE_REUSSIE = "derniere_etape_reussie";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, Intent>() {

            @Override
            protected Intent doInBackground(Void... voids) {
                Intent intent = new Intent(SplashActivity.this, QuestionOuverteActivity.class);
                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                intent.putExtra(Config.EXTRA_ETAPE, pref.getInt(PREF_DERNIERE_ETAPE_REUSSIE, 0));

                GestionEtapes.getInstance(SplashActivity.this);
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
