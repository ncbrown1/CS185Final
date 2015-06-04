package edu.ucsb.cs.cs185.cs185final.models;

/**
 * Created by ncbrown on 6/3/15.
 */
public class Player {
    private static int id_counter = 0;
    private int id;
    private String name;
    private double longitude;
    private double latitude;

    private int lives;

    public Player(String name, int lives) {
        // default location is UCSB campus
        this(name, 34.413968, -119.848943, lives);
    }

    public Player(String name, double lng, double lat, int lives) {
        this.id = id_counter;
        id_counter++;

        this.name = name;
        this.longitude = lng;
        this.latitude = lat;
        this.lives = lives;
    }

    public int die() {
        if(lives <= 0) return lives;
        else return lives--;
    }
    public void moveTo(double lng, double lat) {
        longitude = lng;
        latitude = lat;
    }


    public void setName(String name) { this.name = name; }
    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public double getLongitude() { return this.longitude; }
    public double getLatitude() { return this.latitude; }
}
