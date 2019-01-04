package org.ginlevel.ustapp3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.fragments.SignUpFragment;

public class PaymentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        findViewById(R.id.buttonPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, SignUpFragment.class);
                intent.putExtra("payment_done", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}
