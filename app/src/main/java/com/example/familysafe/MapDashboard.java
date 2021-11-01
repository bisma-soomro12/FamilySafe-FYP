package com.example.familysafe;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class MapDashboard extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    BetteryBroadCast brd;
    private LocationManager locationManager; private LocationListener locationListener; Geocoder geocoder;
    private final long MINI_TIME=1000, MIMI_DISTANCE=5;
    private LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dashboard);
        brd=new BetteryBroadCast();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GPSStatus status=new GPSStatus();
        this.registerReceiver(status,new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        BottomNavigationView navigationView=findViewById(R.id.bottomNavView);
        navigationView.setSelectedItemId(R.id.location);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.contacts:
                        startActivity(new Intent(getApplicationContext(),ContactInfo.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.location:
                        return true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext(),NeedHelp.class));
                        overridePendingTransition(0,0);
                        return true; }
                return false;
            }
        });
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
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("map error", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("map error", "Can't find style. Error: ", e);
        }
        // Add a marker in Sydney and move the camera
        LatLng latLng1 = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng1).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,18),5000,null);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try{
                    latLng= new LatLng(location.getLatitude(),location.getLongitude());
                    // geting lat lng individually for geocoder
                    double latitude =location.getLatitude();
                    double longitude= location.getLongitude();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18),5000,null);
                    // sending formate location
                    formatingLocation();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MINI_TIME,MIMI_DISTANCE,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MINI_TIME,MIMI_DISTANCE,locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        this.registerReceiver(brd,new IntentFilter(Intent.ACTION_BATTERY_LOW));
        super.onStart();
    }

    public void formatingLocation(){
        double latitude =latLng.latitude;
        double longitude= latLng.longitude;

        List<Address> addresses;
        geocoder= new Geocoder(this, Locale.getDefault());

        String address=null;
        String city =null;
        String state= null;
        String country= null;
        String postalCode=null;
        String knonName=null;

        try {
            addresses=geocoder.getFromLocation(latitude,longitude,1);
            address=addresses.get(0).getAddressLine(0);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
            country=addresses.get(0).getCountryName();
            postalCode=addresses.get(0).getPostalCode();
            knonName=addresses.get(0).getFeatureName();

            String message ="Address: "+address+"\nCity: "+city+"\nState: "+state+"\nCountry: "+country+"\nPostal Code: "+postalCode+"\nRoad Name: "+knonName;
            String no="03173341418";
            SmsManager smsManager= SmsManager.getDefault();
            smsManager.sendTextMessage(no,null,message,null,null);
            Toast.makeText(getApplicationContext(), "Location formate sent",Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}