package org.ginlevel.ustapp3.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.adapters.UsersRecyclerViewAdapter;
import org.ginlevel.ustapp3.model.User;
import org.ginlevel.ustapp3.storing.FirebaseDB;
import org.ginlevel.ustapp3.utils.AdditionalFunc;
import org.ginlevel.ustapp3.utils.DrawerHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MAIN_ACTIVITY_LOG";
    private static final String APP_PREF_FILE = "app_settings";
    private static final int CATEGORY_SELECTED = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseDB firebaseDB;
    private SharedPreferences preferences;

    private Toolbar toolbar;
    private DrawerHandler drawerHandler;
    private ImageView imgCategory;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ConstraintLayout checkConnectionLayout;
    private ProgressBar usersLoadingProgress;
    private Button buttonRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fieldsInit();

        setSupportActionBar(toolbar);
        register();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = new FirebaseDB();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInetAndInitMainScreen();
                refreshLayout.setRefreshing(false);
            }
        });

        imgCategory = findViewById(R.id.ivCategory);
        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivityForResult(intent, CATEGORY_SELECTED);
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInetAndInitMainScreen();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_SELECTED && resultCode == RESULT_OK){
            String categoryName = data.getStringExtra("category_name");

            checkInetAndShowUsersByCategory(categoryName);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNavigationDrawer();
        checkInetAndInitMainScreen();
    }

    private void checkInetAndShowUsersByCategory(String category){
        boolean connected = AdditionalFunc.isInternetConnected(this);
        if (connected){
            FirebaseDB firebaseDB = new FirebaseDB();
            checkConnectionLayout.setVisibility(View.GONE);
            firebaseDB.findUsersByCategory(category, this, recyclerView, usersLoadingProgress);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            onInetConnectionAbsent();
        }
    }

    private void checkInetAndInitMainScreen(){
        boolean connected = AdditionalFunc.isInternetConnected(this);
        if (connected){
            FirebaseDB firebaseDB = new FirebaseDB();
            checkConnectionLayout.setVisibility(View.GONE);
            firebaseDB.loadAllUsers(this, recyclerView, usersLoadingProgress);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            onInetConnectionAbsent();
        }
    }



    private void initNavigationDrawer() {
        if (firebaseAuth.getCurrentUser() != null){
            String userName = firebaseAuth.getCurrentUser().getDisplayName();
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String userPhoneNum = firebaseAuth.getCurrentUser().getPhoneNumber();
            User user = new User(userName, userEmail, userPhoneNum);
            drawerHandler = new DrawerHandler(this, user);
        } else {
            drawerHandler = new DrawerHandler(this, null);
        }
        drawerHandler.drawerBuilder(toolbar);
    }

    private void onInetConnectionAbsent(){
        recyclerView.setVisibility(View.GONE);
        usersLoadingProgress.setVisibility(View.GONE);
        checkConnectionLayout.setVisibility(View.VISIBLE);
    }

    private void fieldsInit() {
        toolbar =  findViewById(R.id.toolbarMainActivity);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        checkConnectionLayout = findViewById(R.id.checkConnection);
        checkConnectionLayout.setVisibility(View.GONE);
        usersLoadingProgress = findViewById(R.id.usersLoadingProgress);
        buttonRefresh = findViewById(R.id.btnCheckInetRefresh);
    }

    /**
     * Метод используется для того чтобы {@link TargetActivity}
     * показывалась только один раз при первом запуске
     */
    private void register(){
        preferences = getSharedPreferences(APP_PREF_FILE , MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("was_started", true);
        editor.apply();
    }
}
