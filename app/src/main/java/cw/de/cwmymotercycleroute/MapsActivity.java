package cw.de.cwmymotercycleroute;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap maps;

    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void showCurrentPosition() {
        try {
            maps.setMyLocationEnabled(true);
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            maps.addMarker(new MarkerOptions().position(userLocation).title("@string/current_position"));
            maps.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

            maps.setMinZoomPreference(6.0f);
            maps.setMaxZoomPreference(14.0f);
        } catch (SecurityException exception) {
            Toast.makeText(this, exception.getCause().toString(), Toast.LENGTH_LONG);
        } catch (Exception exception) {
            Toast.makeText(this, exception.getCause().toString(), Toast.LENGTH_LONG);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            showCurrentPosition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCurrentPosition();
                }
                return;
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
        try {
            switch (item.getItemId()) {
                case R.id.add_route:
                    Intent intent = new Intent(this, SetLocationActivity.class);
                    this.startActivity(intent);
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception exception) {
            Toast.makeText(this, exception.getCause().toString(), Toast.LENGTH_LONG);
        }
        return true;

    }
}
