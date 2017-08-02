package cw.de.cwmymotercycleroute;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by sebastian on 01.08.17.
 *
 * Class to represent a waypoint.
 */

class WayPoint {

    private double longitude;

    private double latitude;

    private Date date;

    private Integer weatherKey;

    public Integer getWeatherKey() {
        return weatherKey;
    }

    public void setWeatherKey(Integer weatherKey) {
        this.weatherKey = weatherKey;
    }

    WayPoint(double latitude, double longitude, Date date) {
        setLatitude(latitude);
        setLongitude(longitude);
        setDate(date);
    }

    WayPoint(LatLng latLng, Date date) {
        setLatitude(latLng.latitude);
        setLongitude(latLng.longitude);
        setDate(date);
    }

    public Date getDate() {
        return date;
    }

    private void setDate(Date date) {
        this.date = date;
    }

    double getLongitude() {
        return longitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double getLatitude() {
        return latitude;
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
