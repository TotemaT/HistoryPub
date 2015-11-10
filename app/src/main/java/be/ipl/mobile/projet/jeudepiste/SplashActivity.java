package be.ipl.mobile.projet.jeudepiste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static final String PREF_DERNIERE_ETAPE = "derniere_etape";
    private static final String PREF_DERNIERE_EPREUVE = "derniere_epreuve";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, ListeEpreuveActivity.class);
                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                intent.putExtra(ListeEpreuveActivity.EXTRA_EPREUVE, pref.getInt(PREF_DERNIERE_EPREUVE, 0));
                intent.putExtra(ListeEpreuveActivity.EXTRA_ETAPE, pref.getInt(PREF_DERNIERE_ETAPE, 0));
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
