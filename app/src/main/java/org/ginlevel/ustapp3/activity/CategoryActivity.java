package org.ginlevel.ustapp3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.storing.FirebaseDB;
import org.ginlevel.ustapp3.utils.AdditionalFunc;

public class CategoryActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CATEGORY_LOG";

    private ListView categoryListView;
    private ConstraintLayout checkConnectionLayout;
    private ProgressBar progressLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        checkConnectionLayout = findViewById(R.id.checkConnectionCategory);
        categoryListView = findViewById(R.id.categoryListView);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.tvCategoryName);
                String categoryName = tv.getText().toString();
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                intent.putExtra("category_name", categoryName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        progressLoading = findViewById(R.id.categoryLoadProgressBar);

        loadAndShowCategories();

    }




    private void loadAndShowCategories() {
        boolean connected = AdditionalFunc.isInternetConnected(this);
        if (connected) {
            FirebaseDB firebaseDB = new FirebaseDB();
            checkConnectionLayout.setVisibility(View.GONE);
            firebaseDB.loadAllCategories(this, categoryListView, progressLoading);
            categoryListView.setVisibility(View.VISIBLE);
        } else {
            categoryListView.setVisibility(View.GONE);
            progressLoading.setVisibility(View.GONE);
            checkConnectionLayout.setVisibility(View.VISIBLE);
        }

    }





}
