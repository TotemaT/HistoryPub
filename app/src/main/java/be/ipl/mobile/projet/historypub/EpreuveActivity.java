package be.ipl.mobile.projet.historypub;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE_COURANTE, 0));
        mEpreuve = mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        setContentView(getContentViewId());
        setQuestion();
    }

    private int getContentViewId() {
        Type type = mEpreuve.getType();
        int content = R.layout.epreuve_qcm;

        if (type == Type.PHOTO) {
            content = R.layout.epreuve_qcm;
        } else if (type == Type.ATROU) {
            content = R.layout.epreuve_qcm;
        }

        return content;
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
}
