package s3390317.mad.ass2.view.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Facilitates the scheduling of an IntentService to run at a set interval.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = AlarmReceiver.class.getSimpleName();
    public static final int REQUEST_CODE = 123456;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.startService(
                new Intent(context, DistanceMatrixIntentService.class));
    }
}
