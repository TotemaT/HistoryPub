package be.ipl.mobile.projet.historypub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveOuverte;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Reponse;
import be.ipl.mobile.projet.historypub.pojo.epreuves.ReponseQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Type;

/**
 * Created by matt on 19/11/15.
 */
public class Utils {
    private static final String TAG = "Utils";
    private static int points=0;

    public static void chargerEpreuveOuEtapeSuivante(Context context, Etape etape, Epreuve epreuve) {
        Epreuve epreuveSuivante = etape.getEpreuve(epreuve.getNum() + 1);
        Intent intent = new Intent();
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
            /* sinon regarder les autres types */
        } else {
            Log.d(TAG, "pas d'epreuve suivante");
            /* l'étape est finie, charger la suivante */
            Etape etapeSuivante = GestionEtapes.getInstance(context).getEtape(etape.getNum() + 1);
            if (etapeSuivante == null) {
                /* charger écran de fin reprenant le temps total et le score final */
            } else {
                Log.d(TAG, "charge etape num : " + etapeSuivante.getNum());
                intent = new Intent(context, EtapeActivity.class);
                intent.putExtra(Config.EXTRA_ETAPE_COURANTE, etapeSuivante.getNum());
            }
        }
        context.startActivity(intent);
    }

    public static void augmenterPoints(Activity act, int pointsAjouter) {
        SharedPreferences.Editor edit = act.getPreferences(Context.MODE_PRIVATE).edit();
        Log.i("POINTS_TOTAUX",act.getPreferences(Context.MODE_PRIVATE).getInt(Config.PREF_POINTS_TOTAUX,0)+"");
        points+=pointsAjouter;
        edit.putInt(Config.PREF_POINTS_TOTAUX, points);
    }

    public static int getPoints(){
        return points;
    }

}
