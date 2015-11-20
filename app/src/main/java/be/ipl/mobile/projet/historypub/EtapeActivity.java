package be.ipl.mobile.projet.historypub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Created by matt on 19/11/15.
 */
public class EtapeActivity extends AppCompatActivity {
    private static final String TAG = "EtapeActivity";

    private WebView mWebView;

    private Etape mEtape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        mWebView = (WebView) findViewById(R.id.webView);
        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);
        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));

        Log.d(TAG, "chargement de : " + mEtape.getUrl());

        setupWebview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_epreuve, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        (menu.findItem(R.id.score_menu)).setTitle("Score: " + new Utils(this).getPoints());
        (menu.findItem(R.id.reinit_menu)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Utils util = new Utils(EtapeActivity.this);
                GestionEtapes g = GestionEtapes.getInstance(EtapeActivity.this);
                SharedPreferences.Editor edit = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE).edit();
                edit.putInt(Config.PREF_ETAPE_COURANTE, 0);
                edit.putString(Config.PREF_EPREUVE_COURANTE, null);
                edit.putInt(Config.PREF_POINTS_TOTAUX,0);
                edit.apply();
                util.chargerEpreuveOuEtapeSuivante(null, null);
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
        mWebView.loadUrl("file:///android_asset/" + mEtape.getUrl());
    }

    private void lanceEpreuveCorrespondante(String url) {
        Log.i("ET", mEtape.toString());
        Log.i("NB_EP", mEtape.getNombreEpreuves() + "");
        Type type = mEtape.getEpreuve(url).getType();
        Intent intent;
        /* QCM par défaut, si pas ce type là, on passe dans les if et bla bla bla */
        if (type.equals(Type.QCM))
            intent = new Intent(EtapeActivity.this, QcmActivity.class);
        else if (type.equals(Type.OUVERTE))
            intent = new Intent(EtapeActivity.this, QuestionOuverteActivity.class);
        else
            intent = new Intent(EtapeActivity.this, QuestionOuverteActivity.class);//TODO Temporaire, à remplacer par autre chose
        intent.putExtra(Config.EXTRA_EPREUVE, url);
        intent.putExtra(Config.EXTRA_ETAPE_COURANTE, mEtape.getNum());

        Log.d(TAG, "Url capturée : " + url);

        startActivity(intent);
        finish();
    }


}
