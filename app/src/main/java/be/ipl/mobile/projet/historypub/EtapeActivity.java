package be.ipl.mobile.projet.historypub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private void setupWebview() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                lanceEpreuveCorrespondante(url);
                return true;
            }
        });
        mWebView.loadUrl("file:///android_asset/"+mEtape.getUrl());
    }

    private void lanceEpreuveCorrespondante(String url) {
        Type type = mEtape.getEpreuve(url).getType();
        Intent intent;
        /* QCM par défaut, si pas ce type là, on passe dans les if et bla bla bla */
        intent = new Intent(EtapeActivity.this, QcmActivity.class);

        intent.putExtra(Config.EXTRA_EPREUVE, url);
        intent.putExtra(Config.EXTRA_ETAPE_COURANTE, mEtape.getNum());

        Log.d(TAG, "Url capturée : " + url);

        startActivity(intent);
        finish();
    }


}
