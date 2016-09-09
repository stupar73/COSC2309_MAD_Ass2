package s3390317.mad.ass2.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import java.util.Calendar;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.controller.AddEventListener;
import s3390317.mad.ass2.controller.CalendarGridItemLongPressedListener;
import s3390317.mad.ass2.controller.NavigationItemSelectedListener;
import s3390317.mad.ass2.controller.CalendarGridItemSelectedListener;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.view.model.EventArrayAdapter;
import s3390317.mad.ass2.view.model.IntentRequestCodes;

public class CalendarActivity extends AppCompatActivity
{
    private EventModel model;
    private ActionBarDrawerToggle drawerToggle;
    private CalendarView calendarView;
    private GridView calendarGrid;
    private ListView eventList;
    private EventArrayAdapter eventListAdapter;
    private Calendar selectedDate;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Enable hamburger button to open navigation drawer
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setUpActionBar();
        setUpNavDrawer();
        setUpFloatingActionButton();

        calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarGrid = (GridView) findViewById(R.id.calendar_grid);
        eventList = (ListView) findViewById(R.id.calendar_event_list);

        eventList.setEmptyView(findViewById(R.id.empty_list_text));

        model = EventModel.getSingletonInstance();
        calendarView.setModel(model);

        calendarGrid.setOnItemClickListener(
                new CalendarGridItemSelectedListener(
                this, model, calendarView, eventList, eventListAdapter));

        calendarGrid.setOnItemLongClickListener(
                new CalendarGridItemLongPressedListener(this, calendarGrid));
    }

    private void setUpActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNavDrawer()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        NavigationView navView = (NavigationView) findViewById(
                R.id.drawer_navigation_view);

        navView.setNavigationItemSelectedListener(
                new NavigationItemSelectedListener(this, drawerLayout));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setUpFloatingActionButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(
                R.id.fab_add);
        fab.setOnClickListener(new AddEventListener(this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == IntentRequestCodes.VIEW_EVENT_REQUEST)
                || (requestCode == IntentRequestCodes.ADD_EVENT_REQUEST))
        {
            if (resultCode == RESULT_OK)
            {
                if (data.getBooleanExtra("updateList", false))
                {
                    if (selectedDate != null
                            && eventListAdapter != null)
                    {
                        eventListAdapter.clear();
                        eventListAdapter.addAll(model.getEventsOnDay(
                                selectedDate.get(Calendar.DAY_OF_MONTH),
                                selectedDate.get(Calendar.MONTH),
                                selectedDate.get(Calendar.YEAR)));
                        eventListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
