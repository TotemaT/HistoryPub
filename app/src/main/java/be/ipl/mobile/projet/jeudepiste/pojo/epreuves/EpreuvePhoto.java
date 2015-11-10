package be.ipl.mobile.projet.jeudepiste.pojo.epreuves;

import be.ipl.mobile.projet.jeudepiste.pojo.Zone;

/**
 * Created by matt on 10/11/15.
 */
public class EpreuvePhoto extends Epreuve {

    private Zone zone;

    public EpreuvePhoto(int num, String question, String uri, int points, Zone zone) {
        super(num, question, uri, Type.PHOTO, points);
        this.zone = zone;
    }

    public boolean estDansLaZone(double latitude, double longitude) {
        return zone.contient(latitude, longitude);
    }
}
