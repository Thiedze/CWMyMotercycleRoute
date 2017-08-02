package cw.de.cwmymotercycleroute;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sebastian on 02.08.17.
 *
 * Class to get the weather informations.
 */

class Weather {

    private AppCompatActivity mainActivity;

    Weather(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    List<WayPoint> addWeatherInformations(List<WayPoint> wayPoints) {
        List<LatLng> minMaxLatLng = getMinMaxLatLng(wayPoints);

        for (int index = 0; index < wayPoints.size(); index++) {

        }

        return wayPoints;
    }

    private List<LatLng> getMinMaxLatLng(List<WayPoint> wayPoints) {
        Double minLat = 180.0;
        Double maxLat = -180.0;

        Double minLng = 90.0;
        Double maxLng = -90.0;


        for(WayPoint wayPoint : wayPoints) {
            if(wayPoint.getLatitude() < minLat) {
                minLat = wayPoint.getLatitude();
            }

            if(wayPoint.getLongitude() < minLng) {
                minLng = wayPoint.getLongitude();
            }

            if(wayPoint.getLatitude() > maxLat) {
                maxLat = wayPoint.getLatitude();
            }

            if(wayPoint.getLongitude() > maxLng) {
                maxLng = wayPoint.getLongitude();
            }
        }

        List<LatLng> minMaxLatLng = new ArrayList<>();
        minMaxLatLng.add(new LatLng(minLat, minLng));
        minMaxLatLng.add(new LatLng(maxLat, maxLng));
        return minMaxLatLng;
    }

    private String getResponse(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();

        } catch (Exception exception) {
            Toast.makeText(mainActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        return stringBuilder.toString();
    }

}
