package org.ginlevel.ustapp3.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.activity.UserAccountActivity;
import org.ginlevel.ustapp3.model.User;

import java.util.List;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private List<User> userList;
    private LayoutInflater inflater;
    private Context context;

    public UsersRecyclerViewAdapter(Context context, List<User> userList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.user_recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.tvFullName.setText(user.getFullName());
        holder.tvCategory.setText(user.getCategoryIn());
        holder.tvPhoneNumber.setText(user.getPhoneNumber());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserAccountActivity.class);
                intent.putExtra("userAccount", user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvCategory, tvPhoneNumber;
        ImageView ivUserPhoto;
        CardView cardView;


        ViewHolder(final View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.rvUserFullNameTextView);
            tvCategory = itemView.findViewById(R.id.rvUserCategoryTextView);
            tvPhoneNumber = itemView.findViewById(R.id.rvUserPhoneNumTextView);
            ivUserPhoto = itemView.findViewById(R.id.rvUserImageView);
            cardView = itemView.findViewById(R.id.recyclerViewCard);
        }
    }


}
