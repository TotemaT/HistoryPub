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

package be.ipl.mobile.projet.jeudepiste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static final String PREF_DERNIERE_ETAPE_REUSSIE = "derniere_etape_reussie";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, ListeEpreuveActivity.class);
                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                intent.putExtra(Config.EXTRA_ETAPE, pref.getInt(PREF_DERNIERE_ETAPE_REUSSIE, 0));

                getPreferences(MODE_PRIVATE).edit().putInt(PREF_DERNIERE_ETAPE_REUSSIE, 3);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
