package edu.ucsb.cs.cs185.cs185final;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class InitialScreen extends AppCompatActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        Button newGame = (Button) findViewById(R.id.newGameButton);
        Button joinGame = (Button) findViewById(R.id.joinGameButton);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createGame = new Intent(getApplicationContext(), createGame.class);
                startActivity(createGame);
            }
        });

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(map);
            }
        });
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String locationProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        LatLng latlng;
        if(location != null){
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        else latlng = new LatLng(0,0);
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        mMap.addMarker(new MarkerOptions()
                .title("UCSB")
                .snippet("The most populous city in USSR.")
                .position(latlng));
    }
}

