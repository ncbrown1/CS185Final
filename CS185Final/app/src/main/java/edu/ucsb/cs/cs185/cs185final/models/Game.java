package edu.ucsb.cs.cs185.cs185final.models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String title;
    private int max_participants;
    private int max_score;
    private int num_lives;
    private double critical_distance;
    private boolean is_private;
    private List<Player> players;

    public Game(String title, int max_participants, int max_score, int num_lives, double critical_distance, boolean is_private) {
        this.title = title;
        this.max_participants = max_participants;
        this.max_score = max_score;
        this.num_lives = num_lives;
        this.critical_distance = critical_distance;
        this.is_private = is_private;

        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() { return players; }
    public String getTitle() { return title; }
    public int getMax_participants() { return max_participants; }
    public int getMax_score() { return max_score; }
    public int getNum_lives() { return num_lives; }
    public double getCritical_distance() { return critical_distance; }
    public boolean is_private() { return is_private; }
}
