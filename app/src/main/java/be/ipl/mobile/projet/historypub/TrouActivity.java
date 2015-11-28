package be.ipl.mobile.projet.historypub;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveATrou;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveOuverte;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Reponse;

public class TrouActivity extends BasicActivity {

    private EpreuveATrou mEpreuveATrou;
    private int pointsAEnlever=0;
    private EditText mReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trou);

        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE, 0));
        mEpreuve = mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));
        mEpreuveATrou = (EpreuveATrou) mEpreuve;

        Button repondre = (Button) findViewById(R.id.reponse_btn);
        final Button cheat = (Button) findViewById(R.id.cheat_btn);
        final Button help = (Button) findViewById(R.id.help_btn);
        mReponse = (EditText) findViewById(R.id.question_trou_edit);

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

        TextView question = (TextView) findViewById(R.id.question_trou_textView);
        question.setText(mEpreuve.getQuestion());
        repondre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifierReponse();
            }
        });
        cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheat.setEnabled(false);
                help.setEnabled(false);
                mReponse.setText("");
                for(String rep: mEpreuveATrou.getMots()){
                    mReponse.setText(mReponse.getText().toString()+rep+",");
                }
                mReponse.setText(mReponse.getText().toString().substring(0,mReponse.getText().toString().length()-1));
                mReponse.setEnabled(false);
                pointsAEnlever=mEpreuveATrou.getPoints();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help.setEnabled(false);
                for(String rep: mEpreuveATrou.getMots()){
                    mReponse.setText(mReponse.getText().toString()+rep.substring(0,1)+",");
                }
                mReponse.setText(mReponse.getText().toString().substring(0,mReponse.getText().toString().length()-1));
                pointsAEnlever=(mEpreuve.getPoints()/2);
                //TODO empêcher l'utilisateur de delete la première lettre ainsi révélée + ajouter premiere lettre aux mots
            }
        });
    }

    /**
     * Vérifie la réponse donnée ou demande de répondre.
     * Préviens l'utilisateur si la réponse est correcte ou non et lance l'épreuve ou étape suivante
     */
    private void verifierReponse() {
        if (mReponse.getText().toString().isEmpty() || mReponse.getText().toString().equals("")) {
            Toast.makeText(TrouActivity.this, "Répondez à la question :)", Toast.LENGTH_SHORT).show();
        } else {
            String title;
            List<String> mots = Arrays.asList(mReponse.getText().toString().split(",[ ]*"));
            if(mEpreuveATrou.estRéponseCorrecte(mots)) {
                augmenterPoints((mEpreuve.getPoints()-pointsAEnlever));
                title = "Bonnes réponses! +" + (mEpreuve.getPoints()-pointsAEnlever) + " points.";
            } else {
                title = "Mauvaise réponse! Les bonnes réponses était ";
                for (String mot: mEpreuveATrou.getMots())
                    title+=mot+",";
                title = title.substring(0,title.length()-1);
            }
            int[] duree = getDuree();
            Resources res = getResources();

            String heures = res.getQuantityString(R.plurals.heures, duree[0], duree[0]);
            String minutes = res.getQuantityString(R.plurals.minutes, duree[1], duree[1]);
            String secondes = res.getQuantityString(R.plurals.secondes, duree[2], duree[2]);

            getDialogExplicatif(title,mEtape, mEpreuve, res.getString(R.string.duree_finale, heures, minutes, secondes));
        }
    }
}
