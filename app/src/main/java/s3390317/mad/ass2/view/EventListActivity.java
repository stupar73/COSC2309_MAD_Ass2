package s3390317.mad.ass2.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.controller.AddEventListener;
import s3390317.mad.ass2.controller.EventMultiChoiceModeListener;
import s3390317.mad.ass2.controller.NavigationItemSelectedListener;
import s3390317.mad.ass2.controller.ViewEventListener;
import s3390317.mad.ass2.model.EventModel;
import s3390317.mad.ass2.view.model.EventArrayAdapter;
import s3390317.mad.ass2.view.model.IntentRequestCodes;

public class EventListActivity extends AppCompatActivity
{
    private EventModel model;
    private ListView eventList;
    private EventArrayAdapter eventArrayAdapter;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.actions_event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Enable hamburger button to open navigation drawer
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch(item.getItemId())
        {
            case R.id.event_list_sort_asc:
                sortEventsAscending();
                item.setChecked(true);
                return true;
            case R.id.event_list_sort_desc:
                sortEventsDescending();
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        setUpActionBar();
        setUpNavDrawer();
        setUpFloatingActionButton();

        model = EventModel.getSingletonInstance();

        eventList = (ListView) findViewById(R.id.event_list_activity);

        eventArrayAdapter = new EventArrayAdapter(
                this,
                R.layout.event_list_item,
                true,
                model,
                model.getEventList()
        );

        eventList.setAdapter(eventArrayAdapter);
        eventList.setEmptyView(findViewById(R.id.empty_list_text));
        eventList.setOnItemClickListener(new ViewEventListener(this,
                eventArrayAdapter));
        eventList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        eventList.setMultiChoiceModeListener(new EventMultiChoiceModeListener(
                this, eventList, eventArrayAdapter));
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
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void sortEventsAscending()
    {
        model.sortEventsAscending();
        eventArrayAdapter.notifyDataSetChanged();
    }

    private void sortEventsDescending()
    {
        model.sortEventsDescending();
        eventArrayAdapter.notifyDataSetChanged();
    }
}
