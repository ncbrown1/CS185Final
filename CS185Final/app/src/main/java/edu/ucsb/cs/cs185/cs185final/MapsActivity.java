package edu.ucsb.cs.cs185.cs185final;

import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ucsb.cs.cs185.cs185final.models.Data;
import edu.ucsb.cs.cs185.cs185final.models.Game;
import edu.ucsb.cs.cs185.cs185final.models.Player;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ViewPager gamesPager;
    private final Map<String, Integer> map = new HashMap<>();
    private final ArrayList<Marker> markers = new ArrayList<>();

    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        readPlayersInfo();
        gamesPager = (ViewPager) findViewById(R.id.teams_pager);
        gamesPager.setAdapter(new TeamsAdapter(getSupportFragmentManager(), data.games.toArray(new Game[data.games.size()])));
        gamesPager.addOnPageChangeListener(new PagerListener(this));
        setSelectedTeam(1);

        // https://stackoverflow.com/questions/13914609/viewpager-with-previous-and-next-page-boundaries
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28*2, getResources().getDisplayMetrics());
        gamesPager.setPageMargin(-margin);
        gamesPager.setOffscreenPageLimit(10);
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
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
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String locationProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        LatLng latlng;
        if(location != null){
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        else latlng = new LatLng(0,0);

        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clicked) {
                int team = map.get(clicked.getId());

                setSelectedTeam(team);

                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setSelectedTeam(null);
            }
        });
    }

    public void setTransitiveSelection(int team, float offset) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            if (map.get(marker.getId()).equals(team)) {
                marker.setAlpha(1f - ((1f - 0.2f) * offset));
                LatLng position = marker.getPosition();
                builder.include(position);
            }
            else if (offset < 0 && map.get(marker.getId()).equals(team - 1)) {
                marker.setAlpha(0.2f + ((1f - 0.2f) * offset));
            }
            else if (offset > 0 && map.get(marker.getId()).equals(team + 1)) {
                marker.setAlpha(0.2f + ((1f - 0.2f) * offset));
            } else {
                marker.setAlpha(0.2f);
            }
        }

        if (offset == 0.0f) {
            setSelectedTeam(team);
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 64);
            try {mMap.animateCamera(cameraUpdate);}
            catch (IllegalStateException ignored) {}
        }
    }

    private void setSelectedTeam(@Nullable Integer team) {
        if (team != null) {
            gamesPager.setVisibility(View.VISIBLE);
            gamesPager.setCurrentItem(team - 1);
        } else {
            gamesPager.setVisibility(View.GONE);
        }
    }

    private void readPlayersInfo(){

        try {
            AssetManager am = this.getAssets();
            InputStream is = am.open("PlayersList.txt");
            Reader reader = new InputStreamReader(is);
            data = new Gson().fromJson(reader, Data.class);
            for (Player player : data.players) {
                addPlayerToMap(player.longitude, player.latitude, player.game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPlayerToMap(double lat, double lon, int teamIndex){
        BitmapDescriptor markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

        switch (teamIndex){
            case 1:
                markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
            case 2:
                markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            case 3:
                markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                break;
            case 4:
                markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            case 5:
                markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                break;

        }
        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(markerColor)
                .position(new LatLng(lat, lon)));

        marker.setAlpha(0.2f);
        String id = marker.getId();
        map.put(id, teamIndex);
        markers.add(marker);
    }

    private static class TeamsAdapter extends FragmentPagerAdapter {
        private final Game[] games;

        private TeamsAdapter(FragmentManager fm, Game[] games) {
            super(fm);
            this.games = games;
        }

        @Override
        public int getCount() {
            return games.length;
        }

        @Override
        public Fragment getItem(int position) {
            return TeamFragment.newInstance(games[position]);
        }
    }

    public static class TeamFragment extends Fragment {
        private static final String ARGUMENT_GAME = "asdflkj";

        public static TeamFragment newInstance(@NonNull Game game) {
            Bundle args = new Bundle();
            args.putSerializable(ARGUMENT_GAME, game);
            TeamFragment f = new TeamFragment();
            f.setArguments(args);
            return f;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Game game = (Game) getArguments().getSerializable(ARGUMENT_GAME);
            View view = inflater.inflate(R.layout.team_detail_page, container, false);

            TextView name = (TextView) view.findViewById(R.id.game_name);
            name.setText(game.title);
            return view;
        }
    }

    private static class PagerListener implements ViewPager.OnPageChangeListener {
        public final WeakReference<MapsActivity> activityRef;
        private int position = 0;

        public PagerListener(MapsActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            MapsActivity mapsActivity = activityRef.get();
            if (mapsActivity == null) return;

            System.out.printf("Scrolling %d %f\n", position, positionOffset);

            mapsActivity.setTransitiveSelection(position + 1, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
//            this.position = position;
//            System.out.printf("Selected %d\n", position);
//            MapsActivity mapsActivity = activityRef.get();
//            if (mapsActivity != null) mapsActivity.setSelectedTeam(position + 1);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
