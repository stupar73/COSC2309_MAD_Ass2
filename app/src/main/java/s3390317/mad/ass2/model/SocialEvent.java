package s3390317.mad.ass2.model;

import java.util.Calendar;
import java.util.List;

public interface SocialEvent extends Comparable<SocialEvent>
{
    boolean finishesSameDay();

    String getId();

    String getTitle();

    void setTitle(String title);

    Calendar getStart();

    /**
     * @return The start date and time for this event as a String.
     */
    String getStartString();

    /**
     * @return The start date for this event as a String.
     */
    String getStartDateString();

    /**
     * @return The start time for this event as a String.
     */
    String getStartTimeString();

    void setStart(Calendar start);

    /**
     * Sets the start date for the event according to the given string.
     *
     * @return {@code true} if the date was successfully set, false otherwise.
     */
    boolean setStartDate(String dateStr);

    /**
     * Sets the start time for the event according to the given string.
     *
     * @return {@code true} if the time was successfully set, false otherwise.
     */
    boolean setStartTime(String timeStr);

    Calendar getEnd();

    /**
     * @return The end date and time for this event as a String.
     */
    String getEndString();

    /**
     * @return The end date for this event as a String.
     */
    String getEndDateString();

    /**
     * @return The end time for this event as a String.
     */
    String getEndTimeString();

    void setEnd(Calendar end);

    /**
     * Sets the end date for the event according to the given string.
     *
     * @return {@code true} if the date was successfully set, false otherwise.
     */
    boolean setEndDate(String dateStr);

    /**
     * Sets the end time for the event according to the given string.
     *
     * @return {@code true} if the date was successfully set, false otherwise.
     */
    boolean setEndTime(String timeStr);

    String getVenue();

    void setVenue(String venue);

    String getLocation();

    void setLocation(String location);

    String getNote();

    void setNote(String note);

    List<Contact> getAttendees();

    void setAttendees(List<Contact> attendees);

    void addAttendee(Contact attendee);
}
