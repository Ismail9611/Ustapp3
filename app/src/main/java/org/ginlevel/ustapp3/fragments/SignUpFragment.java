package org.ginlevel.ustapp3.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.activity.AccountActivity;
import org.ginlevel.ustapp3.activity.CategoryActivity;
import org.ginlevel.ustapp3.activity.MainActivity;
import org.ginlevel.ustapp3.activity.PaymentActivity;
import org.ginlevel.ustapp3.model.User;
import org.ginlevel.ustapp3.storing.FirebaseDB;
import org.ginlevel.ustapp3.utils.AdditionalFunc;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment {

    private static final int CATEGORY_SELECTED = 1;


    private View view;
    private EditText etSignUpEmail, etSignUpPass, etSignUpPassConfirm, etSignUpFullName, etSignUpPhoneNumber;
    private Button btnSignUp, btnSignUpCategory;
    private FirebaseAuth firebaseAuth;
    private ImageButton ibAvatar;
    private Uri avaUri;
    private static final int IMAGE_PICK = 1, PAYMENT_DONE = 2;
    private FirebaseDB firebaseDB;
    private User user;
    private boolean isPayed;

    public SignUpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = new FirebaseDB();
        fieldsInit();

        ibAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICK);
            }
        });

        btnSignUpCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDB.loadAllCategoriesToDialog(view.getContext(), btnSignUpCategory);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etSignUpFullName.getText().toString();
                String email = etSignUpEmail.getText().toString();
                String phoneNum = etSignUpPhoneNumber.getText().toString();
                String password = etSignUpPass.getText().toString();
                String passConfirm = etSignUpPassConfirm.getText().toString();
                String category = btnSignUpCategory.getText().toString();
                String description = "Empty..";

                boolean validate = AdditionalFunc.signUpFieldsValidate(fullName, email, phoneNum, password, passConfirm, category, view);

                if (validate) {
                    User user = new User(fullName, email, phoneNum);
                    user.setCategoryIn(category);
                    user.setJobDescription(description);

                    if (isPayed) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            firebaseAuth.signOut();
                        }
                        signUpAndSave(view, user, password);
                    } else {
                        Intent intent = new Intent(view.getContext(), PaymentActivity.class);
                        startActivityForResult(intent, PAYMENT_DONE);
                    }
                }
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK) {
                avaUri = data.getData();
                Glide.with(view.getContext()).load(avaUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ibAvatar);
            }
            if (requestCode == CATEGORY_SELECTED) {
                btnSignUpCategory.setText(data.getCharSequenceExtra("category"));
            }
            if (data.getBooleanExtra("payment_done", false)) {
                isPayed = true;
                btnSignUp.setText(R.string.btAfterPayment);
            }
        }
    }

    private void signUpAndSave(final View view, final User user, String password) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(view.getContext(), AccountActivity.class));
                emailVerification(firebaseAuth.getCurrentUser());
                if (firebaseAuth.getCurrentUser() != null) {
                    firebaseDB.saveUser(user, view);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void emailVerification(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(view.getContext(), "User email was verified!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void fieldsInit() {
        ibAvatar = view.findViewById(R.id.ibAvatar);
        etSignUpFullName = view.findViewById(R.id.etSignUpFullName);
        etSignUpPhoneNumber = view.findViewById(R.id.etSignUpPhone);
        etSignUpEmail = view.findViewById(R.id.etSignUpEmail);
        etSignUpPass = view.findViewById(R.id.etSignUpPass);
        etSignUpPassConfirm = view.findViewById(R.id.etSignUpPassConfirm);
        btnSignUpCategory = view.findViewById(R.id.btnSignUpCategory);
        btnSignUp = view.findViewById(R.id.btnSignUp);
    }


}
