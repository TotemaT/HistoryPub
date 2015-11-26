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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import java.util.Date;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Classe reprenant différentes méthodes utilisées dans les différentes activités épreuve ou étape.
 */
class Utils {
    private static final String TAG = "Utils";

    private Context context;
    private SharedPreferences prefs;

    public Utils(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Lance l'activité à lancer une fois l'épreuve donnée est finie. Si l'étape contient une
     * épreuve suivante, lance l'activité d'épreuve correspondant à son type. Sinon, si le jeu contient
     * une étape suivante lance cette dernière. Le cas échéant, le jeu est fini et lance l'écran final.
     *
     * @param etape   Etape correspondant à l'épreuve finie
     * @param epreuve Epreuve finie
     */
    public void chargerEpreuveOuEtapeSuivante(Etape etape, Epreuve epreuve) {
        Intent intent = new Intent();
        if (etape == null || epreuve == null) {
            intent = new Intent(context, EtapeActivity.class);
            intent.putExtra(Config.PREF_ETAPE_COURANTE, 0);
        } else {
            Epreuve epreuveSuivante = etape.getEpreuve(epreuve.getNum() + 1);
            if (epreuveSuivante != null) {
                Type typeSuivant = epreuveSuivante.getType();
                if (typeSuivant == Type.QCM) {
                    intent = new Intent(context, QcmActivity.class);
                } else if (typeSuivant == Type.OUVERTE) {
                    intent = new Intent(context, QuestionOuverteActivity.class);
                } else if (typeSuivant == Type.PHOTO) {
                    intent = new Intent(context, PhotoActivity.class);
                }
                intent.putExtra(Config.EXTRA_ETAPE, etape.getNum());
                intent.putExtra(Config.EXTRA_EPREUVE, epreuveSuivante.getUri());

                prefs.edit()
                        .putString(Config.PREF_EPREUVE_COURANTE, epreuveSuivante.getUri())
                        .apply();
            } else {
                Etape etapeSuivante = GestionEtapes.getInstance(context).getEtape(etape.getNum() + 1);
                if (etapeSuivante == null) {
                    intent = new Intent(context, FinalActivity.class);
                    prefs.edit()
                            .putInt(Config.PREF_ETAPE_COURANTE, 0)
                            .putString(Config.PREF_EPREUVE_COURANTE, null)
                            .apply();
                } else {
                    Log.d(TAG, "charge etape num : " + etapeSuivante.getNum());
                    intent = new Intent(context, EtapeActivity.class);
                    prefs.edit()
                            .putInt(Config.PREF_ETAPE_COURANTE, etape.getNum() + 1)
                            .putString(Config.PREF_EPREUVE_COURANTE, null)
                            .apply();
                    intent.putExtra(Config.EXTRA_ETAPE, etapeSuivante.getNum());
                }
            }
        }
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * Augmente le score total du nombre de points donnés, correspondant à une épreuve réussie.
     *
     * @param pointsAjouter Nombre de points à ajouter au score total
     */
    public void augmenterPoints(int pointsAjouter) {
        int pointsTot = prefs.getInt(Config.PREF_SCORE, 0) + pointsAjouter;
        prefs.edit()
                .putInt(Config.PREF_SCORE, pointsTot)
                .apply();
        Log.d(TAG, "Points totaux : " + pointsTot);
    }

    /**
     * Retourne le score total sauvegardé dans les préférences de l'application.
     *
     * @return Score total
     */
    public int getPoints() {
        return prefs.getInt(Config.PREF_SCORE, 0);
    }

    /**
     * Remet toutes les préférences à zéro et lance l'activité correspondant à la première
     * étape du jeu.
     */
    public void resetPartie() {
        prefs.edit()
                .clear()
                .apply();
        chargerEpreuveOuEtapeSuivante(null, null);
    }

    /**
     * Retourne la durée du jeu, calculée à partir de l'heure et la date de début du jeu
     * et la date et heure courante. Cette durée est découpé en heures, minutes et secondes
     * et est placée dans un tableau.
     *
     * @return Durée du jeu
     */
    public int[] getDuree() {
        long debut = prefs.getLong(Config.PREF_TEMPS_DEBUT, 0);
        long fin = new Date().getTime();
        long difference = fin - debut;

        int secondsInMillis = 1000;
        int minutesInMillis = secondsInMillis * 60;
        int hoursInMillis = minutesInMillis * 60;

        int[] duree = new int[3];
        duree[0] = (int) difference / hoursInMillis;
        difference = difference % hoursInMillis;
        duree[1] = (int) difference / minutesInMillis;
        difference = difference % minutesInMillis;
        duree[2] = (int) difference / secondsInMillis;

        return duree;
    }

    /**
     * Partage avec d'autres personnes le score total du jeu une fois fini ainsi que le temps
     * total mis pour le finir.
     */
    public void partager(String duree, String score) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.titre_partage_final));
        intent.putExtra(Intent.EXTRA_TEXT,
                context.getString(R.string.contenu_partage_final, duree, score));
        intent = Intent.createChooser(intent, context.getString(R.string.partage_choix));
        context.startActivity(intent);
    }


}
