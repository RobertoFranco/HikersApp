package com.example.rober.hikersapp;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {

    private GoogleMap mMap;

    private String bestProvider;

    private Location location; // location

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final float MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    @Override
    public void onLocationChanged(Location location) {
        RequestLocation();
        Toast.makeText(this, "You changed your location", Toast.LENGTH_LONG).show();
    }

    private void RequestLocation(){
        try {

            //Select the provider
            bestProvider = locationManager.getBestProvider(new Criteria(), false);

            //Update Location
            locationManager.requestLocationUpdates(bestProvider, MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES, this);

            //Request Last Location
            location = locationManager.getLastKnownLocation(bestProvider);

            if(location != null){
                UpdateMap(); //Update UI Information
            }

        } catch (SecurityException e) {
            Toast.makeText(this, "Security Exception.", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Illegal Argument Exception.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Exception: Can Not Found Information.", Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateMap(){
        mMap.clear();
        LatLng miHouse = new LatLng(location.getLatitude(), location.getLatitude());
        mMap.addMarker(new MarkerOptions().position(miHouse).title("This is your location."));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miHouse, 18));
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private double latitude, longitude;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Lat Long
        latitude = getIntent().getDoubleExtra("lat", -34);
        longitude = getIntent().getDoubleExtra("lng", 151);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng miHouse = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(miHouse).title("This is your location."));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miHouse, 18));
    }



}
