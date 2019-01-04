package org.ginlevel.ustapp3.storing;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.ginlevel.ustapp3.adapters.CategoryAdapter;
import org.ginlevel.ustapp3.adapters.UsersRecyclerViewAdapter;
import org.ginlevel.ustapp3.model.Category;
import org.ginlevel.ustapp3.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseDB {

    private DatabaseReference databaseReference;
    private static final String usersCollectionName = "users";
    private static final String categoriesCollectionName = "categories";
    private static final String TAG = "FIREBASE_DB_LOG";

    private List<Category> allCategories = new ArrayList<>();



    public FirebaseDB() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void saveUser(User user, final View view) {
        databaseReference
                .child(usersCollectionName)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(), "User was saved!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Failure while saving user! Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUser(User user, final Context context) {
//        Map<String, Object> map = new HashMap<>();
//        map.put(user.getUserId(), user);
//        databaseReference
//                .child(user.getUserId())
//                .updateChildren(map, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                        Toast.makeText(context, "User details was updated", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public void deleteUser(User user, final View view) {
        Query userQuery = databaseReference.child(usersCollectionName).orderByChild("full_name").equalTo(user.getFullName());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    userSnapshot.getRef().removeValue();
                    Toast.makeText(view.getContext(), "User was deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "Deleting user was canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadAllUsers(Context context, final RecyclerView usersRecyclerView, final ProgressBar loadingProgress) {
        final List<User> allUsers = new ArrayList<>();
        final UsersRecyclerViewAdapter usersRecyclerViewAdapter = new UsersRecyclerViewAdapter(context, allUsers);
        loadingProgress.setVisibility(View.VISIBLE);

        databaseReference.child(usersCollectionName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    allUsers.add(ds.getValue(User.class));
                }
                loadingProgress.setVisibility(View.GONE);
                usersRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "loadAllUsers error: " + databaseError.getDetails());
            }
        });
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        usersRecyclerView.setAdapter(usersRecyclerViewAdapter);
    }

    public void findUserByIdAndSetup(final String userId, final Map<String, View> views){
//        final ImageView mainPhoto = (ImageView) views.get("mainPhoto");
//        final ImageView editImage = (ImageView) views.get("editImage");
//        ImageView addPhotoImage = (ImageView) views.get("accAddPhoto");
        final TextView fullName = (TextView) views.get("fullName");
        final TextView category = (TextView) views.get("category");
        final TextView phoneNumber = (TextView) views.get("phoneNumber");
        final TextView email = (TextView) views.get("email");
        final TextView description = (TextView) views.get("description");

        databaseReference.child(usersCollectionName).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    fullName.setText(user.getFullName());
                    category.setText(user.getCategoryIn());
                    phoneNumber.setText(user.getPhoneNumber());
                    email.setText(user.getEmail());
                    description.setText(user.getJobDescription());
                } else {
                    Log.d(TAG, "findUserByIdAndSetup() -> user is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void findUsersByCategory(final String categoryName, Context context, final RecyclerView usersRecyclerView, final ProgressBar loadingProgress){
        final List<User> usersByCategory = new ArrayList<>();
        final UsersRecyclerViewAdapter usersRecyclerViewAdapter = new UsersRecyclerViewAdapter(context, usersByCategory);
        loadingProgress.setVisibility(View.VISIBLE);
        databaseReference.child(usersCollectionName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersByCategory.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    String userCategory = user.getCategoryIn();
                    if (userCategory != null && userCategory.equals(categoryName)){
                        usersByCategory.add(user);
                        Log.d(TAG, "findUsersByCategory --> in  usersByCategory was added user: " + user.getFullName());
                    }
                }
                loadingProgress.setVisibility(View.GONE);
                usersRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "findUsersByCategory error: " + databaseError.getDetails());
            }
        });

//        usersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        usersRecyclerView.setAdapter(usersRecyclerViewAdapter);
    }

    public void loadAllCategories(Context context, ListView categoryListView, final ProgressBar loadingProgress)  {
        final CategoryAdapter categoryAdapter = new CategoryAdapter(context, allCategories);
        loadingProgress.setVisibility(View.VISIBLE);
        databaseReference.child(categoriesCollectionName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allCategories.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    allCategories.add(ds.getValue(Category.class));
                }
                loadingProgress.setVisibility(View.GONE);
                categoryAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "loadAllCategory error: " + databaseError.getDetails());
            }

        });
        categoryListView.setAdapter(categoryAdapter);
    }


}
