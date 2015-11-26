package be.ipl.mobile.projet.historypub.pojo.epreuves;

import android.location.Location;

import be.ipl.mobile.projet.historypub.pojo.Zone;

/**
 * Created by matt on 10/11/15.
 */
public class EpreuvePhoto extends Epreuve {

    private Zone zone;

    public EpreuvePhoto(int num, String question, String uri, int points, Zone zone) {
        super(num, question, uri, Type.PHOTO, points);
        this.zone = zone;
    }

    public boolean estDansLaZone(Location location) {
        return zone.contient(location);
    }
}
