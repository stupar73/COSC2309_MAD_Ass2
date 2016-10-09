package s3390317.mad.ass2.view.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.util.List;

import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;

public class AndroidModel
{
    private static AndroidModel singletonInstance = null;

    private EventModel eventModel;
    private DatabaseHelper dbHelper;
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;

    public static AndroidModel getSingletonInstance(Context context)
    {
        if (singletonInstance == null)
        {
            singletonInstance = new AndroidModel(context);
        }
        return singletonInstance;
    }

    private AndroidModel(Context context)
    {
        this.eventModel = EventModel.getSingletonInstance();
        this.dbHelper = DatabaseHelper.getSingletonInstance(
                context.getApplicationContext());
        addAllEventsFromDb();
    }

    /**
     * Start the DistanceMatrixIntentService in the background to periodically
     * check for travel time to each event and notify if necessary.
     */
    public void startBackgroundDistanceMatrixService(Context context)
    {
        if (isAlarmRunning(context))
        {
            stopBackgroundDistanceMatrixService();
        }
        Intent intent = new Intent(context.getApplicationContext(),
                AlarmReceiver.class);

        alarmIntent = PendingIntent.getBroadcast(context,
                AlarmReceiver.REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        long interval = prefs.getLong("distance_check_freq_interval",
                DistanceMatrixIntentService.DEFAULT_INTERVAL_MS);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), interval, alarmIntent);
    }

    /**
     * Checks if the background service is already running.
     *
     * @return True if the alarm is already set to repeat the background
     *         service periodically, otherwise false.
     */
    private boolean isAlarmRunning(Context context)
    {
        Intent intent = new Intent(context.getApplicationContext(),
                AlarmReceiver.class);

        return PendingIntent.getBroadcast(context.getApplicationContext(),
                AlarmReceiver.REQUEST_CODE, intent,
                PendingIntent.FLAG_NO_CREATE)
                != null;
    }

    /**
     * Stop the background service.
     */
    private void stopBackgroundDistanceMatrixService()
    {
        if (alarmManager != null && alarmIntent != null)
        {
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
        }
    }

    /**
     * Add all events from the database into the in-memory model.
     * <br/>
     * Should only be run once at application start up.
     */
    private void addAllEventsFromDb()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                List < SocialEvent > events = dbHelper.getAllEvents();

                for (SocialEvent e : events)
                {
                    eventModel.addEvent(e);
                }
            }
        }).start();
    }

    /**
     * Add an event to both the in-memory model and the database.
     * @param event event to be added
     */
    public void addEvent(final SocialEvent event)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                eventModel.addEvent(event);
                dbHelper.addEvent(event);
            }
        }).start();
    }

    /**
     * Remove an event from both the in-memory model and the database.
     *
     * @param eventId id of event to be removed
     */
    public void removeEvent(final String eventId)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                eventModel.removeEvent(eventId);
                dbHelper.removeEvent(eventId);
            }
        }).start();
    }
}
