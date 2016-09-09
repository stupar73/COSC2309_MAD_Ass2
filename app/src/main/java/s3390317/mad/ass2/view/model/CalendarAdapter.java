package s3390317.mad.ass2.view.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import s3390317.mad.ass2.R;

/**
 * Adapter for use with custom CalendarView (s3390317.mad.ass2.view.CalendarView).
 */
public class CalendarAdapter extends ArrayAdapter<Calendar>
{
    private Context context;
    private LayoutInflater inflater;
    private int month;
    private int year;
    private Set<Integer> daysWithEvents;
    private int selectedPosition = -1;

    public CalendarAdapter(Context context, List<Calendar> days,
                           int month, int year, Set<Integer> daysWithEvents)
    {
        super(context, R.layout.calendar_day, days);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.month = month;
        this.year = year;
        this.daysWithEvents = daysWithEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.calendar_day, parent, false);
        }

        Calendar date = getItem(position);
        TextView textView = (TextView) convertView.findViewById(
                R.id.calendar_date_text);

        // If this day has an event, add dot to cell
        if (daysWithEvents != null)
        {
            for (int eventDate : daysWithEvents)
            {
                if (dateIsInMonth(date)
                        && eventDate == date.get(Calendar.DAY_OF_MONTH))
                {
                    convertView.findViewById(R.id.calendar_date_has_event_image)
                            .setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        // Don't display days outside of the current month
        if (dateIsInMonth(date))
        {
            textView.setText(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
        }
        else
        {
            textView.setText("");
        }

        // Highlight today's date
        Calendar today = Calendar.getInstance();
        if (date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                && month == today.get(Calendar.MONTH)
                && year == today.get(Calendar.YEAR))
        {
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextColor(
                    context.getResources().getColor(R.color.calendar_today));
        }

        // Set selected day
        if (position == selectedPosition)
        {
            convertView.findViewById(R.id.calendar_date_selected)
                    .setVisibility(View.VISIBLE);
        }
        else
        {
            convertView.findViewById(R.id.calendar_date_selected)
                    .setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectedPosition(int position)
    {
        this.selectedPosition = position;
    }

    private boolean dateIsInMonth(Calendar date)
    {
        return (date.get(Calendar.MONTH) == month
                && date.get(Calendar.YEAR) == year);
    }
}
