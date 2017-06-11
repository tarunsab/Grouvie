package jett_apps.grouvie;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static jett_apps.grouvie.LandingPage.USER_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signin;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    private Button signout;
    private TextView message;
    private Button carryOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signout = (Button) findViewById(R.id.bn_signout);
        signout.setOnClickListener(this);
        carryOn = (Button) findViewById(R.id.carry_on);
        carryOn.setOnClickListener(this);

        message = (TextView) findViewById(R.id.succes_text);
        message.setVisibility(View.GONE);

        signin = (SignInButton) findViewById(R.id.bn_login);
        // Tapping the buttons will trigger events.
        signin.setOnClickListener(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

    }

    @Override
    public void onClick(View v) {
        // Sign in procedure.
        switch(v.getId()) {
            case R.id.bn_login:
                signIn();
                break;
            case R.id.bn_signout:
                signOut();
                break;
            case R.id.carry_on:
                startActivity(new Intent(this, LandingPage.class));
                break;
        }
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                Intent intent = new Intent(this, LandingPage.class);
                intent.putExtra(USER_NAME, account.getDisplayName());
                startActivity(intent);
            } else {
                message.setText("LOGIN FAILED");
                message.setVisibility(View.VISIBLE);
            }
        }
    }
}


//    private ConstraintLayout profileSection;
//    private Button signout;
//    private TextView name;
//    private TextView email;
//    private ImageView image;


//        Encapsulates the next four items into one section.
//        profileSection = (ConstraintLayout) findViewById(R.id.profileSection);
//        signout = (Button) findViewById(R.id.button_logout);
//        name = (TextView) findViewById(R.id.input_name);
//        email = (TextView) findViewById(R.id.input_email);
//        image = (ImageView) findViewById(R.id.profileImage);

//        signout.setOnClickListener(this);
// Hides info until login complete.
//        profileSection.setVisibility(View.GONE);



//
//    private void handleResult(GoogleSignInResult result) {
//        if(result.isSuccess()) {
//            GoogleSignInAccount account = result.getSignInAccount();
//            String a_name = account.getDisplayName();
//            String a_email = account.getEmail();
//            android.net.Uri img_url = account.getPhotoUrl();
//
//            name.setText(a_name);
//            email.setText(a_email);
//            if(img_url != null) {
//                Glide.with(this).load(img_url).into(image);
//            }
//            updateUI(true);
//        } else {
//            updateUI(false);
//        }
//
//    }


