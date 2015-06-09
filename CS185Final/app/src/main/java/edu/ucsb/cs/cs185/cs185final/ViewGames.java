package edu.ucsb.cs.cs185.cs185final;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.List;

import edu.ucsb.cs.cs185.cs185final.models.Data;
import edu.ucsb.cs.cs185.cs185final.models.Game;


public class ViewGames extends Activity {

    private Data data;
    private RecyclerView rview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games);

        rview = (RecyclerView)findViewById(R.id.game_list);
        rview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rview.setLayoutManager(llm);

        readPlayersInfo();

        GameRecyclerAdapter gra = new GameRecyclerAdapter(data.games);
        rview.setAdapter(gra);
    }

    private void readPlayersInfo(){
        try {
            AssetManager am = this.getAssets();
            InputStream is = am.open("PlayersList.txt");
            Reader reader = new InputStreamReader(is);
            data = new Gson().fromJson(reader, Data.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GameRecyclerAdapter extends RecyclerView.Adapter<GameRecyclerAdapter.GameViewHolder> {

        private List<Game> games;

        public GameRecyclerAdapter(List<Game> games) {
            this.games = games;
        }

        @Override
        public int getItemCount() {
            return games.size();
        }

        @Override
        public GameViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card,parent,false);
            return new GameViewHolder(v);
        }

        private String timeLeft(long time) {
            Calendar now = Calendar.getInstance();
            long nowmillis = now.getTimeInMillis();

            long different = time - nowmillis;

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            return elapsedHours + " Hours, " + elapsedMinutes + "Minutes";
        }

        @Override
        public void onBindViewHolder(final GameViewHolder holder, int i) {
            Game game = games.get(i);
            holder.toolbar.setTitle(game.title);
            holder.toolbar.setSubtitle(timeLeft(game.end_time) + " Remaining");
            holder.num_players.setText(game.players.size() + " / " + game.max_participants);
            holder.num_lives.setText(game.num_lives);
            holder.max_score.setText(game.max_score);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class GameViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            Toolbar toolbar;
            TextView num_players;
            TextView num_lives;
            TextView max_score;

            GameViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cardview);
                toolbar = (Toolbar) itemView.findViewById(R.id.title);
                num_players = (TextView) itemView.findViewById(R.id.num_players);
                num_lives = (TextView) itemView.findViewById(R.id.num_lives);
                max_score = (TextView) itemView.findViewById(R.id.max_score);
            }
        }
    }
}
