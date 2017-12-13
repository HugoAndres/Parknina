package pe.edu.upc.parknina.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import pe.edu.upc.parknina.R;

public class LoggedMapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {

    private GoogleMap mMap;

    private final int[] mapTypes = {
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE
    };
    private int curMapTypeIndex = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_ENABLE_LOCATION = 98;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.loggedGoogleMap);
        mapFragment.getMapAsync(this);

        final FloatingActionButton fab = new FloatingActionButton(this);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageResource(R.drawable.ic_add_24dp);
        fab.setColorPressedResId(R.color.colorPrimaryLight);
        fab.setColorNormalResId(R.color.colorPrimary);
        fab.setColorRippleResId(R.color.white);
        fab.setShadow(true);

        new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                .addMenuItem(R.color.colorIcons, R.drawable.ic_account_box_24dp, "Parking Lot", R.color.colorIcons, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoggedMapsActivity.this, ParkingLotActivity.class));
                    }
                })
                .addMenuItem(R.color.colorIcons, R.drawable.ic_arrow_back_black_24dp, "Parking Ad", R.color.colorIcons, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addMenuItem(R.color.colorIcons, R.drawable.ic_invert_colors_black_24dp, "Parking Book", R.color.colorIcons, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addMenuItem(R.color.colorIcons, R.drawable.ic_exit_to_app_24dp, "Log Out", R.color.colorIcons, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoggedMapsActivity.this, LoginActivity.class));
                    }
                })
                .addMenuItem(R.color.colorIcons, R.drawable.ic_check_24dp, "Credit Card", R.color.colorIcons, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addMenuItem(R.color.colorIcons, R.drawable.ic_settings_24dp, "Configuration", R.color.colorIcons, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.colorPrimary)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageResource(R.drawable.ic_close_24dp);
                        fab.setColorNormalResId(R.color.colorPrimaryLight);
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageResource(R.drawable.ic_add_24dp);
                        fab.setColorNormalResId(R.color.colorPrimary);
                    }
                })
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkLocationPermission()) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(bestProvider, 2000, 0, this); // REMEMBER: It's necessary put first requestLocationUpdates() for method getLastKnownLocation() works
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                mMap.setMapType(mapTypes[curMapTypeIndex]);
                mMap.setTrafficEnabled(true);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                onLocationChanged(location);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (checkLocationPermission()) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng aux = new LatLng(latitude, longitude);
            //mMap.addMarker(new MarkerOptions().position(aux));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(aux));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null); //TODO: Put the last parameter
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission. ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //locationManager.requestLocationUpdates(provider, 400, 1, this);
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
                        if (checkGPSState()) {
                            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                            String bestProvider = locationManager.getBestProvider(criteria, true);
                            locationManager.requestLocationUpdates(bestProvider, 2000, 0, this); // REMEMBER: It's necessary put first requestLocationUpdates() for method getLastKnownLocation() works
                            Location location = locationManager.getLastKnownLocation(bestProvider);
                            if (location != null) {
                                mMap.setMapType(mapTypes[curMapTypeIndex]);
                                mMap.setTrafficEnabled(true);
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setZoomGesturesEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(false);
                                mMap.getUiSettings().setCompassEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                mMap.getUiSettings().setRotateGesturesEnabled(false);
                                onLocationChanged(location);
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                    LatLng sydney = new LatLng(-34, 151);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
                break;
            /*//This not work
            case MY_PERMISSIONS_ENABLE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                    LatLng sydney = new LatLng(-34, 151);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    Toast.makeText(this, "A problem occur. Please restart app.", Toast.LENGTH_LONG).show();
                }
                break;*/
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public final boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("We need your location")
                        .setMessage("You have to accept for use Google Maps.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LoggedMapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(LoggedMapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {
            checkGPSState();
            return true;
        }
    }

    public final boolean checkGPSState() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPS) {
            new AlertDialog.Builder(this)
                    .setMessage("GPS is disabled in your device. Would you like to enable it?. Then restart the app.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            //Go to turn on GPS manually
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    }).create()
                    .show();

            return false;
        }
        else {

            return true;
        }
    }

    public int getCurMapTypeIndex() {
        return curMapTypeIndex;
    }

    public void setCurMapTypeIndex(int curMapTypeIndex) {
        this.curMapTypeIndex = curMapTypeIndex;
    }
}
