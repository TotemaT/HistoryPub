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

package be.ipl.mobile.projet.historypub.pojo;

/**
 * Correspond à un zone d'un rayon donnée, autour d'une point géographique.
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

    /**
     * Retourne la distance entre le centre de la zone et le point donné.
     * Basé sur la formule de Haversine, cf <http://stackoverflow.com/a/12600225>.
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

    /**
     * Vérifie si un point géographique se trouve dans la zone.
     *
     * @param latitude  Latitude du point
     * @param longitude Longitude du point
     * @return True si le point se trouve dans la zone
     */
    public boolean contient(double latitude, double longitude) {
        return distanceAvec(latitude, longitude) <= rayon;
    }
}
