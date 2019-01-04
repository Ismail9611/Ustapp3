package org.ginlevel.ustapp3.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.ginlevel.ustapp3.activity.MainActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfFirstStart();
    }

    private void checkIfFirstStart() {
        String APP_PREF_FILE = "app_settings";
        Intent intent;
        boolean firstStart = getSharedPreferences(APP_PREF_FILE, MODE_PRIVATE).getBoolean("was_started", false);
        if (firstStart) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, TargetActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
