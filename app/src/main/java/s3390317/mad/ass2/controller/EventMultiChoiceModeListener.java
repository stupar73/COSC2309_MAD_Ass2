package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.model.SocialEvent;
import s3390317.mad.ass2.view.model.EventArrayAdapter;

/**
 * Enables deleting events from the {@code EventArrayAdapter} passed in (which
 * propagates the deletion to the model) by long pressing and selecting the
 * delete button in the contextual action bar.
 */
public class EventMultiChoiceModeListener
        implements AbsListView.MultiChoiceModeListener
{
    private Activity activity;
    private ListView eventList;
    private EventArrayAdapter eventArrayAdapter;

    public EventMultiChoiceModeListener(Activity activity, ListView eventList,
                                        EventArrayAdapter eventArrayAdapter)
    {
        this.activity = activity;
        this.eventList = eventList;
        this.eventArrayAdapter = eventArrayAdapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        activity.getMenuInflater().inflate(R.menu.actions_contextual_event_list,
                menu);

        // Change status bar colour to match CAB colour in Lollipop and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            activity.getWindow().setStatusBarColor(activity.getResources()
                    .getColor(R.color.action_mode_dark));
        }

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean checked)
    {
        // Get the number of selected items in the ListView
        int numSelected = eventList.getCheckedItemCount();
        // Change the CAB title to reflect the number of selected items
        mode.setTitle(numSelected + " Selected");
        // Update item's selected status in EventArrayAdapter
        eventArrayAdapter.toggleSelection(position);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.event_list_contextual_delete:
                SparseBooleanArray selectedItemsIds = eventArrayAdapter
                        .getSelectedItemsIds();
                // Remove all selected events
                for (int i = selectedItemsIds.size()-1; i >= 0; i--)
                {
                    // Check if current list item was selected
                    if (selectedItemsIds.valueAt(i))
                    {
                        // Get selected event from the EventArrayAdapter
                        SocialEvent selectedEvent = eventArrayAdapter
                                .getItem(selectedItemsIds.keyAt(i));
                        // Remove the event
                        eventArrayAdapter.remove(selectedEvent);
                        eventArrayAdapter.notifyDataSetChanged();
                    }
                }
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        // Reset status bar colour in Lollipop and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            activity.getWindow().setStatusBarColor(activity.getResources()
                    .getColor(android.R.color.transparent));
        }
        eventArrayAdapter.clearSelection();
    }
}
