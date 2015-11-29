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

package be.ipl.mobile.projet.historypub.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.Date;

import be.ipl.mobile.projet.historypub.R;
import be.ipl.mobile.projet.historypub.config.Config;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;
import be.ipl.mobile.projet.historypub.ucc.GestionEtapes;

/**
 * Activité présentant une étape du jeu à l'utilisateur.
 */
public class EtapeActivity extends BasicActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private static final String TAG = "EtapeActivity";

    /*
    La partie geolocalisation est en grande partie reprise du tutoriel de Google se trouvant
    à cette adresse : https://developer.android.com/training/location/receive-location-updates.html
    */

    private static final long UPDATE_INTERVAL = 10000;
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;

    private GoogleApiClient mApiClient;
    private LocationRequest mLocationRequest;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        mWebView = (WebView) findViewById(R.id.webView);
        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);
        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE, 0));

        Log.d(TAG, "chargement de : " + mEtape.getUrl());

        buildGoogleApiClient();

        setupWebview();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        if (mApiClient.isConnected()) {
            mApiClient.disconnect();
        }

        super.onStop();
    }

    private void buildGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
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
        mWebView.loadUrl(getUrl("location." + mEtape.getUrl()));
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
        SharedPreferences prefs = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE);
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

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connecté à APIClient");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        if (location != null && mEtape.getZone().contient(location)) {
            Log.d(TAG, location.toString());
            stopLocationUpdates();
            lancerEtape();
        } else {
            Log.d(TAG, "Commence à mettre à jour la localisation");
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        /* Retente de se connecter */
        mApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed : " + location.toString());

        if (mEtape.getZone().contient(location)) {
            Log.d(TAG, "Dans la zone");
            lancerEtape();
        }

        Log.d(TAG, "Hors de la zone");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() != ConnectionResult.SUCCESS) {
            String message;
            switch (connectionResult.getErrorCode()) {
                case ConnectionResult.SUCCESS:
                    return;
                case ConnectionResult.SERVICE_MISSING:
                    message = getString(R.string.gps_service_missing);
                    break;
                case ConnectionResult.NETWORK_ERROR:
                    message = getString(R.string.gps_network_error);
                    break;
                default:
                    message = "Erreur de connexion aux services Google Play.";
                    break;
            }
            Toast.makeText(EtapeActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
    }

    private void lancerEtape() {
        stopLocationUpdates();
        Toast.makeText(EtapeActivity.this, R.string.location_ok, Toast.LENGTH_SHORT).show();
        mWebView.loadUrl(getUrl(mEtape.getUrl()));
    }

    public String getUrl(String url) {
        return "file:///android_asset/" + getString(R.string.prefix) + File.separator + url;
    }
}
