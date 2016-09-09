package s3390317.mad.ass2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.view.model.CalendarAdapter;

public class CalendarView extends LinearLayout
{

    private final DateFormat MONTH_FORMAT = new SimpleDateFormat(
            "MMMM yyyy", Locale.getDefault());
    private Calendar displayedMonth;
    private TextView monthHeader;
    private ImageView prevBtn;
    private ImageView nextBtn;
    private GridView calendarGrid;
    private EventModel model;

    public CalendarView(Context context)
    {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setModel(EventModel model)
    {
        this.model = model;
        updateCalendar();
    }

    private void init(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);

        // Start in the current month
        displayedMonth = Calendar.getInstance();

        monthHeader = (TextView) findViewById(R.id.calendar_month_display);
        prevBtn = (ImageView) findViewById(R.id.calendar_prev_button);
        nextBtn = (ImageView) findViewById(R.id.calendar_next_button);
        calendarGrid = (GridView) findViewById(R.id.calendar_grid);

        nextBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                displayedMonth.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        prevBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                displayedMonth.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        updateCalendar(null);
    }

    public void updateCalendar()
    {
        if (model != null)
        {
            updateCalendar(model.getDaysInMonthWithEvents(
                    displayedMonth.get(Calendar.MONTH),
                    displayedMonth.get(Calendar.YEAR)));
        }
        else
        {
            updateCalendar(null);
        }
    }

    public void updateCalendar(Set<Integer> daysWithEvents)
    {
        List<Calendar> cells = new ArrayList<>();
        Calendar cal = (Calendar) displayedMonth.clone();
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = displayedMonth.get(Calendar.MONTH);
        int year = displayedMonth.get(Calendar.YEAR);

        // Determine cell for start of displayed month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int monthStartCell = cal.get(Calendar.DAY_OF_WEEK) - 1;

        // Move calendar back to the beginning of the week (in previous month)
        cal.add(Calendar.DAY_OF_MONTH, -monthStartCell);

        // Fill cell array with blanks for previous month's days
        while (cells.size() < monthStartCell)
        {
            cells.add((Calendar)cal.clone());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Fill cell array with current month's days
        for (int i = 0; i < daysInMonth; i++)
        {
            cells.add((Calendar)cal.clone());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarGrid.setAdapter(new CalendarAdapter(
                getContext(), cells, month, year, daysWithEvents));

        monthHeader.setText(MONTH_FORMAT.format(displayedMonth.getTime()));
    }

    public CalendarAdapter getCalendarAdapter()
    {
        return (CalendarAdapter) calendarGrid.getAdapter();
    }
}
