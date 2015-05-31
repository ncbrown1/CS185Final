package edu.ucsb.cs.cs185.cs185final;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Given a person and a map, we will download their face and show it on the map.
 */
public class PersonMarkerLoader extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<Context> contextRef;
    private final WeakReference<GoogleMap> mapRef;
    private final Person person;

    /**
     * Given a person and a map, we will download their face and show it on the map.
     */
    public PersonMarkerLoader(Context context, Person person, GoogleMap map) {
        super();
        this.contextRef = new WeakReference<>(context);
        this.person = person;
        this.mapRef = new WeakReference<>(map);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Context context = contextRef.get();
        if (context == null) return null;

        try {
            return Picasso.with(context).load(person.icon).resize(200, 200).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap == null) return;

        GoogleMap googleMap = mapRef.get();
        if (googleMap == null) return;

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        googleMap.addMarker(new MarkerOptions()
                .icon(icon)
                .title(person.name)
                .position(person.location));
    }
}
