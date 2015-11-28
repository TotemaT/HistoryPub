/*
    History Pub est une application de jeu de piste proposant de découvrir la ville de Soignies,
    en parcourant cette dernière de bar en bar.

    Copyright (C) 2015
        Matteo Taroli <contact@matteotaroli.be>
        Nathan Raspe <raspe_nathan@live.be>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package be.ipl.mobile.projet.historypub;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveOuverte;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Reponse;

/**
 * Activité reprenant une épreuve de question ouverte.
 */
public class QuestionOuverteActivity extends EpreuveActivity {

    private EpreuveOuverte mEpreuveOuverte;
    private EditText mReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_ouverte);

        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE, 0));
        mEpreuve = mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));
        mEpreuveOuverte = (EpreuveOuverte) mEpreuve;

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
        initCheatButton();
        initgetHelpButton();
    }

    /**
     * Vérifie la réponse donnée ou demande de répondre.
     * Préviens l'utilisateur si la réponse est correcte ou non et lance l'épreuve ou étape suivante
     */
    private void verifierReponse() {
        String title;
        if (mReponse.getText().toString().isEmpty() || mReponse.getText().toString().equals("")) {
            Toast.makeText(QuestionOuverteActivity.this, "Répondez à la question :)", Toast.LENGTH_SHORT).show();
        } else {
            if (mEpreuveOuverte.estReponseCorrecte(new Reponse(mReponse.getText().toString()))) {
                augmenterPoints(mEpreuve.getPoints()-pointsAEnlever);
                title = "Bonne réponse! +" + (mEpreuve.getPoints()-pointsAEnlever) + " points.";
                pointsAEnlever=0;
            } else {
               title = "Mauvaise réponse! La bonne réponse était " + mEpreuveOuverte.getReponse().getReponse();
            }
            int[] duree = getDuree();
            Resources res = getResources();

            String heures = res.getQuantityString(R.plurals.heures, duree[0], duree[0]);
            String minutes = res.getQuantityString(R.plurals.minutes, duree[1], duree[1]);
            String secondes = res.getQuantityString(R.plurals.secondes, duree[2], duree[2]);

            getDialogExplicatif(title,mEtape, mEpreuve, res.getString(R.string.duree_finale, heures, minutes, secondes));
        }
    }

    @Override
    public void doHelp() {
        mReponse.setText(mEpreuveOuverte.getReponse().getReponse().substring(0, 1));
    }

    @Override
    public void doCheat() {
        mReponse.setText(mEpreuveOuverte.getReponse().getReponse());
        mReponse.setEnabled(false);
    }
}
