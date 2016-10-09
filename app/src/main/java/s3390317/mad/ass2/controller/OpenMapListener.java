package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import s3390317.mad.ass2.view.MapActivity;

public class OpenMapListener implements View.OnClickListener
{
    private Activity activity;
    private String eventLocation;

    public OpenMapListener(Activity activity, String eventLocation)
    {
        this.activity = activity;
        this.eventLocation = eventLocation;
    }
    @Override
    public void onClick(View v)
    {
        Intent viewMapIntent = new Intent(activity, MapActivity.class);
        viewMapIntent.putExtra("eventLocation", eventLocation);
        activity.startActivity(viewMapIntent);
    }
}
