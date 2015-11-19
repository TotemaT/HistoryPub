package be.ipl.mobile.projet.historypub.pojo;

import java.util.ArrayList;
import java.util.List;

import be.ipl.mobile.projet.historypub.pojo.epreuves.Epreuve;

/**
 * Created by matt on 10/11/15.
 */
public class Etape {
    private int num;
    private String url;
    private Zone zone;
    private List<Epreuve> epreuves;

    public Etape(int num, String url, Zone zone) {
        this.num = num - 1;
        this.url = url;
        this.zone = zone;
        this.epreuves = new ArrayList<>();
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
        for (Epreuve epreuve : epreuves) {
            if (epreuve.getUri().equals(uri)) {
                return epreuve;
            }
        }
        throw new IllegalArgumentException("L'uri n'existe pas");
    }

    public Epreuve getEpreuve(int num) {
        if (num < 0 || num > epreuves.size() - 1) {
            return null;
        }
        return epreuves.get(num);
    }

    public void addEpreuve(Epreuve epreuve) {
        epreuves.add(epreuve);
    }
}
