package be.ipl.mobile.projet.historypub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Created by matt on 19/11/15.
 */
public class Utils {
    private static final String TAG = "Utils";

    private Context context;
    private SharedPreferences prefs;

    public Utils(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
    }

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
                intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etape.getNum());
                intent.putExtra(Config.EXTRA_EPREUVE, epreuveSuivante.getUri());

                prefs.edit()
                        .putString(Config.PREF_EPREUVE_COURANTE, epreuveSuivante.getUri())
                        .apply();
        /* sinon regarder les autres types */
            } else {
                Log.d(TAG, "pas d'epreuve suivante");
        /* l'étape est finie, charger la suivante */
                Etape etapeSuivante = GestionEtapes.getInstance(context).getEtape(etape.getNum() + 1);
                if (etapeSuivante == null) {
                /* charger écran de fin reprenant le temps total et le score final */
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
                    intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etapeSuivante.getNum());
                }
            }
        }
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void augmenterPoints(int pointsAjouter) {
        int pointsTot = prefs.getInt(Config.PREF_POINTS_TOTAUX, 0) + pointsAjouter;
        prefs.edit()
                .putInt(Config.PREF_POINTS_TOTAUX, pointsTot)
                .apply();
        Log.d(TAG, "Points totaux : " + pointsTot);
    }

    public int getPoints() {
        return prefs.getInt(Config.PREF_POINTS_TOTAUX, 0);
    }

    public void resetPartie() {
        prefs.edit()
                .clear()
                .apply();
        chargerEpreuveOuEtapeSuivante(null, null);
    }

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

    public void partager() {
        /*
        Partage l'étape et épreuve courante avec la durée jusqu'à maintenant
        Faire la distinction entre partage durant la partie et une fois la partie finie!

        */
    }
}
