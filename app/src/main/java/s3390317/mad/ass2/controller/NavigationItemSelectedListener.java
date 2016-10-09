package s3390317.mad.ass2.controller;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.view.CalendarActivity;
import s3390317.mad.ass2.view.EventListActivity;
import s3390317.mad.ass2.view.PreferencesActivity;

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

        switch (item.getItemId())
        {
            case R.id.calendar_activity:
                item.setChecked(true);
                if (!context.getClass().equals(CalendarActivity.class))
                {
                    Intent calendarIntent = new Intent(
                            context, CalendarActivity.class);
                    context.startActivity(calendarIntent);
                }
                return true;
            case R.id.event_list_activity:
                item.setChecked(true);
                if (!context.getClass().equals(EventListActivity.class))
                {
                    Intent eventListIntent = new Intent(
                            context, EventListActivity.class);
                    context.startActivity(eventListIntent);
                }
                return true;
            case R.id.preferences_activity:
                Intent preferencesIntent = new Intent(
                        context, PreferencesActivity.class);
                context.startActivity(preferencesIntent);
                return true;
            default:
                return false;
        }
    }
}
