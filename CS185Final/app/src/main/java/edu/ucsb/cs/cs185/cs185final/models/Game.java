package edu.ucsb.cs.cs185.cs185final.models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public String title;
    public int max_participants;
    public int max_score;
    public int num_lives;
    public double critical_distance;
    public boolean is_private;
    public ArrayList<Player> players;

    public Game(Game game) {
        this.title = game.title;
        this.max_participants = game.max_participants;
        this.max_score = game.max_score;
        this.num_lives = game.num_lives;
        this.critical_distance = game.critical_distance;
        this.is_private = game.is_private;

        this.players = new ArrayList<Player>();
        for (Player player : game.players) {
            this.players.add(new Player(player));
        }
    }

    public Game() {

    }
}