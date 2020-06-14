package com.techasylum.retrofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity {

    private LoginButton  loginButton;
    Button Logbtn;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    String token="";
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        Logbtn=findViewById(R.id.log);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();





        accessToken=AccessToken.getCurrentAccessToken();
        if( accessToken != null )
        {
            Toast.makeText(FacebookActivity.this, "already Login ", Toast.LENGTH_SHORT).show();
            token=AccessToken.getCurrentAccessToken().getToken();
            Intent intent=new  Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
            finish();
        }

          //Callback
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
      loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                    Toast.makeText(FacebookActivity.this, "Login Suceesfully"+EMAIL, Toast.LENGTH_SHORT).show();
                     token=AccessToken.getCurrentAccessToken().getToken();
                   Intent intent=new  Intent(getApplicationContext(),MainActivity.class);
                   intent.putExtra("token",token);
                   startActivity(intent);
                    finish();



                // App code
            }

            @Override
            public void onCancel() {

                Toast.makeText(FacebookActivity.this, "cancel", Toast.LENGTH_SHORT).show();


                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(FacebookActivity.this, "Login error "+exception.getMessage(), Toast.LENGTH_SHORT).show();

                // App code
            }
        });

        Logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Post Data
                LoginManager.getInstance().logInWithPublishPermissions(
                        FacebookActivity.this,
                        Arrays.asList("publish_actions"));

                //get Data
                LoginManager.getInstance().logInWithReadPermissions(FacebookActivity.this, Arrays.asList(EMAIL,"public_profile","user_posts"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                Toast.makeText(FacebookActivity.this, "Login Suceesfully"+EMAIL, Toast.LENGTH_SHORT).show();
                                token=AccessToken.getCurrentAccessToken().getToken();
                                Intent intent=new  Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("token",token);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Toast.makeText(FacebookActivity.this, "cancel", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Toast.makeText(FacebookActivity.this, "Login error "+exception.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


              token=AccessToken.getCurrentAccessToken().getToken();
        Toast.makeText(FacebookActivity.this, "Token"+token, Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if( accessToken != null )
        {
            Toast.makeText(FacebookActivity.this, "already Login ", Toast.LENGTH_SHORT).show();
            token=AccessToken.getCurrentAccessToken().getToken();
            Intent intent=new  Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if( accessToken != null )
        {
            Toast.makeText(FacebookActivity.this, "already Login ", Toast.LENGTH_SHORT).show();
            token=AccessToken.getCurrentAccessToken().getToken();
            Intent intent=new  Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
            finish();
        }
    }
}
