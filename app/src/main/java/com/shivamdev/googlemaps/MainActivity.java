package com.shivamdev.googlemaps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {


    EditText etStartAddress, etFinalAddress;
    LatLng startAddressPosition, finalAddressPosition;
    Marker addressMarker;
    private GoogleMap googleMap;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStartAddress = (EditText) findViewById(R.id.addressEditText);
        etFinalAddress = (EditText) findViewById(R.id.finalAddressEditText);


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


        } catch (Exception e) {
            L.l(e.getMessage());
        }
    }

    public void showAddressMarker(View view) {

        String newAddress = etStartAddress.getText().toString();

        if (newAddress != null) {
            new PlaceAMarker().execute(newAddress);
        }

    }

    public void getDirections(View view) {

        String startingAddress = etStartAddress.getText().toString();
        String finalAddress = etFinalAddress.getText().toString();

        if (startingAddress.equals("") || (finalAddress.equals(""))) {
            L.t(this, "Enter a Starting and Ending address");
        } else {
            new GetDirections().execute(startingAddress, finalAddress);
        }

    }

    protected void getLatLng(String address, boolean setDestination) {

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";

        HttpGet httpGet = new HttpGet(uri);

        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse httpResponse;

        StringBuilder sb = new StringBuilder();

        try {
            httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();

            InputStream is = httpEntity.getContent();

            int byteData;

            while ((byteData = is.read()) != -1) {

                sb.append((char) byteData);

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(sb.toString());

            //jsonArray = jsonObject.getJSONArray("results");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).
                    getJSONObject("geometry").getJSONObject("location").getDouble("lat");


            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0).
                    getJSONObject("geometry").getJSONObject("location").getDouble("lng");


            if (setDestination) {
                finalAddressPosition = new LatLng(lat, lng);
            } else {
                finalAddressPosition = new LatLng(lat, lng);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PlaceAMarker extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... address) {

            String startAddress = address[0];

            startAddress = startAddress.replaceAll(" ", "%20");

            getLatLng(startAddress, false);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            addressMarker = googleMap.addMarker(new MarkerOptions()
                    .position(startAddressPosition)
                    .title("Address"));
        }
    }

    private class GetDirections extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... addresses) {

            String startAddress = addresses[0];

            startAddress = startAddress.replaceAll(" ", "%20");

            getLatLng(startAddress, false);

            String finalAddress = addresses[1];

            finalAddress = finalAddress.replaceAll(" ", "%20");

            getLatLng(finalAddress, true);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String geoUriString = "http://maps.google.com/maps?addr=" +
                    startAddressPosition.latitude + "," +
                    startAddressPosition.longitude + "&daddr=" +
                    finalAddressPosition.latitude + "," +
                    finalAddressPosition.longitude;

            Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));
            startActivity(mapCall);
        }
    }
}
