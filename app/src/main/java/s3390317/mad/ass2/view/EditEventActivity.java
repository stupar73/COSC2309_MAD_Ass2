package s3390317.mad.ass2.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.controller.ContactPickerListener;
import s3390317.mad.ass2.controller.DatePickerDialogListener;
import s3390317.mad.ass2.controller.TimePickerDialogListener;
import s3390317.mad.ass2.model.Contact;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;
import s3390317.mad.ass2.view.model.ContactDataManager;
import s3390317.mad.ass2.view.model.DatabaseHelper;
import s3390317.mad.ass2.view.model.IntentRequestCodes;

public class EditEventActivity extends AppCompatActivity
{
    private String LOG_TAG = this.getClass().getName();

    private EventModel model;
    private DatabaseHelper dbHelper;
    private String eventId;
    private SocialEvent event;
    private EditText titleField;
    private EditText startDateField;
    private EditText startTimeField;
    private EditText endDateField;
    private EditText endTimeField;
    private EditText venueField;
    private EditText locationField;
    private EditText noteField;
    private EditText attendeesField;
    private List<Contact> attendees;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.actions_edit_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.edit_event_save:
                saveChanges();
                return true;
            case R.id.edit_event_delete:
                removeEvent();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change up indicator to a cross
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(
                R.drawable.ic_close_white_24dp);

        model = EventModel.getSingletonInstance();
        dbHelper = DatabaseHelper.getSingletonInstance(this);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        event = model.getEventById(eventId);

        attendees = new ArrayList<>();

        populateFields();
    }

    private void populateFields()
    {
        titleField = (EditText) findViewById(R.id.edit_event_title_field);
        titleField.setText(event.getTitle());

        startDateField = (EditText) findViewById(R.id.edit_event_start_date_field);
        startDateField.setText(event.getStartDateString());
        new DatePickerDialogListener(this, startDateField);

        startTimeField = (EditText) findViewById(R.id.edit_event_start_time_field);
        startTimeField.setText(event.getStartTimeString());
        new TimePickerDialogListener(this, startTimeField);

        endDateField = (EditText) findViewById(R.id.edit_event_end_date_field);
        endDateField.setText(event.getEndDateString());
        new DatePickerDialogListener(this, endDateField);

        endTimeField = (EditText) findViewById(R.id.edit_event_end_time_field);
        endTimeField.setText(event.getEndTimeString());
        new TimePickerDialogListener(this, endTimeField);

        venueField = (EditText) findViewById(R.id.edit_event_venue_field);
        venueField.setText(event.getVenue());

        locationField = (EditText) findViewById(R.id.edit_event_location_field);
        locationField.setText(event.getLocation());

        noteField = (EditText) findViewById(R.id.edit_event_note_field);
        noteField.setText(event.getNote());

        attendeesField = (EditText) findViewById(R.id.edit_event_attendees_field);
        attendeesField.setOnClickListener(new ContactPickerListener(this));
        attendees.addAll(event.getAttendees());
        updateAttendeesField();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == IntentRequestCodes.PICK_CONTACTS_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                ContactDataManager contactsManager = new ContactDataManager(
                        this, data);
                String name = "";
                String email = "";
                try
                {
                    name = contactsManager.getContactName();
                    email = contactsManager.getContactEmail();
                }
                catch (ContactDataManager.ContactQueryException e)
                {
                    Log.e(LOG_TAG, e.getMessage());
                }
                attendees.add(new Contact(name, email));
                updateAttendeesField();
            }
        }
    }

    /**
     * Update the attendees field with a comma-delimited string of selected
     * contacts.
     */
    private void updateAttendeesField()
    {
        List<String> attendeesNames = new ArrayList<>();
        for (Contact c : attendees)
        {
            System.out.println(c);
            attendeesNames.add(c.toString());
        }
        attendeesField.setText(TextUtils.join(", ", attendeesNames));
    }

    /**
     * Save changes made to the event to the model. <br>
     * NOTE: No checking is being done to ensure fields have actually been
     * populated.
     */
    private void saveChanges()
    {
        // Set all the fields of the event, even if they haven't all been changed
        event.setTitle(titleField.getText().toString());
        event.setStartDate(startDateField.getText().toString());
        event.setStartTime(startTimeField.getText().toString());
        event.setEndDate(endDateField.getText().toString());
        event.setEndTime(endTimeField.getText().toString());
        event.setVenue(venueField.getText().toString());
        event.setLocation(locationField.getText().toString());
        event.setNote(noteField.getText().toString());
        event.setAttendees(attendees);

        dbHelper.updateEvent(event);

        // Let the parent know the event was modified
        Intent returnIntent = new Intent();
        returnIntent.putExtra("modified", true);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Tell the parent activity that the event should be removed from the model.
     */
    private void removeEvent()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("remove", true);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
