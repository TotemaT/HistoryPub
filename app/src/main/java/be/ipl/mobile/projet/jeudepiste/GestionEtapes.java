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
import be.ipl.mobile.projet.jeudepiste.pojo.Zone;
import be.ipl.mobile.projet.jeudepiste.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.jeudepiste.pojo.epreuves.EpreuveATrou;
import be.ipl.mobile.projet.jeudepiste.pojo.epreuves.EpreuvePhoto;
import be.ipl.mobile.projet.jeudepiste.pojo.epreuves.EpreuveQCM;

/**
 * Created by matt on 10/11/15.
 */
public class GestionEtapes {
    /*TODO il reste a faire:
        - Fermer le fichier CampusAlma.xml une fois le traitement effectué
        - Ajouter les réponses pour les questions à choix multiple
        - Traiter tout ce qui est relatif à l'uri (Quand on clique on arrive sur la bonne épreuve/etape <=> Trouver le bon fichier html
    */
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
                if (parser.getEventType() != XmlPullParser.START_TAG)
                    continue;
                String name = parser.getName();

                if (name.equals("Etape"))
                    etapes.add(readEtape(parser));
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Erreur lors de la lecture du fichier XML");
        }

    }

    public Etape getEtape(int numero) {
        return etapes.get(numero);
    }

    private Etape readEtape(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Etape");
        int num = Integer.parseInt(parser.getAttributeValue(Config.NAMESPACE, "num"));
        String url = parser.getAttributeValue(Config.NAMESPACE, "url");
        Zone zone=null;
        List<Epreuve> list = new ArrayList<Epreuve>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Zone")) {
                zone = readZone(parser);
            } else if (name.equals("Epreuves"))
                list = readEpreuves(parser);
        }

        Etape e = new Etape(num,url,zone);
        Log.i("NOMBRE EPREUVES",list.size()+"");
        for(Epreuve ep: list) {
            e.addEpreuve(ep);
            Log.i("EPREUVE_NUM"+ep.getNum(),"Question: "+ep.getQuestion()+" type:"+ep.getType());
        }
        return e;
    }

    private Zone readZone(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,Config.NAMESPACE,"Zone");
        double latitude=0;
        double longitude=0;
        int rayon=0;

        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if(name.equals("Latitude"))
                latitude = Double.parseDouble(parser.nextText());
            else if(name.equals("Longitude"))
                longitude = Double.parseDouble(parser.nextText());
            else if(name.equals("Rayon"))
                rayon = Integer.parseInt(parser.nextText());
        }
        return new Zone(latitude,longitude,rayon);
    }

    private List<Epreuve> readEpreuves(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,Config.NAMESPACE,"Epreuves");
        List<Epreuve> list = new ArrayList<Epreuve>();
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if(name.equals("Epreuve"))
                list.add(readEpreuve(parser));
        }
        return list;
    }

    private Epreuve readEpreuve(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Epreuve");
        int num = Integer.parseInt(parser.getAttributeValue(Config.NAMESPACE,"num"));
        String uri = parser.getAttributeValue(Config.NAMESPACE, "uri");
        int points = Integer.parseInt(parser.getAttributeValue(Config.NAMESPACE, "points"));
        String type = parser.getAttributeValue(Config.NAMESPACE, "type");
        String question="";
        Zone zone = null;
        List<String> mots = new ArrayList<String>();

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if(name.equals("Question"))
                question = parser.nextText();
            else if(name.equals("Zone"))
                zone = readZone(parser);
            else if(name.equals("Reponse")){
                boolean isTrue = Boolean.parseBoolean(parser.getAttributeValue(Config.NAMESPACE,"bonne"));
                String rep = parser.nextText();
                //TODO comment je rajoute ça si jamais je ne sais pas instancier l'epreuve QCM avant?
            } else if(name.equals("Mot")){
                mots.add(parser.nextText());
            }
        }

        type = type.toUpperCase();
        switch (type){
            case "QCM":
                //TODO ajouter les reponses ici
                return new EpreuveQCM(num,question,uri,points);
            case "PHOTO":
                return new EpreuvePhoto(num,question,uri,points,zone);
            case "ATROU":
                EpreuveATrou e2 = new EpreuveATrou(num,question,uri,points);
                for(String mot: mots)
                    e2.addMot(mot);
                return e2;
            default:
                return null;
        }
    }
}
