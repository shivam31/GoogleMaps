package com.shivamdev.googlemaps;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends Activity {

    static final LatLng MYPOS = new LatLng(40, -79);

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if(googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            googleMap.setMyLocationEnabled(true);

            googleMap.setTrafficEnabled(true);

            googleMap.setIndoorEnabled(true);

            googleMap.setBuildingsEnabled(true);

            googleMap.getUiSettings().setZoomControlsEnabled(true);

            Marker marker = googleMap.addMarker(new MarkerOptions().position(MYPOS).title("Hello Shivam"));

        } catch (Exception e) {
            L.l(e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
