package be.ipl.mobile.projet.historypub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveOuverte;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Reponse;

public class QuestionOuverteActivity extends AppCompatActivity {
    private Etape mEtape;
    private EpreuveOuverte mEpreuve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));
        mEpreuve = (EpreuveOuverte) mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        setContentView(R.layout.activity_question_ouverte);
        Button repondre = (Button) findViewById(R.id.reponse_btn);
        final EditText edit = (EditText) findViewById(R.id.question_ouverte_edit);
        TextView question = (TextView) findViewById(R.id.question_ouverte);
        question.setText(mEpreuve.getQuestion());
        repondre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText() == null) {
                    Toast.makeText(QuestionOuverteActivity.this, "Répondez à la question :)", Toast.LENGTH_SHORT).show();
                } else if (mEpreuve.estReponseCorrecte(new Reponse(edit.getText().toString()))) {
                    Toast.makeText(QuestionOuverteActivity.this, "Bonne réponse!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuestionOuverteActivity.this, "Mauvaise réponse! La bonne réponse était " + mEpreuve.getReponse().getReponse(), Toast.LENGTH_LONG).show();
                }
                Utils.chargerEpreuveOuEtapeSuivante(QuestionOuverteActivity.this, mEtape, mEpreuve);
                finish();
            }
        });
    }
}
