package org.ginlevel.ustapp3.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.model.User;

public class UserAccountActivity extends AppCompatActivity {

    private ImageView userMainImage;
    private TextView userFullName, userCategory, userPhoneNumber, userEmail, userDescription;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        fieldsInit();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        User user = (User) getIntent().getSerializableExtra("userAccount");
        if (user != null){
            userFullName.setText(user.getFullName());
            userCategory.setText(user.getCategoryIn());
            userPhoneNumber.setText(user.getPhoneNumber());
            userEmail.setText(user.getEmail());
            userDescription.setText(user.getJobDescription());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void fieldsInit(){
        userMainImage = findViewById(R.id.userAccountImage);
        userFullName = findViewById(R.id.userAccountFullName);
        userCategory = findViewById(R.id.userAccountCategory);
        userPhoneNumber = findViewById(R.id.userAccountPhoneNumber);
        userEmail = findViewById(R.id.userAccountEmail);
        userDescription = findViewById(R.id.userAccountDescription);
        toolbar = findViewById(R.id.userAccountToolbar);
    }
}
