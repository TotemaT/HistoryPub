package be.ipl.mobile.projet.historypub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Created by matt on 19/11/15.
 */
public class EtapeActivity extends AppCompatActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
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

    private Etape mEtape;
    private Utils util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        util = new Utils(this);

        mWebView = (WebView) findViewById(R.id.webView);
        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);
        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));

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

    private void lanceEpreuveCorrespondante(String url) {
        Log.i("ET", mEtape.toString());
        Log.i("NB_EP", mEtape.getNombreEpreuves() + "");
        Type type = mEtape.getEpreuve(url).getType();
        Intent intent;
        if (type.equals(Type.QCM)) {
            intent = new Intent(EtapeActivity.this, QcmActivity.class);
        } else if (type.equals(Type.OUVERTE)) {
            intent = new Intent(EtapeActivity.this, QuestionOuverteActivity.class);
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
        intent.putExtra(Config.EXTRA_ETAPE_COURANTE, mEtape.getNum());
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
        if (!connectionResult.equals(ConnectionResult.SUCCESS)) {
            Toast.makeText(EtapeActivity.this, R.string.gps_erreur, Toast.LENGTH_SHORT).show();
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
        mWebView.loadUrl(mEtape.getUrl());
    }

    public String getUrl(String url) {
        return "file:///android_asset/" + getString(R.string.prefix) + File.separator + url;
    }
}
