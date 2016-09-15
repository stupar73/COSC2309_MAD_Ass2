package s3390317.mad.ass2.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.controller.ContactPickerListener;
import s3390317.mad.ass2.controller.DatePickerDialogListener;
import s3390317.mad.ass2.controller.TimePickerDialogListener;
import s3390317.mad.ass2.model.Contact;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SimpleSocialEvent;
import s3390317.mad.ass2.model.SocialEvent;
import s3390317.mad.ass2.view.model.ContactDataManager;
import s3390317.mad.ass2.view.model.ContactDataManager.ContactQueryException;
import s3390317.mad.ass2.view.model.DatabaseHelper;
import s3390317.mad.ass2.view.model.IntentRequestCodes;
import s3390317.mad.ass2.view.model.ModelDbHelper;

public class AddEventActivity extends AppCompatActivity
{
    private String LOG_TAG = this.getClass().getName();

    private EventModel model;
    private DatabaseHelper dbHelper;
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
    private boolean dateWasPassed;
    private Calendar datePassed;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.actions_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.add_event_save:
                addEvent();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change up indicator to a cross
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        // Check if the parent activity passed a date as part of the intent
        if (getIntent().getBooleanExtra("datePassed", false))
        {
            dateWasPassed = true;
            Calendar today = Calendar.getInstance();

            int day = getIntent().getIntExtra("day", today.get(Calendar.DAY_OF_MONTH));
            int month = getIntent().getIntExtra("month", today.get(Calendar.MONTH));
            int year = getIntent().getIntExtra("year", today.get(Calendar.YEAR));

            datePassed = new GregorianCalendar(year, month, day);
        }

        model = EventModel.getSingletonInstance();
        dbHelper = DatabaseHelper.getSingletonInstance(this);

        attendees = new ArrayList<>();

        assignUiElements();
    }

    private void assignUiElements()
    {
        titleField = (EditText) findViewById(R.id.add_event_title_field);

        startDateField = (EditText) findViewById(R.id.add_event_start_date_field);
        if (dateWasPassed)
        {
            new DatePickerDialogListener(this, startDateField, datePassed);
        }
        else
        {
            new DatePickerDialogListener(this, startDateField);
        }

        startTimeField = (EditText) findViewById(R.id.add_event_start_time_field);
        new TimePickerDialogListener(this, startTimeField);

        endDateField = (EditText) findViewById(R.id.add_event_end_date_field);
        if (dateWasPassed)
        {
            new DatePickerDialogListener(this, endDateField, datePassed);
        }
        else
        {
            new DatePickerDialogListener(this, endDateField);
        }

        endTimeField = (EditText) findViewById(R.id.add_event_end_time_field);
        new TimePickerDialogListener(this, endTimeField);

        venueField = (EditText) findViewById(R.id.add_event_venue_field);

        locationField = (EditText) findViewById(R.id.add_event_location_field);

        noteField = (EditText) findViewById(R.id.add_event_note_field);

        attendeesField = (EditText) findViewById(R.id.add_event_attendees_field);
        attendeesField.setOnClickListener(new ContactPickerListener(this));
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
                catch (ContactQueryException e)
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
     * Add the event to the model. <br>
     * NOTE: No checking is being done to ensure fields have actually been
     * populated. Start and end date defaults to the current time and everything
     * else defaults to empty strings.
     */
    private void addEvent()
    {
        String title = titleField.getText().toString();
        String startDateStr = startDateField.getText().toString();
        String startTimeStr = startTimeField.getText().toString();
        String endDateStr = endDateField.getText().toString();
        String endTimeStr = endTimeField.getText().toString();
        String venue = venueField.getText().toString();
        String location = locationField.getText().toString();
        String note = noteField.getText().toString();

        SocialEvent event = new SimpleSocialEvent(title, startDateStr,
                startTimeStr, endDateStr, endTimeStr, venue, location, note,
                attendees);

        ModelDbHelper.addEvent(model, dbHelper, event);

        // Let the parent know that the event list may have changed
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updateList", true);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
