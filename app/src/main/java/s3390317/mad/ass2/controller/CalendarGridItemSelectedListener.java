package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;
import s3390317.mad.ass2.view.CalendarView;
import s3390317.mad.ass2.view.model.EventArrayAdapter;

/**
 * When attached as the {@code OnItemClickListener} to a Calendar
 * {@code GridView}, pressing a date will populate the given
 * {@code EventArrayAdapter} with the events for that date.
 */
public class CalendarGridItemSelectedListener
        implements AdapterView.OnItemClickListener
{
    private Activity activity;
    private EventModel model;
    private CalendarView calendar;
    private ListView eventList;
    private EventArrayAdapter eventArrayAdapter;

    public CalendarGridItemSelectedListener(Activity activity, EventModel model,
                                            CalendarView calendar,
                                            ListView eventList,
                                            EventArrayAdapter eventArrayAdapter)
    {
        this.activity = activity;
        this.model= model;
        this.calendar = calendar;
        this.eventList = eventList;
        this.eventArrayAdapter = eventArrayAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        Calendar date = (Calendar) parent.getItemAtPosition(position);

        List<SocialEvent> eventsInMonth = model.getEventsOnDay(
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.MONTH), date.get(Calendar.YEAR));

        eventArrayAdapter = new EventArrayAdapter(
                activity,
                R.layout.calendar_event_list_item,
                false,
                model,
                eventsInMonth
        );
        eventList.setAdapter(eventArrayAdapter);
        eventList.setOnItemClickListener(new ViewEventListener(
                activity, eventArrayAdapter));
        eventList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        eventList.setMultiChoiceModeListener(new EventMultiChoiceModeListener(
                activity, eventList, eventArrayAdapter));

        calendar.getCalendarAdapter().setSelectedPosition(position);
        calendar.getCalendarAdapter().notifyDataSetChanged();
    }
}
