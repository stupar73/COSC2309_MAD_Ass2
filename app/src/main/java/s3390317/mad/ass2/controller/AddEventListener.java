package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import s3390317.mad.ass2.view.AddEventActivity;
import s3390317.mad.ass2.view.model.IntentRequestCodes;

public class AddEventListener implements View.OnClickListener
{
    private Activity activity;

    public AddEventListener(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onClick(View view)
    {
        Intent addEventIntent = new Intent(activity, AddEventActivity.class);
        activity.startActivityForResult(
                addEventIntent, IntentRequestCodes.ADD_EVENT_REQUEST);
    }
}
