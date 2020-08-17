package sg.edu.rp.webservices.c302_p12_problemstatement;

import java.io.Serializable;

public class Incident implements Serializable {
    private String type, message;
    private double latitude, longitude;

    public Incident(String type, double latitude, double longitude, String message) {
        this.type = type;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
