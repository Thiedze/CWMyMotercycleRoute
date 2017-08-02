package cw.de.cwmymotercycleroute;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    private GoogleMaps googleMaps;

    private GoogleMapsDirection googleMapsDirection;

    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        googleMaps = new GoogleMaps(this);
        googleMapsDirection = new GoogleMapsDirection(this);
        weather = new Weather(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps.setMaps(googleMap);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            googleMaps.showCurrentPosition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMaps.showCurrentPosition();
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_route:
                Intent intent = new Intent(this, SetLocationActivity.class);
                this.startActivityForResult(intent, 0);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<WayPoint> wayPoints = googleMapsDirection.getWayPoints(new LatLng(data.getDoubleExtra("latStartLocation", 0.0), data.getDoubleExtra("longStartLocation", 0.0)),
                new LatLng(data.getDoubleExtra("latEndLocation", 0.0), data.getDoubleExtra("longEndLocation", 0.0)));

        wayPoints = weather.addWeatherInformations(wayPoints);
        googleMaps.showWayPoints(wayPoints);
    }
}
