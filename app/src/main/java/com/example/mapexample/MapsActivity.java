package com.example.mapexample;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapexample.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private ActivityResultLauncher<String[]> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions()
                    , stringBooleanMap -> {
                        if(stringBooleanMap.get(Manifest.permission.ACCESS_FINE_LOCATION)&&stringBooleanMap.get(Manifest.permission.ACCESS_COARSE_LOCATION)){
                            getLastLocation();
                        }
                    });

    private FusedLocationProviderClient fusedLocationClient;


    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        fusedLocationClient.getCurrentLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                Log.v("ttt","success");
                if(location != null){
                    LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
                    mMap.addMarker(new MarkerOptions().position(loc).title("myLocation"));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("ttt",e.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
        }else {
            getLastLocation();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        googleMap.getUiSettings().setZoomControlsEnabled(true);
       /* LatLng firstLocation = new LatLng(31.498255, 34.435646);
        LatLng secondLocation = new LatLng(31.49928176117343, 34.43674422211495);
        LatLng thirdLocation = new LatLng(31.500250916467085, 34.43769563934914);
        LatLng fourLocation = new LatLng(31.50095827789666, 34.43395103002826);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation,13));
        googleMap.addPolygon(new PolygonOptions()

                .add(firstLocation,secondLocation,thirdLocation,fourLocation)
                .strokeWidth(8)
                .strokeColor(Color.BLACK)
        );
        googleMap.addMarker(new MarkerOptions().position(firstLocation));
        googleMap.addMarker(new MarkerOptions().position(fourLocation));
*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}