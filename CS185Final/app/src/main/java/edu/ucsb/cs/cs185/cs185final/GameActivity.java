package edu.ucsb.cs.cs185.cs185final;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ucsb.cs.cs185.cs185final.models.Game;
import edu.ucsb.cs.cs185.cs185final.models.Player;

public class GameActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Game game;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        game = (Game) i.getExtras().getSerializable("game");
        setUpMapIfNeeded();
        final Vibrator vib= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialogBuilder.setMessage("Nick is close!!!")
                .setCancelable(false)
                .setPositiveButton("KILL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vib.cancel();
                        finish();
                        Intent videoGame = new Intent(getApplicationContext(), GameEnd.class);
                        startActivity(videoGame);
                    }
                });
        final AlertDialog alert = alertDialogBuilder.create();
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                alert.show();
                vib.vibrate(5000);}
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(34.413593, -119.841195));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        mMap.addMarker(new MarkerOptions().position(new LatLng(34.413593, -119.841195)).title("Nick"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(34.413250, -119.841692)).title("Alex"));

        if(!game.title.equals("new")){
            Player player;
            for(int i=0;i<game.players.size();i++){
                player = game.players.get(i);
                mMap.addMarker(new MarkerOptions().position(new LatLng(player.longitude, player.latitude)).title(player.name));
            }
        }
    }
}
