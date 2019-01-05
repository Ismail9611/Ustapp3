package org.ginlevel.ustapp3.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import org.ginlevel.ustapp3.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdditionalFunc {

    private AdditionalFunc(){
    }

    public static boolean isInternetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static boolean signUpFieldsValidate(String fullName, String email, String phoneNum,
                                               String password, String passConfirm, String category, View view){
        if (fullName.length() < 5){
            toastMessage(R.string.fullNameErr, view);
            return false;
        }
        if (phoneNum.length() < 9){
            toastMessage(R.string.phoneNumErr, view);
            return false;
        }
        if (!emailRegex(email)){
            toastMessage(R.string.emailErr, view);
            return false;
        }
        if (password.length() < 5){
            toastMessage(R.string.passwordErr, view);
            return false;
        }
        if (!password.equals(passConfirm)){
            toastMessage(R.string.passwordConfirmErr, view);
            return false;
        }
        if (category.equals("Select category")){
            toastMessage(R.string.categoryErr, view);
            return false;
        }
        return true;
    }

    private static void toastMessage(int messageRes, View view){
        String message = view.getContext().getString(messageRes);
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private static boolean emailRegex(String regex){
        Pattern pattern = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
        Matcher matcher = pattern.matcher(regex);
        return matcher.matches();
    }

}
