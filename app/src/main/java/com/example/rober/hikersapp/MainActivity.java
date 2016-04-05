package com.example.rober.hikersapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.jar.Manifest;

public class MainActivity extends Activity implements android.location.LocationListener {

    //UI Elements
    private TextView latTxtView;
    private TextView lngTxtView;
    private TextView accuracyTxtView;
    private TextView speedTxtView;
    private TextView bearingTxtView;
    private TextView altitudeTxtView;
    private TextView addressTxtView;


    // Declaring a Location Manager
    protected LocationManager locationManager;

    private String bestProvider;

    private Location location; // location

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final float MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Components
        latTxtView = (TextView) findViewById(R.id.LatTxtView);
        lngTxtView = (TextView) findViewById(R.id.LngTxtView);
        accuracyTxtView = (TextView) findViewById(R.id.AccuracyTxtView);
        speedTxtView = (TextView) findViewById(R.id.SpeedTxtView);
        bearingTxtView = (TextView) findViewById(R.id.BearingTxtView);
        altitudeTxtView = (TextView) findViewById(R.id.AltitudeTextView);
        addressTxtView = (TextView) findViewById(R.id.AddressTxtView);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Request Location
        RequestLocation();

    }

    @Override
    protected void onPause() {
        super.onPause();

        try{
            //Removes all location updates for the specified LocationListener.
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Toast.makeText(MainActivity.this, "Security Exception. Could not remove location updates.", Toast.LENGTH_LONG).show();
        }

    }

    //Action to Map Button
    public void MapBtnPressed(View view) {
        //Launch the Map
        Intent i = new Intent("android.intent.action.MapActivity");

        try{
            Double a = location.getLatitude();
            Double b = location.getLongitude();

            if ( a != null && b != null){
                i.putExtra("lat", a);
                i.putExtra("lng", b);
                startActivityForResult(i, 1);
            }else {
                Toast.makeText(MainActivity.this, "Can not display map. Location not found.", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Can not display map. Location not found.", Toast.LENGTH_LONG).show();
        }



    }

    //Updates all UI elements withe the correct information
    private void UpdateInfo(Location location) throws Exception{

        latTxtView.setText("Latitude: " +       (( Double.toString(     location.getLatitude()  ) == null    )? "000.00"  : location.getLatitude() )  );
        lngTxtView.setText("Longitude: " +      (( Double.toString(     location.getLongitude() ) == null    )? "000.00"  : location.getLongitude())  );
        accuracyTxtView.setText("Accuracy: " +  (( Float.toString(      location.getAccuracy()  ) == null    )? "0.0"     : location.getAccuracy() )  + " m");
        speedTxtView.setText("Speed: " +        (( Float.toString(      location.getSpeed()     ) == null    )? "0.0"     : location.getSpeed()    )  + " m/s");
        bearingTxtView.setText("Bearing: " +    (( Float.toString(      location.getBearing()   ) == null    )? "0.0"     : location.getBearing()  )  );
        altitudeTxtView.setText("Altitude: " +  (( Double.toString(     location.getAltitude()  ) == null    )? "0.0"     : location.getAltitude() )  + " m");
        addressTxtView.setText(GetAddress());
    }

    //Get the Address from the location found
    private String GetAddress(){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> listAddresses = null;

        String address = "Address: \n";

        try {

            listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0){


                for (int i = 0; i < 4; i++){
                    if (i == 3){
                        address += listAddresses.get(0).getAddressLine(i) + ".";
                        continue;
                    }
                    address += listAddresses.get(0).getAddressLine(i) + ", ";
                }

                addressTxtView.setText(address);

            }
        } catch (Exception e) {
            return "No address found.";
        }

        return address;
    }

    private void RequestLocation(){
        try {

            //Select the provider
            bestProvider = locationManager.getBestProvider(new Criteria(), false);

            //Update Location
            locationManager.requestLocationUpdates(bestProvider, MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES, MainActivity.this);

            //Request Last Location
            location = locationManager.getLastKnownLocation(bestProvider);

            if(location != null){
                UpdateInfo(location); //Update UI Information
            }

        } catch (SecurityException e) {
            Toast.makeText(MainActivity.this, "Security Exception.", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(MainActivity.this, "Illegal Argument Exception.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Exception: Can Not Found Information.", Toast.LENGTH_LONG).show();
        }
    }

    //Called when the location has changed.
    @Override
    public void onLocationChanged(Location location) {
        try{
            UpdateInfo(location);
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Can not update information.", Toast.LENGTH_LONG).show();
        }
    }

    //Called when the provider is disabled by the user.
    @Override
    public void onProviderDisabled(String provider) {
    }

    //Called when the provider is enabled by the user.
    @Override
    public void onProviderEnabled(String provider) {
    }

    //Called when the provider status changes.
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
