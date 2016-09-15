package s3390317.mad.ass2.view.model;

import java.util.List;

import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;

/**
 * Created by stuart on 15/09/16.
 */
public class ModelDbHelper
{
    public static void addAllEventsFromDb(EventModel model,
                                          DatabaseHelper dbHelper)
    {
        List<SocialEvent> events = dbHelper.getAllEvents();

        for (SocialEvent e : events)
        {
            model.addEvent(e);
        }
    }

    public static void addEvent(EventModel model, DatabaseHelper dbHelper,
                                SocialEvent event)
    {
        model.addEvent(event);
        dbHelper.addEvent(event);
    }

    public static boolean removeEvent(EventModel model, DatabaseHelper dbHelper,
                                   String eventId)
    {
        boolean result;
        result = model.removeEvent(eventId);
        result &= dbHelper.removeEvent(eventId);

        return result;
    }
}
