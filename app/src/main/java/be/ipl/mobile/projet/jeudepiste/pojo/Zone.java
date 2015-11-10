package be.ipl.mobile.projet.jeudepiste.pojo;

/**
 * Created by matt on 10/11/15.
 */
public class Zone {

    private static final int RAYON_TERRE = 6371000;

    private double latitude;
    private double longitude;
    private int rayon;

    public Zone(double latitude, double longitude, int rayon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.rayon = rayon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRayon() {
        return rayon;
    }

    /*
        Retourne la distance entre le centre de la zone et les coordonnées données.
        Basé sur la formule de Haversine, cf <http://stackoverflow.com/a/12600225>
    */
    private int distanceAvec(double latitude, double longitude) {
        double distanceLatitude = Math.toRadians(this.latitude - latitude);
        double distanceLongitude = Math.toRadians(this.longitude - longitude);

        double a = Math.sin(distanceLatitude / 2) * Math.sin(distanceLatitude / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(latitude))
                * Math.sin(distanceLongitude / 2) * Math.sin(distanceLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(RAYON_TERRE * c));
    }

    public boolean contient(double latitude, double longitude) {
        return distanceAvec(latitude, longitude) <= rayon;
    }
}
