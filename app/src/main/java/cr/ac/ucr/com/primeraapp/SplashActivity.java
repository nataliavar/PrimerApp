package cr.ac.ucr.com.primeraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import cr.ac.ucr.com.primeraapp.Utils.AppPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isLoggedIn = AppPreferences.getInstance(this).getBoolean(AppPreferences.Keys.IS_LOGGED_IN, false);

        Intent intent;

       if(isLoggedIn){
           intent = new Intent(this, MainActivity.class);
       }else{
           intent = new Intent(this, LoginActivity.class);
       }

       startActivity(intent);
        /* se utiliza si no se quiere volver a  este activity */
       finish();
    }
}