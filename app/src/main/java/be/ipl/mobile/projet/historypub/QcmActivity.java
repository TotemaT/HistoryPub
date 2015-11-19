package be.ipl.mobile.projet.historypub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.ReponseQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Created by matt on 10/11/15.
 */
public class QcmActivity extends AppCompatActivity {
    private static final String TAG = "QcmActivity";

    private Etape mEtape;
    private EpreuveQCM mEpreuve;

    private AppCompatCheckBox mCheckBoxUn;
    private AppCompatCheckBox mCheckBoxDeux;
    private AppCompatCheckBox mCheckBoxTrois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));
        mEpreuve = (EpreuveQCM) mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        setContentView(R.layout.epreuve_qcm);

        mCheckBoxUn = (AppCompatCheckBox) findViewById(R.id.choix_1).findViewById(R.id.qcm_checkbox);
        mCheckBoxDeux = (AppCompatCheckBox) findViewById(R.id.choix_2).findViewById(R.id.qcm_checkbox);
        mCheckBoxTrois = (AppCompatCheckBox) findViewById(R.id.choix_3).findViewById(R.id.qcm_checkbox);

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            /* Probablement moyen de faire ça plus joliment, petit hack en attendant de trouver */
                if (b) {
                    mCheckBoxUn.setChecked(false);
                    mCheckBoxDeux.setChecked(false);
                    mCheckBoxTrois.setChecked(false);
                    compoundButton.setChecked(true);
                }
            }
        };

        mCheckBoxUn.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxDeux.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxTrois.setOnCheckedChangeListener(onCheckedChangeListener);
        setQuestion();

        Button button = (Button) findViewById(R.id.reponse_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifieReponse();
            }
        });
    }

    private void setQuestion() {
        ((TextView) findViewById(R.id.question_textView)).setText(mEpreuve.getQuestion());

        List<ReponseQCM> reponses = mEpreuve.getReponses();

        View choixUn = findViewById(R.id.choix_1);
        ((TextView) choixUn.findViewById(R.id.reponse_textView)).setText(reponses.get(0).getReponse());
        View choixDeux = findViewById(R.id.choix_2);
        ((TextView) choixDeux.findViewById(R.id.reponse_textView)).setText(reponses.get(1).getReponse());
        View choixTrois = findViewById(R.id.choix_3);
        ((TextView) choixTrois.findViewById(R.id.reponse_textView)).setText(reponses.get(2).getReponse());

    }


    private void verifieReponse() {
        int reponseChoisie = -1;
        if (mCheckBoxUn.isChecked()) {
            reponseChoisie = 0;
        } else if (mCheckBoxDeux.isChecked()) {
            reponseChoisie = 1;
        } else if (mCheckBoxTrois.isChecked()) {
            reponseChoisie = 2;
        }

        if (reponseChoisie == -1) {
            Toast.makeText(QcmActivity.this, "Répondez à la question :)", Toast.LENGTH_SHORT).show();
        } else {

            String bonneReponse = "";
            for (ReponseQCM reponse : mEpreuve.getReponses()) {
                if (reponse.estBonne()) {
                    bonneReponse = reponse.getReponse();
                    break;
                }
            }

            if (mEpreuve.getReponses().get(reponseChoisie).estBonne()) {
                Toast.makeText(QcmActivity.this, "Bonne réponse!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(QcmActivity.this, "Mauvaise réponse... :(\nLa bonne réponse était : " + bonneReponse, Toast.LENGTH_SHORT).show();
            }

            chargerEpreuveOuEtapeSuivante();
        }
    }

    private void chargerEpreuveOuEtapeSuivante() {
        Epreuve epreuveSuivante = mEtape.getEpreuve(mEpreuve.getNum() + 1);
        if (epreuveSuivante != null) {
            Type typeSuivant = epreuveSuivante.getType();
            if (typeSuivant == Type.QCM) {
                Intent intent = new Intent(this, QcmActivity.class);
                intent.putExtra(Config.EXTRA_ETAPE_COURANTE, mEtape.getNum());
                Log.d(TAG, epreuveSuivante.getUri());
                intent.putExtra(Config.EXTRA_EPREUVE, epreuveSuivante.getUri());
                startActivity(intent);
                finish();
            }
            /* sinon regarder les autres types */
        } else {
            Log.d(TAG, "pas d'epreuve suivante");
            /* l'étape est finie, charger la suivante */
            Etape etapeSuivante = GestionEtapes.getInstance(this).getEtape(mEtape.getNum() + 1);
            if (etapeSuivante == null) {
                /* charger écran de fin reprenant le temps total et le score final */
            } else {
                Log.d(TAG, "charge etape num : " + etapeSuivante.getNum());
                Intent intent = new Intent(this, EtapeActivity.class);
                intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etapeSuivante.getNum());
                startActivity(intent);
                finish();
            }
        }
    }
}
