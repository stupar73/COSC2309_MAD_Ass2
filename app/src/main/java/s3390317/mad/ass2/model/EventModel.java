package s3390317.mad.ass2.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventModel
{
    private static EventModel singletonInstance = null;

    private Map<String, SocialEvent> events;
    private List<SocialEvent> sortedEventsList;
    private boolean sortedAscending;
    public Context appContext;

    public static EventModel getSingletonInstance()
    {
        if (singletonInstance == null)
        {
            singletonInstance = new EventModel();
        }
        return singletonInstance;
    }

    private EventModel()
    {
        events = new HashMap<>();
        sortedEventsList = new ArrayList<>();
        sortedAscending = true;

        // Add 10 sample events to the model
        /*for (int i = 0; i < 10; i++)
        {
            Calendar eventStart = Calendar.getInstance();
            eventStart.add(Calendar.DATE, i);
            Calendar eventEnd = (Calendar) eventStart.clone();
            eventEnd.add(Calendar.HOUR, 1);

            List<Contact> attendees = new ArrayList<>();

            SocialEvent event = new SimpleSocialEvent("Test event " + i,
                    eventStart, eventEnd, "Test venue " + i, "(0,0)",
                    "Test note " + i, attendees);

            addEvent(event);
        }*/


    }

    // TODO Temp function
    public void writeAll()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(appContext);

        for (SocialEvent e : sortedEventsList)
        {
            dbHelper.insertEvent(e);
        }
    }

    // TODO Temp function
    public void readAll()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(appContext);

        List<SocialEvent> dbEvents = dbHelper.getAllEvents();

        for (SocialEvent e : dbEvents)
        {
            addEvent(e);
        }
    }

    /**
     * Add an event to the model and resort the {@code sortedEventsList}
     *
     * @param event event to be added
     */
    public void addEvent(SocialEvent event)
    {
        events.put(event.getId(), event);
        sortedEventsList.add(event);
        sortEvents();
    }

    /**
     * Remove an event from the model.
     *
     * @param eventId id of event to be removed
     *
     * @return {@code true} if event successfully removed, {@code false}
     *         otherwise.
     */
    public boolean removeEvent(String eventId)
    {
        SocialEvent removed = events.remove(eventId);

        if (removed == null)
        {
            return false;
        }
        sortedEventsList.remove(removed);

        return true;
    }

    /**
     * Obtain a {@code SocialEvent} object from a given event id.
     *
     * @param id id of event to retrieve
     *
     * @return {@code SocialEvent} for given id, or {@code null} if not found.
     */
    public SocialEvent getEventById(String id)
    {
        return events.get(id);
    }

    /**
     * @return
     *      An unmodifiable list of events in the model (backed by the list
     *      in the model, i.e.: if new events are added to the model they
     *      are reflected in this list).
     */
    public List<SocialEvent> getEventList()
    {
        return Collections.unmodifiableList(sortedEventsList);
    }

    /**
     * @return
     *      A set of {@code Integer}s representing days in the given month
     *      which have events on them.
     */
    public Set<Integer> getDaysInMonthWithEvents(int month, int year)
    {
        Set<Integer> daysWithEvents = new HashSet<>();

        for (SocialEvent event : sortedEventsList)
        {
            Calendar eventStart = event.getStart();
            if (eventStart.get(Calendar.MONTH) == month
                    && eventStart.get(Calendar.YEAR) == year)
            {
                daysWithEvents.add(event.getStart().get(Calendar.DAY_OF_MONTH));
            }
        }
        return daysWithEvents;
    }

    /**
     * @return A list of {@code SocialEvents} on the given date.
     */
    public List<SocialEvent> getEventsOnDay(int day, int month, int year)
    {
        List<SocialEvent> events = new ArrayList<>();
        for (SocialEvent event : sortedEventsList)
        {
            Calendar eventStart = event.getStart();
            if (eventStart.get(Calendar.DAY_OF_MONTH) == day
                    && eventStart.get(Calendar.MONTH) == month
                    && eventStart.get(Calendar.YEAR) == year)
            {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Sort events according to the currently selected sort order.
     */
    public void sortEvents()
    {
        if (sortedAscending)
        {
            sortEventsAscending();
        }
        else
        {
            sortEventsDescending();
        }
    }

    /**
     * Sort events in ascending order (earliest at the top).
     */
    public void sortEventsAscending()
    {
        sortedAscending = true;
        Collections.sort(sortedEventsList, new Comparator<SocialEvent>()
        {
            @Override
            public int compare(SocialEvent e1, SocialEvent e2)
            {
                return e1.compareTo(e2);
            }
        });
    }

    /**
     * Sort events in descending order (latest at the top).
     */
    public void sortEventsDescending()
    {
        sortedAscending = false;
        Collections.sort(sortedEventsList, new Comparator<SocialEvent>()
        {
            @Override
            public int compare(SocialEvent e1, SocialEvent e2)
            {
                return e2.compareTo(e1);
            }
        });
    }
}
