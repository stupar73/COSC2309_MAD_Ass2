package s3390317.mad.ass2.view.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import s3390317.mad.ass2.model.Contact;
import s3390317.mad.ass2.model.SimpleSocialEvent;
import s3390317.mad.ass2.model.SocialEvent;

/**
 * TODO
 * Created by Stuart on 11/09/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String LOG_TAG = DatabaseHelper.class.getName();

    private static DatabaseHelper singletonInstance;

    private static final String DATABASE_NAME = "socialEventPlannerDev.db";
    private static final int DATABASE_VERSION = 1;
    private static final String EVENT_TABLE_NAME = "events";
    private static final String EVENT_COLUMN_ID = "_id";
    private static final String EVENT_COLUMN_TITLE = "title";
    private static final String EVENT_COLUMN_START = "start";
    private static final String EVENT_COLUMN_END = "end";
    private static final String EVENT_COLUMN_VENUE = "venue";
    private static final String EVENT_COLUMN_LOCATION = "location";
    private static final String EVENT_COLUMN_NOTE = "note";

    public static DatabaseHelper getSingletonInstance(Context context)
    {
        if (singletonInstance == null)
        {
            singletonInstance = new DatabaseHelper(
                    context.getApplicationContext());
        }
        return singletonInstance;
    }

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + EVENT_TABLE_NAME + "("
                + EVENT_COLUMN_ID + " text primary key,"
                + EVENT_COLUMN_TITLE + " text,"
                + EVENT_COLUMN_START + " integer,"
                + EVENT_COLUMN_END + " integer,"
                + EVENT_COLUMN_VENUE + " text,"
                + EVENT_COLUMN_LOCATION + " text,"
                + EVENT_COLUMN_NOTE + " text)"
        );
        Log.i(LOG_TAG, "db created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Probably should do something more useful here
        db.execSQL("drop table if exists " + EVENT_TABLE_NAME);
        onCreate(db);
    }

    public List<SocialEvent> getAllEvents()
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(EVENT_TABLE_NAME, null, null, null, null, null,
                null);

        List<SocialEvent> events = new ArrayList<>();

        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    String id = cursor.getString(
                            cursor.getColumnIndex(EVENT_COLUMN_ID));
                    String title = cursor.getString(
                            cursor.getColumnIndex(EVENT_COLUMN_TITLE));
                    long start = cursor.getLong(
                            cursor.getColumnIndex(EVENT_COLUMN_START));
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTimeInMillis(start);
                    long end = cursor.getLong(
                            cursor.getColumnIndex(EVENT_COLUMN_END));
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTimeInMillis(end);
                    String venue = cursor.getString(
                            cursor.getColumnIndex(EVENT_COLUMN_VENUE));
                    String location = cursor.getString(
                            cursor.getColumnIndex(EVENT_COLUMN_LOCATION));
                    String note = cursor.getString(
                            cursor.getColumnIndex(EVENT_COLUMN_NOTE));

                    SocialEvent event = new SimpleSocialEvent(id, title,
                            startCal, endCal, venue, location, note,
                            new ArrayList<Contact>());
                    events.add(event);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return events;
    }

    public boolean addEvent(SocialEvent event)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = createContentValuesFromEvent(event);

        return db.insert(EVENT_TABLE_NAME, null, values) != -1;
    }

    public boolean updateEvent(SocialEvent event)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = createContentValuesFromEvent(event);
        String whereClause = EVENT_COLUMN_ID + " = ?";
        String[] whereArgs = new String[] {event.getId()};

        return db.update(EVENT_TABLE_NAME, values, whereClause, whereArgs) > 0;
    }

    public boolean removeEvent(String id)
    {
        SQLiteDatabase db = getWritableDatabase();

        return (db.delete(EVENT_TABLE_NAME, EVENT_COLUMN_ID + " = ?",
                new String[]{id}) > 0);
    }

    private ContentValues createContentValuesFromEvent(SocialEvent event)
    {
        ContentValues values = new ContentValues();
        values.put(EVENT_COLUMN_ID, event.getId());
        values.put(EVENT_COLUMN_TITLE, event.getTitle());
        values.put(EVENT_COLUMN_START, event.getStart().getTimeInMillis());
        values.put(EVENT_COLUMN_END, event.getEnd().getTimeInMillis());
        values.put(EVENT_COLUMN_VENUE, event.getVenue());
        values.put(EVENT_COLUMN_LOCATION, event.getLocation());
        values.put(EVENT_COLUMN_NOTE, event.getNote());

        return values;
    }
}
