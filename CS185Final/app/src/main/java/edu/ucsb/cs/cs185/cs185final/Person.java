package edu.ucsb.cs.cs185.cs185final;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a player in some game
 */
public class Person {
    public final String name;
    public final Uri icon;
    public final LatLng location;

    Person(LatLng location) {
        this("Mark Nguyen", Uri.parse("http://www.nguyenmp.com/my_face_2.jpg"), location);
    }

    private Person(String name, Uri icon, LatLng location) {
        this.name = name;
        this.icon = icon;
        this.location = location;

    }
}
