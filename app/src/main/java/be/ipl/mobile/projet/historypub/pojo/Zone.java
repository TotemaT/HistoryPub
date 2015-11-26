package be.ipl.mobile.projet.historypub.pojo;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by matt on 10/11/15.
 */
public class Zone {

    private Location mLocation;
    private int rayon;

    public Zone(double latitude, double longitude, int rayon) {
        mLocation = new Location(LocationManager.GPS_PROVIDER);
        mLocation.setLatitude(latitude);
        mLocation.setLongitude(longitude);
        this.rayon = rayon;
    }

    public boolean contient(Location location) {
        float distance = location.distanceTo(mLocation);
        Log.d("Zone", "Distance : " + distance + " - Rayon : " + rayon);
        return distance <= rayon;
    }
}
