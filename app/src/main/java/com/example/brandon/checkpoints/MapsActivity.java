package com.example.brandon.checkpoints;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

//import java.lang.Object.*;
//import com.google.android.gms.maps.

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public String run;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    public LatLng[] course;
    public double[] timeMil;
    public MarkerOptions[] markers;
    int currentMarker=0;
    private Boolean check=false;
    private Chronometer clock;
    String currentName="NA", currentLevel="NA", currentTime="NA";

    public void beginner(View view){
        course = new LatLng[5];
        course[0]=new LatLng(43.472647, -80.539396);
        course[1]=new LatLng(43.472988, -80.539651);
        course[2]=new LatLng(43.473112, -80.539339);
        course[3]=new LatLng(43.473648, -80.539656);
        course[4]=new LatLng(43.473436, -80.540192);
        timeMil = new double[course.length];
        markers = new MarkerOptions[5];

        check=true;
        populateMap();
        view.setVisibility(View.GONE);
        View b = findViewById(R.id.button3);
        View c = findViewById(R.id.button4);
        EditText d = (EditText)findViewById(R.id.editText);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);

        run+=d.toString();
        currentName=d.getText().toString();
        currentLevel="Beginner";
        d.setVisibility(View.GONE);

        run+="/Beginner/";
    }

    public void intermediate(View view){
        course = new LatLng[5];
        course[0]=new LatLng(43.472711, -80.540478);
        course[1]=new LatLng(43.472337, -80.540054);
        course[2]=new LatLng(43.472217, -80.539845);
        course[3]=new LatLng(43.472466, -80.539834);
        course[4]=new LatLng(43.472789, -80.540215);
        timeMil = new double[course.length];
        markers = new MarkerOptions[5];

        check=true;
        populateMap();
        view.setVisibility(View.GONE);
        View b = findViewById(R.id.button2);
        View c = findViewById(R.id.button4);
        EditText d = (EditText)findViewById(R.id.editText);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        run+=d.toString();
        currentName=d.getText().toString();
        currentLevel="Intermediate";
        d.setVisibility(View.GONE);
        run+="Intermediate/";
    }

    public void expert(View view) {
        course = new LatLng[5];
        course[0]=new LatLng(43.472797, -80.540220);
        course[1]=new LatLng(43.472614, -80.540757);
        course[2]=new LatLng(43.473104, -80.541299);
        course[3]=new LatLng(43.473813, -80.539700);
        course[4]=new LatLng(43.472968, -80.539695);
        timeMil = new double[course.length];
        markers = new MarkerOptions[5];

        check=true;
        populateMap();
        view.setVisibility(View.GONE);
        View b = findViewById(R.id.button2);
        View c = findViewById(R.id.button3);
        EditText d = (EditText)findViewById(R.id.editText);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        run+=d.toString();
        currentName=d.getText().toString();
        currentLevel="Advanced";
        d.setVisibility(View.GONE);
        run+="Expert/";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_maps2);
        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(1)
                .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
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
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    private void populateMap()
    {
        for (int i=0; i<course.length; i++)
        {
            if (currentMarker==i)
            {
                markers[i] = new MarkerOptions().position((course[i])).title("Checkpoint "+(i+1)).snippet("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markers[i]);
            }
            else
            {
                markers[i] = new MarkerOptions().position((course[i])).title("Checkpoint " + (i + 1));
                mMap.addMarker(markers[i]);
            }
        }
    }

    private double mdist(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk =  (180/3.14169);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        mLocation=location;
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        double test;

        if(check) {
            test = mdist(markers[currentMarker].getPosition().latitude, markers[currentMarker].getPosition().longitude, currentLatitude, currentLongitude);

            if (test < 10) {
                if(currentMarker==0)
                {
                    clock = (Chronometer) findViewById(R.id.chronometer);
                    clock.setBase(SystemClock.elapsedRealtime());
                    clock.start();
                }
                timeMil[currentMarker] = System.currentTimeMillis();
                currentMarker++;
                mMap.clear();
                populateMap();
            }

            if (currentMarker == markers.length) {
                clock.stop();
                Firebase ref = new Firebase("https://checkpoints.firebaseio.com/");
                Firebase stats = ref.child(currentLevel).child(currentName);
                stats.child("Time").setValue(clock.getText().toString());
                stats.push();
                check=false;
                double[] runTimes = new double[timeMil.length];
                //calculate full race time and dis
                for (int i = 1; i < timeMil.length; i++) {
                    runTimes[i - 1] = timeMil[i] - timeMil[i - 1];
                }
                runTimes[timeMil.length - 1] = timeMil[timeMil.length - 1] - timeMil[0];
                run+= clock.getText().toString();
                run+="/";

                //run= name/level/time/
                /*try {
                    FileOutputStream fos = openFileOutput("records.txt", Context.MODE_PRIVATE);
                    fos.write(run.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}
