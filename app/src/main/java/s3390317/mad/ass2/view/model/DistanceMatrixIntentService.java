package s3390317.mad.ass2.view.model;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;
import s3390317.mad.ass2.view.ViewEventActivity;

/**
 * Created by Stuart on 9/10/2016.
 */

public class DistanceMatrixIntentService extends IntentService
{
    public static final long DEFAULT_INTERVAL_MS = 5 * 60 * 1000; // 5min
    public static final long DEFAULT_THRESHOLD_MS = 15 * 60 * 1000; // 15min

    private static final String LOG_TAG = DistanceMatrixIntentService.class
            .getSimpleName();
    private static final String DISTANCE_MATRIX_URL
            = "https://maps.googleapis.com/maps/api/distancematrix/json"
            + "?origins=%s&destinations=%s";

    private EventModel model;

    public DistanceMatrixIntentService()
    {
        super("DistanceMatrixIntentService");
        this.model = EventModel.getSingletonInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.i(LOG_TAG, "Running distance matrix service");

        // Check network availability
        if (!isOnline())
        {
            Log.i(LOG_TAG, "No network connectivity");
            return;
        }

        // Get current location
        Location currentLocation = LocationTracker.getLocation(
                getApplicationContext());

        if (currentLocation == null)
        {
            Log.i(LOG_TAG, "Couldn't get current location");
            return;
        }

        try
        {
            /*
             * Call Google Distance Matrix API to calculate driving time
             * to each event
             */
            Map<SocialEvent, Long> travelTimeToEvents = getTravelTimeToEvents(
                    currentLocation);

            /*
             * If any meet the criteria ([travelTime + threshold] or less
             * until event start), notify user
             */
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            long threshold = prefs.getLong("notif_threshold",
                    DistanceMatrixIntentService.DEFAULT_THRESHOLD_MS);
            for (Map.Entry<SocialEvent, Long> pair
                    : travelTimeToEvents.entrySet())
            {
                SocialEvent event = pair.getKey();
                long travelTime = pair.getValue();

                long eventStartMs = event.getStart().getTimeInMillis();
                long travelTimeMs = travelTime * 1000;
                long currentTime = System.currentTimeMillis();

                // Skip events that don't meet the criteria
                if (((eventStartMs - (travelTimeMs + threshold)) > currentTime)
                        || (eventStartMs < currentTime))
                {
                    continue;
                }

                // Create intent for clicking on notification
                Intent viewEventIntent = new Intent(this,
                        ViewEventActivity.class);
                viewEventIntent.putExtra("eventId", event.getId());
                PendingIntent viewEventPendingIntent = PendingIntent.
                        getActivity(this, IntentRequestCodes.VIEW_EVENT_REQUEST,
                                viewEventIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                // Create the notification itself
                Notification notification
                        = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_event_notification)
                        .setContentTitle(getResources().getString(
                                R.string.notification_title, event.getTitle()))
                        .setContentText(getResources().getString(
                                R.string.notification_text,
                                millisToDisplay(eventStartMs - currentTime),
                                secondsToDisplay(travelTime)))
                        .setContentIntent(viewEventPendingIntent)
                        .setAutoCancel(true)
                        .build();

                // Display the notification
                NotificationManagerCompat notifyMgr
                        = NotificationManagerCompat.from(this);
                notifyMgr.notify(generateNotificationId(), notification);
            }
        } catch (Exception e)
        {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

    }

    /**
     * Check if the device has network connectivity.
     *
     * @return True if there is network connectivity, otherwise false.
     */
    private boolean isOnline()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Make a call to the Google Distance Matrix API to determine how long it
     * would take to travel by car from the user's current location to each
     * event individually.
     *
     * @param currentLocation the user's current location
     *
     * @return A Map of Events to time in milliseconds to travel to it by car.
     */
    private Map<SocialEvent, Long> getTravelTimeToEvents(Location currentLocation)
            throws IOException, JSONException
    {
        List<SocialEvent> events = model.getEventList();

        if (events.isEmpty())
        {
            return new HashMap<>();
        }

        // Construct 'destinations' parameter for URL
        String dests = "";
        for (int i = 0; i < events.size(); i++)
        {
            dests += events.get(i).getLocation();
            if (i != events.size() - 1)
            {
                // Add separator for multiple destinations
                dests += "%7C";
            }
        }
        // Construct 'origins' parameter for URL
        String origin = currentLocation.getLatitude() + ","
                + currentLocation.getLongitude();
        // Construct URL for distance matrix API call
        String urlStr = String.format(DISTANCE_MATRIX_URL, origin, dests);

        // Perform distance matrix API call
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200)
        {
            throw new HttpException(responseCode);
        }

        Map<SocialEvent, Long> travelTimeToEvents = new HashMap<>();
        /*
         * Parse JSON result of distance matrix API call
         * A Map is constructed linking event ID to travel time to event
         */
        String jsonStr = new Scanner(connection.getInputStream())
                .useDelimiter("\\A").next();
        JSONObject jsonResult =  new JSONObject(jsonStr);
        JSONArray distMatrixElements = jsonResult.getJSONArray("rows")
                .getJSONObject(0).getJSONArray("elements");
        for (int i = 0; i < distMatrixElements.length(); i++)
        {
            travelTimeToEvents.put(events.get(i),
                    distMatrixElements.getJSONObject(i)
                            .getJSONObject("duration").getLong("value"));
        }

        return travelTimeToEvents;
    }

    /**
     * Generate a random notification ID based on the current system time.
     *
     * @return A notification ID
     */
    private int generateNotificationId()
    {
        long now = System.currentTimeMillis();
        String nowStr = String.valueOf(now);

        return Integer.valueOf(nowStr.substring(nowStr.length() - 6));
    }

    /**
     * Convert milliseconds value into a String showing it in hours, minutes,
     * and seconds.
     *
     * @param millis milliseconds value to convert
     *
     * @return A string of the form "Xh, Ym, Zs"
     */
    private static String millisToDisplay(long millis)
    {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        // TODO Make this nicer (i.e. ignore fields that are 0)
        return String.format(Locale.getDefault(), "%dh, %dm, %ds",
                hours, minutes, seconds);
    }

    /**
     * Helper method for {@link #millisToDisplay(long)} to accept values in
     * seconds.
     *
     * @param secs seconds value to convert
     *
     * @return A string of the form "Xh, Ym, Zs"
     *
     * @see #millisToDisplay(long)
     */
    private static String secondsToDisplay(long secs)
    {
        return millisToDisplay(secs * 1000);
    }
}
