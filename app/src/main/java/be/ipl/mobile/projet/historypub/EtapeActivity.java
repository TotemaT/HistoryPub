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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.Date;

import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Activité présentant une étape du jeu à l'utilisateur.
 */
public class EtapeActivity extends BasicActivity {
    private static final String TAG = "EtapeActivity";

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        mWebView = (WebView) findViewById(R.id.webView);
        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);
        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE, 0));

        Log.d(TAG, "chargement de : " + mEtape.getUrl());

        setupWebview();
    }

    /**
     * Charge le fichier HTML dans la webview et défini la gestion des liens.
     */
    private void setupWebview() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                lanceEpreuveCorrespondante(url);
                return true;
            }
        });
        mWebView.loadUrl("file:///android_asset/" + getString(R.string.prefix) + File.separator + mEtape.getUrl());
    }

    /**
     * Lance l'épreuve correspondante au lien URL donné.
     *
     * @param url URL correspondant à l'épreuve
     */
    private void lanceEpreuveCorrespondante(String url) {
        Log.i("ET", mEtape.toString());
        Log.i("NB_EP", mEtape.getNombreEpreuves() + "");
        Type type = mEtape.getEpreuve(url).getType();
        Intent intent;
        if (type.equals(Type.QCM)) {
            intent = new Intent(EtapeActivity.this, QcmActivity.class);
        } else if (type.equals(Type.OUVERTE)) {
            intent = new Intent(EtapeActivity.this, QuestionOuverteActivity.class);
        } else if (type.equals(Type.ATROU)) {
            intent = new Intent(EtapeActivity.this, TrouActivity.class);
        } else {
            intent = new Intent(EtapeActivity.this, PhotoActivity.class);
        }
        SharedPreferences prefs = getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        /* Si pas dans les préférences, premier lancement ou premier lancement après un reset */
        if (!prefs.contains(Config.PREF_TEMPS_DEBUT)) {
            getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE)
                    .edit()
                    .putLong(Config.PREF_TEMPS_DEBUT, new Date().getTime())
                    .apply();
        }
        intent.putExtra(Config.EXTRA_ETAPE, mEtape.getNum());
        intent.putExtra(Config.EXTRA_EPREUVE, mEtape.getEpreuve(url).getUri());

        Log.d(TAG, "Url capturée : " + url);

        startActivity(intent);
        finish();
    }

}
