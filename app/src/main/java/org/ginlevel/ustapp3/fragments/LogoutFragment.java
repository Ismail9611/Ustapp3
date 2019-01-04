package org.ginlevel.ustapp3.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.activity.MainActivity;

public class LogoutFragment extends Fragment {

    private View view;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.logout_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(view.getContext(), MainActivity.class));
                }
            }
        });

        return view;
    }

}
