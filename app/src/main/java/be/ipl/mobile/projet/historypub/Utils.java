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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

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
     * Partage du texte avec d'autres personnes.
     *
     * @param titre   Titre du message à partager
     * @param contenu Contenu du message à partager
     */
    public void partager(String titre, String contenu) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, titre);
        intent.putExtra(Intent.EXTRA_TEXT, contenu);
        intent = Intent.createChooser(intent, context.getString(R.string.partage_choix));
        context.startActivity(intent);
    }

    /**
     * Partage le score total du jeu une fois fini ainsi que le temps mis pour le finir.
     *
     * @param duree Duree totale mise pour terminer le jeu
     * @param score Score totale marqué lors du jeu
     */
    public void partagerFinal(String duree, String score) {
        partager(context.getString(R.string.titre_partage_final),
                context.getString(R.string.contenu_partage_final, duree, score));
    }

    /**
     * Partage le score total engrangé après une étape ainsi que le temps mis pour arriver à la fin
     * de cette épreuve, en comptant depuis le début du jeu.
     *
     * @param epreuve Numéro de l'épreuve dans l'étape
     * @param etape   Numéro de l'étape
     * @param duree   Durée entre le début du jeu et la fin de l'épreuve
     * @param points  Nombre de points gagné jusqu'à maintenant
     */
    public void partagerEpreuve(int epreuve, int etape, String duree, String points) {
        partager(context.getString(R.string.titre_partage_epreuve),
                context.getString(R.string.contenu_partage_epreuve, epreuve, etape, duree, points));
    }

    /**
     * Affiche un dialogue expliquant la réponse de l'épreuve finie. Permet aussi de partager son
     * avancement.
     *
     * @param etape   Etape courante
     * @param epreuve Epreuve courante
     * @param duree   Duree entre le début du jeu et maintenant
     */
    public void getDialogExplicatif(final Etape etape, final Epreuve epreuve, final String duree) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Explication")
                .setMessage(epreuve.getExplication())
                .setPositiveButton(R.string.continuer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* Rien à faire ici, dismiss le dialog et entre dans OnDismissListener */
                    }
                })
                .setNeutralButton(R.string.partager, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Rien ici, on override le bouton par après pour éviter qu'il dismiss le dialog */
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        chargerEpreuveOuEtapeSuivante(etape, epreuve);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    Permet d'afficher le partage, mais sans quitter le dialog, évite donc de remettre
                    l'utilisateur sur la question à son retour, lui permettant de répondre à nouveau.
                 */
                partagerEpreuve(epreuve.getNum(), etape.getNum(), duree,
                        context.getResources().getQuantityString(R.plurals.points, getPoints(), getPoints()));
            }
        });
    }


}
