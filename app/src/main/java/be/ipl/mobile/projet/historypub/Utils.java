package be.ipl.mobile.projet.historypub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Created by matt on 19/11/15.
 */
public class Utils {
    private static final String TAG = "Utils";
    private Context context;

    public Utils(Context context){
        this.context = context;
    }

    public void chargerEpreuveOuEtapeSuivante(Etape etape, Epreuve epreuve) {
        Intent intent = new Intent();
        if(etape==null||epreuve==null){
            intent = new Intent(context, EtapeActivity.class);
            intent.putExtra(Config.PREF_ETAPE_COURANTE,0);
        } else {
            Epreuve epreuveSuivante = etape.getEpreuve(epreuve.getNum() + 1);
            SharedPreferences.Editor edit = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE).edit();
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
                edit.putString(Config.PREF_EPREUVE_COURANTE, epreuveSuivante.getUri());
                edit.apply();
        /* sinon regarder les autres types */
            } else {
                Log.d(TAG, "pas d'epreuve suivante");
        /* l'étape est finie, charger la suivante */
                Etape etapeSuivante = GestionEtapes.getInstance(context).getEtape(etape.getNum() + 1);
                if (etapeSuivante == null) {
                /* charger écran de fin reprenant le temps total et le score final */
                    intent = new Intent(context, FinalActivity.class);
                    edit.putInt(Config.PREF_ETAPE_COURANTE, 0);
                    edit.putString(Config.PREF_EPREUVE_COURANTE, null);
                    edit.apply();
                } else {
                    Log.d(TAG, "charge etape num : " + etapeSuivante.getNum());
                    intent = new Intent(context, EtapeActivity.class);
                    edit.putInt(Config.PREF_ETAPE_COURANTE, etape.getNum() + 1);
                    edit.putString(Config.PREF_EPREUVE_COURANTE, null);
                    edit.apply();
                    intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etapeSuivante.getNum());
                }
            }
        }
        context.startActivity(intent);
    }

    public void augmenterPoints(int pointsAjouter) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        int pointsTot = prefs.getInt(Config.PREF_POINTS_TOTAUX, 0) + pointsAjouter;
        prefs.edit()
                .putInt(Config.PREF_POINTS_TOTAUX, pointsTot)
                .apply();
        Log.d(TAG, "Points totaux : " + pointsTot);
    }

    public int getPoints() {
        return context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE).getInt(Config.PREF_POINTS_TOTAUX, 0);
    }
}
