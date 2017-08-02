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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sebastian on 01.08.17.
 * <p>
 * Class to communicate with google directions.
 */

class GoogleMapsDirection {

    private AppCompatActivity mainActivity;

    GoogleMapsDirection(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    List<WayPoint> getWayPoints(LatLng startLocation, LatLng endLocation) {
        List<WayPoint> wayPoints = new ArrayList<>();
        try {
            if (startLocation != null && endLocation != null) {
                wayPoints.clear();

                wayPoints.add(new WayPoint(startLocation, new Date()));
                wayPoints.addAll(getWayPointsFromResponse(getGoogleDirectionInformation(startLocation, endLocation), wayPoints.get(wayPoints.size() - 1)));
                wayPoints.add(new WayPoint(endLocation, null));
            }
        } catch (Exception exception) {
            Toast.makeText(mainActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        return wayPoints;
    }

    private Date getWayPointDate(Date currentDate, Integer minutes) {
        Date newDate = null;
        try {
            long time = currentDate.getTime();
            newDate = new Date(time + (minutes * 60000));
        } catch (Exception exception) {
            Toast.makeText(mainActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        return newDate;
    }

    private List<WayPoint> getWayPointsFromResponse(String json, WayPoint startWayPoint) {
        List<WayPoint> wayPoints = new ArrayList<>();
        try {
            JSONArray steps = new JSONObject(json).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            Date lastDate = startWayPoint.getDate();
            for (int index = 0; index < steps.length(); index++) {
                JSONObject step = steps.getJSONObject(index);
                Date date = getWayPointDate(lastDate, step.getJSONObject("duration").getInt("value"));
                WayPoint wayPoint = new WayPoint(step.getJSONObject("start_location").getDouble("lat"), step.getJSONObject("start_location").getDouble("lng"), date);
                lastDate = wayPoint.getDate();
                wayPoints.add(wayPoint);
            }
        } catch (Exception exception) {
            Toast.makeText(mainActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        return wayPoints;
    }

    private String getGoogleDirectionInformation(LatLng startLocation, LatLng endLocation) {
        String response = "";
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + startLocation.latitude + "," + startLocation.longitude +
                    "&destination=" + endLocation.latitude + "," + endLocation.longitude + "&avoid=highways&key=" + mainActivity.getResources().getString(R.string.google_maps_key));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();

            response = stringBuilder.toString();
        } catch (Exception exception) {
            Toast.makeText(mainActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        return response;
    }

}
