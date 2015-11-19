package be.ipl.mobile.projet.historypub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
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
public class EpreuveActivity extends AppCompatActivity {
    private static final String TAG = "EpreuveActivity";

    private Etape mEtape;
    private Epreuve mEpreuve;

    /* Utilisé dans le cas d'un QCM */
    private AppCompatCheckBox mCheckBoxUn;
    private AppCompatCheckBox mCheckBoxDeux;
    private AppCompatCheckBox mCheckBoxTrois;

    /* Utilisé pour toutes les questions */
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));
        mEpreuve = mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        setEpreuveView();
        setQuestion();

        mButton = (Button) findViewById(R.id.reponse_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifieReponse();
            }
        });
    }

    private void setEpreuveView() {
        Type type = mEpreuve.getType();
        if (type == Type.QCM) {
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

        }
        if (type == Type.PHOTO) {
            setContentView(R.layout.epreuve_qcm);
        }
        if (type == Type.ATROU) {
            setContentView(R.layout.epreuve_qcm);
        }
    }

    private void setQuestion() {
        Type type = mEpreuve.getType();
        if (type == Type.QCM) {
            EpreuveQCM epreuve = (EpreuveQCM) mEpreuve;
            ((TextView) findViewById(R.id.question_textView)).setText(epreuve.getQuestion());

            List<ReponseQCM> reponses = epreuve.getReponses();

            View choixUn = findViewById(R.id.choix_1);
            ((TextView) choixUn.findViewById(R.id.reponse_textView)).setText(reponses.get(0).getReponse());
            View choixDeux = findViewById(R.id.choix_2);
            ((TextView) choixDeux.findViewById(R.id.reponse_textView)).setText(reponses.get(1).getReponse());
            View choixTrois = findViewById(R.id.choix_3);
            ((TextView) choixTrois.findViewById(R.id.reponse_textView)).setText(reponses.get(2).getReponse());

        }
    }


    private void verifieReponse() {
        Type type = mEpreuve.getType();
        if (type == Type.QCM) {
            EpreuveQCM epreuve = (EpreuveQCM) mEpreuve;

            int reponseChoisie = -1;
            if (mCheckBoxUn.isChecked()) {
                reponseChoisie = 0;
            } else if (mCheckBoxDeux.isChecked()) {
                reponseChoisie = 1;
            } else if (mCheckBoxTrois.isChecked()) {
                reponseChoisie = 2;
            }

            if (reponseChoisie == -1) {
                Toast.makeText(EpreuveActivity.this, "Répondez à la question :)", Toast.LENGTH_SHORT).show();
            } else {

                String bonneReponse = "";
                for (ReponseQCM reponse : epreuve.getReponses()) {
                    if (reponse.estBonne()) {
                        bonneReponse = reponse.getReponse();
                        break;
                    }
                }

                if (epreuve.getReponses().get(reponseChoisie).estBonne()) {
                    Toast.makeText(EpreuveActivity.this, "Bonne réponse!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EpreuveActivity.this, "Mauvaise réponse... :(\nLa bonne réponse était : " + bonneReponse, Toast.LENGTH_SHORT).show();

                }

                /* Charger prochaine épreuve */
            }
        }
    }
}
