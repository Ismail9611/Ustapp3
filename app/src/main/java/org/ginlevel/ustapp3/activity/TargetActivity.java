package org.ginlevel.ustapp3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.ginlevel.ustapp3.R;

public class TargetActivity extends AppCompatActivity  {

    private Button btnReg, btnStartScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        btnReg = findViewById(R.id.btnTargetReg);
        btnStartScreen = findViewById(R.id.btnTargetStartScreen);

        btnStartScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TargetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TargetActivity.this, SignInUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }





}
