package s3390317.mad.ass2.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class SimpleSocialEvent implements SocialEvent
{
    private String id;
    private String title;
    private Calendar start;
    private Calendar end;
    private String venue;
    private String location;
    private String note;
    private List<Contact> attendees;
    private final DateFormat dateTimeFormat = new SimpleDateFormat(
            "EEE, d MMM yyyy @ h:mm a", Locale.getDefault());
    private final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy",
            Locale.getDefault());
    private final DateFormat timeFormat = new SimpleDateFormat("h:mm a",
            Locale.getDefault());

    public SimpleSocialEvent(String id, String title, Calendar start,
                             Calendar end, String venue, String location,
                             String note, List<Contact> attendees)
    {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.venue = venue;
        this.location = location;
        this.note = note;
        this.attendees = attendees;
    }

    public SimpleSocialEvent(String title, Calendar start, Calendar end,
                             String venue, String location, String note,
                             List<Contact> attendees)
    {
        this(UUID.randomUUID().toString(), title, start, end, venue, location,
                note, attendees);
    }

    public SimpleSocialEvent(String title, String startDateStr,
                             String startTimeStr, String endDateStr,
                             String endTimeStr, String venue, String location,
                             String note, List<Contact> attendees)
    {
        this(UUID.randomUUID().toString(), title,
                Calendar.getInstance(TimeZone.getDefault()),
                Calendar.getInstance(TimeZone.getDefault()),
                venue, location,note, attendees);
        setStartDate(startDateStr);
        setStartTime(startTimeStr);
        setEndDate(endDateStr);
        setEndTime(endTimeStr);
    }

    @Override
    public boolean finishesSameDay()
    {
        return (start.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)
                && start.get(Calendar.YEAR) == end.get(Calendar.YEAR));
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public Calendar getStart()
    {
        return start;
    }

    @Override
    public String getStartString()
    {
        return dateTimeFormat.format(start.getTime());
    }

    @Override
    public String getStartDateString()
    {
        return dateFormat.format(start.getTime());
    }

    @Override
    public String getStartTimeString()
    {
        return timeFormat.format(start.getTime());
    }

    @Override
    public void setStart(Calendar start)
    {
        this.start = start;
    }

    @Override
    public boolean setStartDate(String dateStr)
    {
        try
        {
            // Retrieve calendar object from passed string
            Calendar newStartDate = Calendar.getInstance(TimeZone.getDefault());
            newStartDate.setTime(dateFormat.parse(dateStr));

            // Update start date values
            start.set(Calendar.DAY_OF_MONTH,
                    newStartDate.get(Calendar.DAY_OF_MONTH));
            start.set(Calendar.MONTH,
                    newStartDate.get(Calendar.MONTH));
            start.set(Calendar.YEAR,
                    newStartDate.get(Calendar.YEAR));

            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    @Override
    public boolean setStartTime(String timeStr)
    {
        try
        {
            // Retrieve calendar object from passed string
            Calendar newStartTime = Calendar.getInstance(TimeZone.getDefault());
            newStartTime.setTime(timeFormat.parse(timeStr));

            // Update start time values
            start.set(Calendar.HOUR,
                    newStartTime.get(Calendar.HOUR));
            start.set(Calendar.MINUTE,
                    newStartTime.get(Calendar.MINUTE));
            start.set(Calendar.AM_PM,
                    newStartTime.get(Calendar.AM_PM));

            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    @Override
    public Calendar getEnd()
    {
        return end;
    }

    @Override
    public String getEndString()
    {
        return dateTimeFormat.format(end.getTime());
    }

    @Override
    public String getEndDateString()
    {
        return dateFormat.format(end.getTime());
    }

    @Override
    public String getEndTimeString()
    {
        return timeFormat.format(end.getTime());
    }

    @Override
    public void setEnd(Calendar end)
    {
        this.end = end;
    }

    @Override
    public boolean setEndDate(String dateStr)
    {
        try
        {
            // Retrieve calendar object from passed string
            Calendar newEndDate = Calendar.getInstance(TimeZone.getDefault());
            newEndDate.setTime(dateFormat.parse(dateStr));

            // Update start date values
            end.set(Calendar.DAY_OF_MONTH,
                    newEndDate.get(Calendar.DAY_OF_MONTH));
            end.set(Calendar.MONTH,
                    newEndDate.get(Calendar.MONTH));
            end.set(Calendar.YEAR,
                    newEndDate.get(Calendar.YEAR));

            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    @Override
    public boolean setEndTime(String timeStr)
    {
        try
        {
            // Retrieve calendar object from passed string
            Calendar newEndTime = Calendar.getInstance(TimeZone.getDefault());
            newEndTime.setTime(timeFormat.parse(timeStr));

            // Update start time values
            end.set(Calendar.HOUR,
                    newEndTime.get(Calendar.HOUR));
            end.set(Calendar.MINUTE,
                    newEndTime.get(Calendar.MINUTE));
            end.set(Calendar.AM_PM,
                    newEndTime.get(Calendar.AM_PM));

            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    @Override
    public String getVenue()
    {
        return venue;
    }

    @Override
    public void setVenue(String venue)
    {
        this.venue = venue;
    }

    @Override
    public String getLocation()
    {
        return location;
    }

    @Override
    public void setLocation(String location)
    {
        this.location = location;
    }

    @Override
    public String getNote()
    {
        return note;
    }

    @Override
    public void setNote(String note)
    {
        this.note = note;
    }

    @Override
    public List<Contact> getAttendees()
    {
        return Collections.unmodifiableList(attendees);
    }

    @Override
    public void setAttendees(List<Contact> attendees)
    {
        this.attendees = attendees;
    }

    @Override
    public void addAttendee(Contact attendee)
    {
        this.attendees.add(attendee);
    }

    @Override
    public int compareTo(SocialEvent e)
    {
        return start.compareTo(e.getStart());
    }
}
