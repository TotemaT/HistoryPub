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
        this.num = num;
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

    public Epreuve getEpreuve(int num) {
        if (num < 0 || num >= getNombreEpreuves()) {
            throw new IllegalArgumentException("L'index n'est pas compris dans la liste");
        }
        return epreuves.get(num);
    }

    public void addEpreuve(Epreuve epreuve) {
        epreuves.add(epreuve);
    }
}
