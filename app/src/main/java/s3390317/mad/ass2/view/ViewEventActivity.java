package s3390317.mad.ass2.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.controller.OpenMapListener;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;
import s3390317.mad.ass2.view.model.DatabaseHelper;
import s3390317.mad.ass2.view.model.IntentRequestCodes;
import s3390317.mad.ass2.view.model.AndroidModel;

public class ViewEventActivity extends AppCompatActivity
{
    private EventModel model;
    private AndroidModel androidModel;
    private String eventId;
    private SocialEvent event;
    private boolean updateList = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.actions_view_event, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = EventModel.getSingletonInstance();
        androidModel = AndroidModel.getSingletonInstance(this);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        event = model.getEventById(eventId);

        populateFields();
    }

    private void populateFields()
    {
        setTitle(event.getTitle());

        TextView start = (TextView) findViewById(R.id.view_event_start_field);
        start.setText(event.getStartString());

        TextView end = (TextView) findViewById(R.id.view_event_end_field);
        end.setText(event.getEndString());

        TextView venue = (TextView) findViewById(R.id.view_event_venue_field);
        venue.setText(event.getVenue());

        TextView location = (TextView) findViewById(R.id.view_event_location_field);
        location.setText(getResources().getString(
                R.string.event_location_field_map, event.getLocation()));
        location.setOnClickListener(new OpenMapListener(this, event.getLocation()));

        TextView note = (TextView) findViewById(R.id.view_event_note_field);
        note.setText(event.getNote());

        TextView attendees = (TextView) findViewById(R.id.view_event_attendees_field);
        String attendeesStr = event.getAttendees().toString();
        attendees.setText(attendeesStr.substring(1, attendeesStr.length()-1));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.view_event_edit:
                launchEditEventActivity();
                return true;
            case R.id.view_event_delete:
                removeEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchEditEventActivity()
    {
        Intent editEventIntent = new Intent(this, EditEventActivity.class);
        editEventIntent.putExtra("eventId", eventId);
        this.startActivityForResult(
                editEventIntent, IntentRequestCodes.EDIT_EVENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == IntentRequestCodes.EDIT_EVENT_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                if (data.getBooleanExtra("remove", false))
                {
                    removeEvent();
                }
                else if (data.getBooleanExtra("modified", false))
                {
                    updateList = true;
                    populateFields();
                }
            }
        }
    }

    /**
     * Remove the event from the model.
     */
    private void removeEvent()
    {
        androidModel.removeEvent(eventId);
        Toast.makeText(this, event.getTitle() + " deleted.", Toast.LENGTH_SHORT)
                .show();
        updateList = true;
        this.finish();
    }

    @Override
    public void finish()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updateList", updateList);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }
}
