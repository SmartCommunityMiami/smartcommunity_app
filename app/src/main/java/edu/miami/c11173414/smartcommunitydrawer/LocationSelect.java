package edu.miami.c11173414.smartcommunitydrawer;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationSelect extends FragmentActivity implements
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        // mMap.setOnMapClickListener(this);
        // mMap.setOnMapLongClickListener(this);
        Toast.makeText(this, "Long press on desired location", Toast.LENGTH_LONG).show();
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
        // Add a marker in Sydney and move the camera
        LatLng miami = new LatLng(25.7617, -80.1918);
        mMap.addMarker(new MarkerOptions().position(miami).title("Marker in Miami"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miami));
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("onMapClick: ", "detected click");
        Toast.makeText(this, "("+latLng.latitude+", "+latLng.longitude+")", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.i("onMapLongClick: ", "detected long click on\nLat: " + point.latitude + "\nLon: " + point.longitude);
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        Intent returnIntent = new Intent();
        double lat = point.latitude;
        double lon = point.longitude;
        returnIntent.putExtra(getPackageName()+"LocationSelect.latitude", lat);
        returnIntent.putExtra(getPackageName()+"LocationSelect.longitude", lon);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
