package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import static jett_apps.grouvie.LandingPage.CINEMA_MESSAGE;
import static jett_apps.grouvie.LandingPage.DAY_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.LATITUDE;
import static jett_apps.grouvie.LandingPage.LONGITUDE;
import static jett_apps.grouvie.LandingPage.SHOWTIME_MESSAGE;
import static jett_apps.grouvie.LandingPage.USER_NAME;

public class LeaderInitialPlan extends AppCompatActivity {

    double latitude, longitude;
    String chosenFilm, chosenCinema, chosenTime, chosenDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(LATITUDE, 0);
        longitude = intent.getDoubleExtra(LONGITUDE, 0);
        chosenFilm = intent.getStringExtra(FILM_MESSAGE);
        chosenCinema = intent.getStringExtra(CINEMA_MESSAGE);
        chosenTime = intent.getStringExtra(SHOWTIME_MESSAGE);
        chosenDay = intent.getStringExtra(DAY_MESSAGE);

        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDay);

    }

    public void sendToGroup(View view) throws IOException {
        //TODO: Send initial/draft plan to web server to update the database
        //TODO: Send current plan to rest of the group

        JSONObject json = new JSONObject();
        try {
            json.accumulate("PHONE_NUMBER", "1");
            json.accumulate("GROUP_ID", 0);
            json.accumulate("SHOWTIME", chosenTime);
            json.accumulate("FILM", chosenFilm);
            json.accumulate("PRICE", 32.22);
            json.accumulate("LOCATION_LAT", latitude);
            json.accumulate("LOCATION_LONG", longitude);
            json.accumulate("IMAGE", "HTTP");
            json.accumulate("IS_LEADER", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ServerContact().execute("insert", json.toString());

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        String user_name = getIntent().getStringExtra(USER_NAME);
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra(USER_NAME, user_name);
        startActivity(intent);
    }
}

