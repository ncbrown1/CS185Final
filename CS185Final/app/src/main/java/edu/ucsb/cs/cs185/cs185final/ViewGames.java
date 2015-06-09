package edu.ucsb.cs.cs185.cs185final;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import java.util.List;

import edu.ucsb.cs.cs185.cs185final.models.Data;
import edu.ucsb.cs.cs185.cs185final.models.Game;


public class ViewGames extends Activity {

    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games);
        readPlayersInfo();
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

        @Override
        public void onBindViewHolder(final GameViewHolder holder, int i) {
            Game game = games.get(i);
////        holder.toolbar.setTitleTextAppearance(context,);
//            holder.toolbar.setTitle(item.getTitle());
//            holder.toolbar.setSubtitle(item.getDate());
////        holder.toolbar.setSubtitle(item.getTitle());
//            holder.toolbar.getMenu().clear();
//            holder.toolbar.inflateMenu(newsOrEvents == 1 ? R.menu.menu_news : R.menu.menu_events);
//            final String title = item.getTitle();
//            final String weburl = item.getLink();
//
//            holder.description.setText(item.getDescription());
//            holder.cv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(newsOrEvents == 1)
//                        startWebView(title,weburl);
//                    else
//                        holder.toolbar.showOverflowMenu();
//                }
//            });
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class GameViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            Toolbar toolbar;
            TextView description;

            GameViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cardview);
                toolbar = (Toolbar) itemView.findViewById(R.id.title);
                description = (TextView) itemView.findViewById(R.id.description);
            }
        }
    }
}
