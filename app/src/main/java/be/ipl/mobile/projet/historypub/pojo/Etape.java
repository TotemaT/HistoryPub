package be.ipl.mobile.projet.historypub.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;

/**
 * Created by matt on 10/11/15.
 */
public class Etape {
    private int num;
    private String url;
    private Zone zone;
    private Map<String, Epreuve> epreuves;

    public Etape(int num, String url, Zone zone) {
        this.num = num;
        this.url = url;
        this.zone = zone;
        this.epreuves = new HashMap<>();
    }

    public int getNum() {
        return num;
    }

    public String getUrl() {
        return url;
    }

    public Zone getZone() {
        return zone;
    }

    public int getNombreEpreuves() {
        return epreuves.size();
    }

    public Epreuve getEpreuve(String uri) {
        if (!epreuves.containsKey(uri)) {
            throw new IllegalArgumentException("L'uri n'existe pas");
        }
        return epreuves.get(uri);
    }

    public void addEpreuve(Epreuve epreuve) {
        epreuves.put(epreuve.getUri(), epreuve);
    }
}
