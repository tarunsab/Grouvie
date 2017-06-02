package jett_apps.grouvie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class SelectFilm extends AppCompatActivity implements LocationListener {

    public static final String FILM_MESSAGE = "FILMTITLE";
    public static final String CINEMA_MESSAGE= "CINEMATITLE";
    public static final String SHOWTIME_MESSAGE = "SHOWTIME";

    Location location;
    double latitude = 51.499074;
    double longitude = -0.177070;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_film);

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 8);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 8);
        }



        location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
        } else {

            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                onLocationChanged(location);
            }

        }

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://" + ServerContact.WebServerIP + ":5000/get_films");
        JSONObject json = new JSONObject();
        try {
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json_str = json.toString();
        StringEntity se = null;
        try {
            se = new StringEntity(json_str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(se);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        InputStream is = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            is = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result;
        if(is != null) {
            result = convertStreamToString(is);
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
        } else {
            result = "Did not work!";
        }
        String[] films = result.split(",");
        Log.v("DANK MEMES", Arrays.toString(films));


        final String[] showingFilmsArray = result.split(",");
//                {"Guardians of the Galaxy Vol 2",
//                "The Fate of the Furious",
//                "Boss Baby",
//                "WonderWoman",
//                "Baywatch",
//                "Alien: Covenant",
//                "Beauty and the Beast",
//                "Lion",
//                "Pirates of the Caribbean"};
        final String allocatedCinema = "Vue Westfield Stratford";

        ListAdapter filmAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, showingFilmsArray);
        ListView filmsListView = (ListView) findViewById(R.id.filmList);
        filmsListView.setAdapter(filmAdapter);


        filmsListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filmTitle = showingFilmsArray[position];
                    Intent intent = new Intent(view.getContext(), SelectShowtime.class);
                    intent.putExtra(FILM_MESSAGE, filmTitle);
                    intent.putExtra(CINEMA_MESSAGE, allocatedCinema);
                    startActivity(intent);
                }
            }
        );

    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //Print out location message for debugging purposes
//        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
