package be.ipl.mobile.projet.jeudepiste;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.ipl.mobile.projet.jeudepiste.pojo.Etape;

/**
 * Created by matt on 10/11/15.
 */
public class GestionEtapes {
    private static final String TAG = "GestionEtapes";

    private List<Etape> etapes;
    private Context context;

    private static GestionEtapes instance;

    private GestionEtapes(Context context) {
        this.context = context;
        initialiserListeEpreuves();
    }

    public static GestionEtapes getInstance(Context context) {
        if (instance == null) {
            instance = new GestionEtapes(context);
        }
        return instance;
    }

    public void initialiserListeEpreuves() {
        etapes = new ArrayList<>();

        /* Lecture fichier xml */
        XmlPullParser parser;
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(context.getAssets().open("CampusAlma.xml"), null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Jeu");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {

                /* TODO Créer une nouvelle Epreuve à chaque noeud puis remplir les différents attributs des épreuves etc (Mais je suis fatigué là donc à faire plus tard) */
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                Log.i("XmlFile", parser.getName());
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Erreur lors de la lecture du fichier XML");
        }

    }

    public Etape getEtape(int numero) {
        return etapes.get(numero);
    }
}
