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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.ReponseQCM;

/**
 * Activité reprenant une épreuve de question à choix multiples.
 */
public class QcmActivity extends AppCompatActivity {
    private static final String TAG = "QcmActivity";

    private Etape mEtape;
    private EpreuveQCM mEpreuve;

    private AppCompatCheckBox mCheckBoxUn;
    private AppCompatCheckBox mCheckBoxDeux;
    private AppCompatCheckBox mCheckBoxTrois;

    private Utils util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epreuve_qcm);

        util = new Utils(this);

        GestionEtapes gestionEtapes = GestionEtapes.getInstance(this);

        mEtape = gestionEtapes.getEtape(getIntent().getIntExtra(Config.EXTRA_ETAPE, 0));
        mEpreuve = (EpreuveQCM) mEtape.getEpreuve(getIntent().getStringExtra(Config.EXTRA_EPREUVE));

        View choixUn = findViewById(R.id.choix_1);
        View choixDeux = findViewById(R.id.choix_2);
        View choixTrois = findViewById(R.id.choix_3);

        mCheckBoxUn = (AppCompatCheckBox) choixUn.findViewById(R.id.qcm_checkbox);
        mCheckBoxDeux = (AppCompatCheckBox) choixDeux.findViewById(R.id.qcm_checkbox);
        mCheckBoxTrois = (AppCompatCheckBox) choixTrois.findViewById(R.id.qcm_checkbox);
        mCheckBoxUn.setClickable(false);
        mCheckBoxDeux.setClickable(false);
        mCheckBoxTrois.setClickable(false);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Uncheck toutes les checkbox puis check la réponse sélectionnée */
                mCheckBoxUn.setChecked(false);
                mCheckBoxDeux.setChecked(false);
                mCheckBoxTrois.setChecked(false);

                ((AppCompatCheckBox) view.findViewById(R.id.qcm_checkbox)).setChecked(true);
            }
        };

        choixUn.setOnClickListener(clickListener);
        choixDeux.setOnClickListener(clickListener);
        choixTrois.setOnClickListener(clickListener);

        setQuestion();

        Button button = (Button) findViewById(R.id.reponse_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifieReponse();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        (menu.findItem(R.id.score_menu)).setTitle(getResources().getString(R.string.score, util.getPoints()));
        (menu.findItem(R.id.reinit_menu)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                util.resetPartie();
                return false;
            }
        });
        (menu.findItem(R.id.avancement)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                util.showAvancement(mEtape, mEpreuve);
                return false;
            }
        });
        return true;
    }

    /**
     * Affiche la question et les différents choix de réponses à cette dernière.
     */
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


    /**
     * Vérifie la réponse donnée ou demande de répondre.
     * Préviens l'utilisateur si la réponse est correcte ou non et lance l'épreuve ou étape suivante
     */
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

            Utils utils = new Utils(this);

            if (mEpreuve.getReponses().get(reponseChoisie).estBonne()) {
                Toast.makeText(QcmActivity.this, "Bonne réponse! +" + mEpreuve.getPoints() + " points.", Toast.LENGTH_LONG).show();
                utils.augmenterPoints(mEpreuve.getPoints());
            } else {
                Toast.makeText(QcmActivity.this, "Mauvaise réponse... :(\nLa bonne réponse était : " + bonneReponse, Toast.LENGTH_SHORT).show();
            }

            int[] duree = util.getDuree();
            Resources res = getResources();

            String heures = res.getQuantityString(R.plurals.heures, duree[0], duree[0]);
            String minutes = res.getQuantityString(R.plurals.minutes, duree[1], duree[1]);
            String secondes = res.getQuantityString(R.plurals.secondes, duree[2], duree[2]);

            utils.getDialogExplicatif(mEtape, mEpreuve, res.getString(R.string.duree_finale, heures, minutes, secondes));
        }
    }
}
