package be.ipl.mobile.projet.historypub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by matt on 10/11/15.
 */
public class EpreuveActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_epreuve);

        Intent intent = getIntent();
        int etape = intent.getIntExtra(Config.EXTRA_ETAPE, -1);
        int epreuve = intent.getIntExtra(Config.EXTRA_EPREUVE, -1);
        mWebView = (WebView) findViewById(R.id.webView);

        /* Récuperer l'étape dans GestionEtapes et y récuperer l'url*/
    }
}
