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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 *
 */
public class ListeEpreuveActivity extends AppCompatActivity {
    private static final String TAG = "ListeEpreuveActivity";
    private static final String PREF_DERNIERE_EPREUVE_REUSSIE = "derniere_epreuve_reussie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Dernière épreuve réussie : " + getIntent().getIntExtra(Config.EXTRA_ETAPE, -1));
    }
}
