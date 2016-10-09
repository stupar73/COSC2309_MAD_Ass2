package s3390317.mad.ass2.view.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class LocationTracker
{
    private static final String LOG_TAG = LocationTracker.class.getSimpleName();

    /**
     * Get the device's current location from the GPS if available, otherwise
     * the network. If neither are available/allowed the return will be null.
     *
     * @return The device's current location according to, in order of
     *         preference, the GPS or the network. Null if neither are
     *         available/allowed.
     */
    public static Location getLocation(Context context)
    {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(LOCATION_SERVICE);
        Location location = null;

        boolean networkAvailable = locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER);
        boolean gpsAvailable = locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER);

        // Get location from GPS if available
        if (gpsAvailable)
        {
            try
            {
                locationManager.requestSingleUpdate(
                        LocationManager.GPS_PROVIDER,
                        new EmptyLocationListener(), null);

                location = locationManager.getLastKnownLocation(
                            LocationManager.GPS_PROVIDER);
            }
            catch(SecurityException e)
            {
                Log.i(LOG_TAG, "Cannot get location from GPS, android"
                        + ".permission.ACCESS_FINE_LOCATION not granted.");
            }
        }
        // Otherwise, get location from network if available
        else if (networkAvailable)
        {
            try
            {
                locationManager.requestSingleUpdate(
                        LocationManager.NETWORK_PROVIDER,
                        new EmptyLocationListener(), null);

                location = locationManager.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER);
            }
            catch(SecurityException e)
            {
                Log.i(LOG_TAG, "Cannot get location from network, android"
                        + ".permission.ACCESS_COARSE_LOCATION not granted.");
            }
        }

        return location;
    }

    /**
     * Blank LocationListener since none of the methods are required for this
     * task.
     */
    private static class EmptyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location){}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}

        @Override
        public void onProviderEnabled(String provider){}

        @Override
        public void onProviderDisabled(String provider){}
    }
}
