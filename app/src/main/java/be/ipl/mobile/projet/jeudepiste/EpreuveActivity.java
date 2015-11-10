package be.ipl.mobile.projet.jeudepiste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

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

        /* Charger XML et récupérer le bon fichier HTML à afficher */
    }
}
