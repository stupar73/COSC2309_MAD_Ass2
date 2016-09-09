package s3390317.mad.ass2.controller;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.view.CalendarActivity;
import s3390317.mad.ass2.view.EventListActivity;

/**
 * Opens the relevant activity upon selection from the navigation draw.
 */
public class NavigationItemSelectedListener
        implements NavigationView.OnNavigationItemSelectedListener
{
    private Context context;
    private DrawerLayout drawerLayout;

    public NavigationItemSelectedListener(Context context,
                                          DrawerLayout drawerLayout)
    {
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        drawerLayout.closeDrawers();

        // Item selected is already the active activity, just close drawer
        if(item.isChecked())
        {
            return true;
        }

        item.setChecked(true);

        switch (item.getItemId())
        {
            case R.id.calendar_activity:
                if (!context.getClass().equals(CalendarActivity.class))
                {
                    Intent calendarIntent = new Intent(
                            context, CalendarActivity.class);
                    context.startActivity(calendarIntent);
                }
                return true;
            case R.id.event_list_activity:
                if (!context.getClass().equals(EventListActivity.class))
                {
                    Intent eventListIntent = new Intent(
                            context, EventListActivity.class);
                    context.startActivity(eventListIntent);
                }
                return true;
            default:
                return false;
        }
    }
}
