package org.ginlevel.ustapp3.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.model.User;
import org.ginlevel.ustapp3.storing.FirebaseDB;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private ImageView accMainPhoto, accEdit, accAddPhoto;
    private TextView accFullName, accCategory, accDescription, accPhoneNumber, accEmail;
    private LinearLayout accImagesLayout;
    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = findViewById(R.id.toolbarAccountActivity);
        initViews();
        initAccount();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        accEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, AccountEditActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initAccount(){
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            FirebaseDB firebaseDB = new FirebaseDB();
            Map<String, View> views = new HashMap<>();
            views.put("mainPhoto", accMainPhoto);
//            views.put("editImage", accEdit);
//            views.put("addPhotoImage", accAddPhoto);
            views.put("fullName", accFullName);
            views.put("category", accCategory);
            views.put("description", accDescription);
            views.put("phoneNumber", accPhoneNumber);
            views.put("email", accEmail);
            views.put("imagesLayout", accImagesLayout);
            firebaseDB.findUserByIdAndSetup(firebaseAuth.getCurrentUser().getUid(), views);
        }
    }


    private void initViews(){
        accMainPhoto = findViewById(R.id.accountImage);
        accEdit = findViewById(R.id.accountEditImage);
        accAddPhoto = findViewById(R.id.account_image_add);
        accFullName = findViewById(R.id.accountFullName);
        accCategory = findViewById(R.id.accountCategory);
        accDescription = findViewById(R.id.accountDescription);
        accPhoneNumber = findViewById(R.id.accountPhoneNumber);
        accEmail = findViewById(R.id.accountEmail);
        accImagesLayout = findViewById(R.id.account_images_layout);
    }
}
