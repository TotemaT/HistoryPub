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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
* Activité de SplashScreen affichant le logo et le nom de l'application
* pendant le chargement du fichier XML
*/
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIMEOUT = 2000;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE);

                /* Numerotation des etapes et épreuves commence à 0 */
                int etape = pref.getInt(Config.PREF_ETAPE_COURANTE, 0);
                String epreuve = pref.getString(Config.PREF_EPREUVE_COURANTE, null);

                /* Charge le fichier XML */
                GestionEtapes gestionEtapes = GestionEtapes.getInstance(SplashActivity.this);

                if (epreuve == null) {
                    intent = new Intent(SplashActivity.this, EtapeActivity.class);
                    intent.putExtra(Config.EXTRA_ETAPE, etape);
                } else {
                    Epreuve ep = gestionEtapes.getEtape(etape).getEpreuve(epreuve);
                    if (ep.getType() == Type.QCM) {
                        intent = new Intent(SplashActivity.this, QcmActivity.class);
                    } else if (ep.getType() == Type.OUVERTE) {
                        intent = new Intent(SplashActivity.this, QuestionOuverteActivity.class);
                    } else if (ep.getType() == Type.ATROU) {
                        Log.i("HELLO","PASSED");
                        intent = new Intent(SplashActivity.this, TrouActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, PhotoActivity.class);
                    }

                    intent.putExtra(Config.EXTRA_ETAPE, etape);
                    intent.putExtra(Config.EXTRA_EPREUVE, epreuve);
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}
