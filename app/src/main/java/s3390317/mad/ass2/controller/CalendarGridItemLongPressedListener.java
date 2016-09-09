package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Calendar;

import s3390317.mad.ass2.view.AddEventActivity;
import s3390317.mad.ass2.view.model.IntentRequestCodes;

/**
 * When attached as the {@code OnItemLongClickListener} to a Calendar
 * {@code GridView}, long pressing a date will start the AddEvent activity with
 * the start and end date pre-filled with the date long-pressed.
 */
public class CalendarGridItemLongPressedListener
        implements AdapterView.OnItemLongClickListener
{
    private Activity activity;
    private GridView calendarGrid;

    public CalendarGridItemLongPressedListener(Activity activity,
                                               GridView calendarGrid)
    {
        this.activity = activity;
        this.calendarGrid = calendarGrid;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id)
    {
        // Retrieve the long-pressed date
        Calendar date = (Calendar) parent.getItemAtPosition(position);

        Intent addEventIntent = new Intent(activity, AddEventActivity.class);
        addEventIntent.putExtra("datePassed", true);
        addEventIntent.putExtra("day", date.get(Calendar.DAY_OF_MONTH));
        addEventIntent.putExtra("month", date.get(Calendar.MONTH));
        addEventIntent.putExtra("year", date.get(Calendar.YEAR));

        activity.startActivityForResult(
                addEventIntent, IntentRequestCodes.ADD_EVENT_REQUEST);

        calendarGrid.setSelection(position);

        return true;
    }
}
