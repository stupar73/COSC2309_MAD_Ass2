package s3390317.mad.ass2.view.model;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.model.SocialEvent;

/**
 * Adapter for use with {@code SocialEvents}s
 */
public class EventArrayAdapter extends ArrayAdapter<SocialEvent>
{
    private Context context;
    private EventModel model;
    private AndroidModel androidModel;
    private int layoutResource;
    private boolean isStandalone;
    private SparseBooleanArray selectedItemsIds;

    public EventArrayAdapter(Context context, EventModel model,
                             AndroidModel androidModel, List<SocialEvent> events,
                             int resource, boolean isStandalone)
    {
        super(context, resource, events);
        this.context = context;
        this.model = model;
        this.androidModel = androidModel;
        this.layoutResource = resource;
        this.isStandalone = isStandalone;
        this.selectedItemsIds = new SparseBooleanArray();

        this.model.sortEvents();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResource, parent, false);
        }

        SocialEvent event = getItem(position);

        if (isStandalone)
        {
            TextView eventDayOfMonth = (TextView) view.findViewById(
                    R.id.event_day_of_month);
            eventDayOfMonth.setText(String.format(Locale.getDefault(), "%d",
                    event.getStart().get(Calendar.DAY_OF_MONTH)));

            TextView eventMonth = (TextView) view.findViewById(R.id.event_month);
            eventMonth.setText(event.getStart().getDisplayName(Calendar.MONTH,
                    Calendar.SHORT, Locale.getDefault()));
        }

        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        eventTitle.setText(event.getTitle());
        TextView eventTime = (TextView) view.findViewById(R.id.event_time);
        if (event.finishesSameDay())
        {
            // hh:mm AM/PM - hh:mm AM/PM
            eventTime.setText(view.getResources().getString(R.string.event_time,
                    event.getStartTimeString(), event.getEndTimeString()));
        }
        else
        {
            // hh:mm AM/PM - dd/MM/yyyy hh:mm AM/PM
            eventTime.setText(view.getResources().getString(R.string.event_time,
                    event.getStartTimeString(), event.getEndString()));
        }
        return view;
    }

    @Override
    public void remove(SocialEvent event)
    {
        androidModel.removeEvent(event.getId());
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged()
    {
        model.sortEvents();
        super.notifyDataSetChanged();
    }

    public void toggleSelection(int position)
    {
        selectView(position, !selectedItemsIds.get(position));
    }

    public void selectView(int position, boolean selected)
    {
        if (selected)
        {
            selectedItemsIds.put(position, selected);
        }
        else
        {
            selectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public void clearSelection()
    {
        selectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedItemsIds()
    {
        return selectedItemsIds;
    }
}
