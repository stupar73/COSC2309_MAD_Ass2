package s3390317.mad.ass2.view;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import s3390317.mad.ass2.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap map;
    private LatLng eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get event location from passed in intent
        Intent intent = getIntent();
        String eventLocStr = intent.getStringExtra("eventLocation");
        String[] latLngStrs = eventLocStr.split(",");
        eventLocation = new LatLng(Double.valueOf(latLngStrs[0]),
                Double.valueOf(latLngStrs[1]));

        /*
         * Obtain the SupportMapFragment and get notified when the map is ready
         * to be used.
         */
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will
     * be prompted to install it inside the SupportMapFragment.
     * This method will only be triggered once the user has installed Google
     * Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;

        map.addMarker(new MarkerOptions().position(eventLocation)
                .title("Event location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));
    }
}
