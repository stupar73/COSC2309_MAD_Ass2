package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import s3390317.mad.ass2.view.ViewEventActivity;
import s3390317.mad.ass2.view.model.EventArrayAdapter;
import s3390317.mad.ass2.view.model.IntentRequestCodes;

public class ViewEventListener implements AdapterView.OnItemClickListener
{
    private Activity activity;
    private EventArrayAdapter eventArrayAdapter;

    public ViewEventListener(Activity activity, EventArrayAdapter eventArrayAdapter)
    {
        this.activity = activity;
        this.eventArrayAdapter = eventArrayAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        Intent viewEventIntent = new Intent(activity, ViewEventActivity.class);
        viewEventIntent.putExtra("eventId",
                eventArrayAdapter.getItem(position).getId());
        activity.startActivityForResult(
                viewEventIntent, IntentRequestCodes.VIEW_EVENT_REQUEST);
    }
}
