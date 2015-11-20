package be.ipl.mobile.projet.historypub;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveOuverte;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Reponse;

public class QuestionOuverteActivity extends AppCompatActivity {
    private Etape mEtape;
    private EpreuveOuverte mEpreuve;

    private EditText mReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));
        mEpreuve = (EpreuveOuverte) mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        setContentView(R.layout.activity_question_ouverte);
        Button repondre = (Button) findViewById(R.id.reponse_btn);
        mReponse = (EditText) findViewById(R.id.question_ouverte_edit);

        /* Permet de répondre à la question en cliquant sur entré */
        mReponse.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            verifierReponse();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        TextView question = (TextView) findViewById(R.id.question_textView);
        question.setText(mEpreuve.getQuestion());
        repondre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifierReponse();
            }
        });
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
                Utils util = new Utils(QuestionOuverteActivity.this);
                GestionEtapes g = GestionEtapes.getInstance(QuestionOuverteActivity.this);
                SharedPreferences.Editor edit = getSharedPreferences(Config.PREFERENCES, MODE_PRIVATE).edit();
                edit.putInt(Config.PREF_ETAPE_COURANTE, 0);
                edit.putString(Config.PREF_EPREUVE_COURANTE, null);
                edit.putInt(Config.PREF_POINTS_TOTAUX, 0);
                edit.apply();
                util.chargerEpreuveOuEtapeSuivante(null, null);
                return false;
            }
        });
        return true;
    }


    private void verifierReponse() {
        if (mReponse.getText().toString().isEmpty() || mReponse.getText().toString().equals("")) {
            Toast.makeText(QuestionOuverteActivity.this, "Répondez à la question :)", Toast.LENGTH_SHORT).show();
        } else {
            Utils utils = new Utils(this);
            if (mEpreuve.estReponseCorrecte(new Reponse(mReponse.getText().toString()))) {
                utils.augmenterPoints(mEpreuve.getPoints());
                Toast.makeText(QuestionOuverteActivity.this, "Bonne réponse! +"+mEpreuve.getPoints()+" points.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(QuestionOuverteActivity.this, "Mauvaise réponse! La bonne réponse était " + mEpreuve.getReponse().getReponse(), Toast.LENGTH_LONG).show();
            }
            utils.chargerEpreuveOuEtapeSuivante(mEtape, mEpreuve);
            finish();
        }
    }
}
