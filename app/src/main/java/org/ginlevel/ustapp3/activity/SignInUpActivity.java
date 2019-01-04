package org.ginlevel.ustapp3.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.adapters.ViewPagerAdapter;
import org.ginlevel.ustapp3.fragments.LogoutFragment;
import org.ginlevel.ustapp3.fragments.SignInFragment;
import org.ginlevel.ustapp3.fragments.SignUpFragment;

public class SignInUpActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbarSingInUp);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPageInit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void viewPageInit() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (firebaseAuth.getCurrentUser() != null){
            adapter.addFragment(new LogoutFragment(), "Logout");
        } else {
            adapter.addFragment(new SignInFragment(), "Sign in");
        }
        adapter.addFragment(new SignUpFragment(), "Sing up");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
