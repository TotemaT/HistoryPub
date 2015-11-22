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

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.ipl.mobile.projet.historypub.pojo.Etape;
import be.ipl.mobile.projet.historypub.pojo.Zone;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveATrou;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveOuverte;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuvePhoto;
import be.ipl.mobile.projet.historypub.pojo.epreuves.EpreuveQCM;
import be.ipl.mobile.projet.historypub.pojo.epreuves.Reponse;
import be.ipl.mobile.projet.historypub.pojo.epreuves.ReponseQCM;

/**
 * Gère la lecture du fichier XML et permet ensuite de récupérer les différentes étapes
 * et épreuves du jeu.
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

    /**
     * Lis le fichier XML et crée les différents éléments du jeu
     */
    private void initialiserListeEpreuves() {
        etapes = new ArrayList<>();

        /* Lecture fichier xml */
        XmlPullParser parser;
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(context.getAssets().open(context.getString(R.string.prefix) + File.separator + Config.FICHIER_ALMA), null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Jeu");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();

                if (name.equals("Etape")) {
                    etapes.add(readEtape(parser));
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Erreur lors de la lecture du fichier XML");
        }
    }

    /**
     * Retourne l'étape décrite par son numéro
     *
     * @param numero Numéro d'identification de l'étape
     * @return L'étape demandé ou null si le numéro ne correspond à aucune étape
     */
    public Etape getEtape(int numero) {
        if (numero < 0 || numero >= etapes.size()) {
            return null;
        }
        return etapes.get(numero);
    }

    /**
     * Retourne une étape créée en lisant le fichier XML.
     *
     * @param parser Parser permettant de lire le fichier XML
     * @return Etape lue dans le XML
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Etape readEtape(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Etape");
        int num = Integer.parseInt(parser.getAttributeValue(Config.NAMESPACE, "num"));
        String url = parser.getAttributeValue(Config.NAMESPACE, "url");
        Zone zone = null;
        List<Epreuve> list = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Zone")) {
                zone = readZone(parser);
            } else if (name.equals("Epreuves"))
                list = readEpreuves(parser);
        }

        Etape e = new Etape(num, url, zone);
        for (Epreuve ep : list)
            e.addEpreuve(ep);

        return e;
    }

    /**
     * Retourne une zone en lisant le fichier XML.
     *
     * @param parser Parser permettant de lire le fichier XML
     * @return Zone lue dans le XML
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Zone readZone(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Zone");
        double latitude = 0;
        double longitude = 0;
        int rayon = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Latitude"))
                latitude = Double.parseDouble(parser.nextText());
            else if (name.equals("Longitude"))
                longitude = Double.parseDouble(parser.nextText());
            else if (name.equals("Rayon"))
                rayon = Integer.parseInt(parser.nextText());
        }
        return new Zone(latitude, longitude, rayon);
    }

    /**
     * Retourne une liste d'épreuve en lisant le fichier XML.
     *
     * @param parser Parser permettant de lire le fichier XML
     * @return Liste d'épreuves lues dans le XML
     * @throws IOException
     * @throws XmlPullParserException
     */
    private List<Epreuve> readEpreuves(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Epreuves");
        List<Epreuve> list = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Epreuve")) {
                Epreuve e = readEpreuve(parser);
                if (e != null)
                    list.add(e);
            }
        }
        return list;
    }

    /**
     * Retourne une épreuve en lisant le fichier XML.
     *
     * @param parser Parser permettant de lire le fichier XML
     * @return Epreuve lue dans le XML
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Epreuve readEpreuve(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, Config.NAMESPACE, "Epreuve");
        int num = Integer.parseInt(parser.getAttributeValue(Config.NAMESPACE, "num"));
        String uri = parser.getAttributeValue(Config.NAMESPACE, "uri");
        int points = Integer.parseInt(parser.getAttributeValue(Config.NAMESPACE, "points"));
        String type = parser.getAttributeValue(Config.NAMESPACE, "type");
        String question = "";
        Zone zone = null;
        List<String> mots = new ArrayList<String>();
        List<ReponseQCM> reponses = new ArrayList<ReponseQCM>();
        List<Reponse> reponsesOuvertes = new ArrayList<>();
        Reponse reponse = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Question"))
                question = parser.nextText();
            else if (name.equals("Zone"))
                zone = readZone(parser);
            else if (name.equals("Reponse")) {
                if (parser.getAttributeValue(Config.NAMESPACE, "bonne") == null || parser.getAttributeValue(Config.NAMESPACE, "bonne").isEmpty()) {
                    reponsesOuvertes.add(new Reponse(parser.nextText()));
                } else {
                    boolean isTrue = Boolean.parseBoolean(parser.getAttributeValue(Config.NAMESPACE, "bonne"));
                    reponses.add(new ReponseQCM(parser.nextText(), isTrue));
                }
            } else if (name.equals("Mot")) {
                mots.add(parser.nextText());
            }
        }

        type = type.toUpperCase();
        switch (type) {
            case "QCM":
                EpreuveQCM e1 = new EpreuveQCM(num, question, uri, points);
                for (ReponseQCM rep : reponses)
                    e1.addReponse(rep);
                return e1;
            case "PHOTO":
                return new EpreuvePhoto(num, question, uri, points, zone);
            case "ATROU":
                EpreuveATrou e2 = new EpreuveATrou(num, question, uri, points);
                for (String mot : mots)
                    e2.addMot(mot);
                return e2;
            case "OUVERTE":
                return new EpreuveOuverte(num, question, uri, points, reponsesOuvertes);
            default:
                return null;
        }
    }
}
