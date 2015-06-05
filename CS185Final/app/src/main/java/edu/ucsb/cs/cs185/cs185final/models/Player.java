package edu.ucsb.cs.cs185.cs185final.models;

public class Player {
    public int id;
    public String name;
    public double longitude;
    public double latitude;
    public int lives;
    public int game;

    public Player(Player player) {
        this.id = player.id;
        this.name = player.name;
        this.longitude = player.longitude;
        this.latitude = player.latitude;
        this.lives = player.lives;
    }

    public Player() {

    }
}