package com.example.petcareapp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    String locationString = "";
    Location myLocation;
    LatLng pickedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //AKO NOSIME LOKACIJA OTVORI
        if(getIntent().getSerializableExtra("location") != null){
            locationString = getIntent().getStringExtra("location");
        }
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

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng SDGoce = new LatLng(41.9959105,21.3897568);
        float zoomLevel=15.0f;
//        mMap.addMarker(new MarkerOptions().position(SDGoce).title("Studentski Dom Goce Delcev"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SDGoce,zoomLevel));

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Button btn = (Button) findViewById(R.id.btnDoneMaps);
        mMap.setMyLocationEnabled(true);
        btn.setVisibility(View.GONE);
        if(!locationString.equals("")){ //Check if you get to this activity from post
            String temp[] = locationString.split(" ");
            pickedLocation = new LatLng(Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
            mMap.addMarker(new MarkerOptions().position(pickedLocation).title("MyLocation"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickedLocation,zoomLevel));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    //Do nothing, this listener is to prevent putting markers on click.
                }
            });
        }else{
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    pickedLocation = latLng;
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.addMarker(markerOptions);
                    btn.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    public void doneAddMarker(View view){
        Intent data = new Intent(MapsActivity.this,ReportLostActivity.class);

        data.putExtra("latitude",pickedLocation.latitude);
        data.putExtra("longitude",pickedLocation.longitude);
        setResult(RESULT_OK,data);
        startActivity(data);
        finish();
        /*
        Dismiss current activity and send the GeoPoint to previous activity.
        Figure out how your gonna let the user know that he already picked a location.
        */
    }
}