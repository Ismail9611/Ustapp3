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
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.activity.MainActivity;

public class SignInFragment extends Fragment {

    private View view;
    private EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;

    public SignInFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_in_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        Button btnSignIn = view.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                signIn(email, password);
            }
        });

        return view;
    }

    private void signIn(String email, String password) {
        if (email.equals("") || password.equals("")) {
            Toast.makeText(view.getContext(), "Email/Password must not be blank!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(view.getContext(), "Successfully sign in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(view.getContext(), MainActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(view.getContext(), "Sign in failure!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
