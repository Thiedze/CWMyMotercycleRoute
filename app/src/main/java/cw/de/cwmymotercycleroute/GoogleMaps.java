package cw.de.cwmymotercycleroute;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by sebastian on 01.08.17.
 *
 * Class to display google maps functionality.
 */

class GoogleMaps {

    private GoogleMap maps;

    private AppCompatActivity mainActivity;

    GoogleMaps(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setMaps(GoogleMap maps) {
        this.maps = maps;
    }

    void showWayPoints(List<WayPoint> wayPoints) {
        for (int index = 0; index < wayPoints.size(); index++) {
            maps.addMarker(new MarkerOptions().position(new LatLng(wayPoints.get(index).getLatitude(),
                    wayPoints.get(index).getLongitude())).title(String.valueOf(index)).icon(BitmapDescriptorFactory.fromPath("weatherkey_" + wayPoints.get(index).getWeatherKey() + "-png")));
        }
    }

    void showCurrentPosition() {
        try {
            maps.setMyLocationEnabled(true);
            LocationManager service = (LocationManager) mainActivity.getSystemService(AppCompatActivity.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            if(location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                maps.addMarker(new MarkerOptions().position(userLocation).title("@string/current_position"));
                maps.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                maps.setMinZoomPreference(6.0f);
                maps.setMaxZoomPreference(14.0f);
            }
        } catch (SecurityException exception) {
            Toast.makeText(mainActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
