package s3390317.mad.ass2.view;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import s3390317.mad.ass2.R;
import s3390317.mad.ass2.view.model.AndroidModel;
import s3390317.mad.ass2.view.model.DistanceMatrixIntentService;

public class PreferencesActivity extends AppCompatActivity
{
    private AndroidModel androidModel;
    private SharedPreferences prefs;
    private EditText notifThresholdField;
    private EditText distanceCheckFreqField;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.actions_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.preferences_save:
                savePreferences();
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        setUpActionBar();

        androidModel = AndroidModel.getSingletonInstance(this);
        prefs =  PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());

        populateFields();
    }

    private void setUpActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void populateFields()
    {
        notifThresholdField = (EditText) findViewById(R.id.notif_threshold_field);
        notifThresholdField.setText(
                String.valueOf(prefs.getLong("notif_threshold",
                        DistanceMatrixIntentService.DEFAULT_THRESHOLD_MS)
                        / 1000 / 60));

        distanceCheckFreqField = (EditText) findViewById(R.id.distance_check_freq_field);
        distanceCheckFreqField.setText(
                String.valueOf(prefs.getLong("distance_check_freq_interval",
                        DistanceMatrixIntentService.DEFAULT_INTERVAL_MS)
                        / 1000 / 60));
    }

    private void savePreferences()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putLong("notif_threshold",
                Long.valueOf(notifThresholdField.getText().toString())
                        * 60 * 1000);
        prefsEditor.putLong("distance_check_freq_threshold",
                Long.valueOf(distanceCheckFreqField.getText().toString())
                        * 60 * 1000);
        prefsEditor.apply();

        // Restart background service with new values
        androidModel.startBackgroundDistanceMatrixService(getApplicationContext());
    }
}
