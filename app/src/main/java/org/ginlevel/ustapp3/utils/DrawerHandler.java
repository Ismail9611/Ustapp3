package org.ginlevel.ustapp3.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import org.ginlevel.ustapp3.R;
import org.ginlevel.ustapp3.activity.AccountActivity;
import org.ginlevel.ustapp3.activity.SignInUpActivity;
import org.ginlevel.ustapp3.model.User;

public class DrawerHandler {

    private Activity activity;
    private User user;


    public DrawerHandler(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }


    public void drawerBuilder(Toolbar toolbar) {
        DrawerBuilder drawerResult = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(activity.getResources().getString(R.string.nav_reg))
                                .withIcon(activity.getResources().getDrawable(R.drawable.ic_nav_reg))
                                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        Intent intent = new Intent(activity, SignInUpActivity.class);
                                        activity.startActivity(intent);
                                        return true;
                                    }
                                }),
                        new SecondaryDrawerItem()
                                .withName(activity.getResources().getString(R.string.nav_about))
                                .withIcon(activity.getResources().getDrawable(R.drawable.ic_nav_info))

                );
        if (user != null){
            drawerResult.withAccountHeader(accountHeaderInit(user));
            drawerResult.build();
        } else {
            drawerResult.build();
        }

    }

    private AccountHeader accountHeaderInit(User user) {
        IProfile userProfile = initProfile(user);
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(activity.getResources().getDrawable(R.color.colorPrimary))
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(view.getContext(), AccountActivity.class);
                        view.getContext().startActivity(intent);
                        return current;
                    }
                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .addProfiles(userProfile).build();
    }

    private IProfile initProfile(User user){
        IProfile profile = new ProfileDrawerItem();
        profile.withEmail(user.getEmail());
        profile.withName(user.getFullName());
        if (user.getMainPhotoUrl() != null){
            profile.withIcon(user.getMainPhotoUrl());
        }
        return profile;
    }

}
