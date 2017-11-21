package com.btm.pagodirecto.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner_;
import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

/**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public class Util {

    private static final String TAG = "Util";
    private static final String TAG_TOKEN = "TOKEN";
    private static final String TAG_USERNAME = "USERNAME";
    private static final String TAG_FIRSTNAME = "FIRSTNAME";
    private static final String TAG_LASTNAME = "LASTNAME";
    private static final String TAG_EMAIL = "EMAIL";
    public static final String INTENT_EXTRA_PROMOTIONS ="PROMOTIONS";
    private static final String TAG_ROLES = "USER_ROLES";
    private static final String TAG_MAX_ROL = "USER_MAX_ROLE";
    private static final String TAG_PREFERENCES = "PREFERENCES_CUSTOMER";
    protected static BaseActivity activity;
    protected static BeaconScanner_ activityBeacon;
    protected static SharedPreferences sharedPreferences;
    private static Long intervalTime = Long.valueOf(86400000);
    public static ProgressDialog progress;


    private static final String SOURCE_TYPE = "ma";          // Fuente de datos, Ejm: ma = mobile application
    private static final String TENANT_ID = "1000000" ;      // Identificador del cliente, Ejm: 1000000 = GSG
    private static final String THING_TYPE_ID = "100004" ;   // Identificador del tipo de "thing", Ejm: 100004 = smart-phone
    private static final String THING_ID =
            Build.SERIAL.toUpperCase();               // Identificador alfa numérico del "thing" Ejm: SerialID.

    public static final String HEADER_ID =
            SOURCE_TYPE+":"+TENANT_ID+":"+THING_TYPE_ID+":"+THING_ID; // Build id for Header Json
    private static Context context;

    public static Long getTimeForNotification(){
        return intervalTime;
    }

    public static void setActivity(BaseActivity _activity) {
        activity = _activity;
    }

    public static void setBeaconActivity(BeaconScanner_ _activity) {
        activityBeacon = _activity;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static Context getContext() {
        return getActivity().getApplicationContext();
    }

    public static void saveInSharedPreferences(String key, String value) {
        getSharedPreferences().          // get the unique instance for SP.
                edit().putString(key, value). // set the information to store in form of key-value.
                apply();                     // apply changes.
    }

    // Get the value from SP related to "key"
    public static String getFromSharedPreferences(String key) {
        return getSharedPreferences().
                getString(key, ""); //If not found return ""(empty). Avoid returning null
    }

    // Delete an specific value from SP
    protected static void deleteFromSharedPreferences(String key) {
        getSharedPreferences().
                edit().remove(key). // remove only the key value.
                apply();
    }

    public static void deleteAllSharedPreferences() {
        getSharedPreferences().
                edit().clear().     // remove al SP.
                apply();
    }

    // The next methods are the ones invoked in the code
    public static String getToken() {
        return getFromSharedPreferences(TAG_TOKEN);
    }

    public static void saveToken(String token) {
        saveInSharedPreferences(TAG_TOKEN, token);
    }

    /*public static void saveMaxAuthority(UserProfile.Role role) {
        saveInSharedPreferences(TAG_MAX_ROL, role.toString());
    }

    public static void saveUser(UserProfile user) {
        saveInSharedPreferences(TAG_USERNAME, user.getUsername());
        saveInSharedPreferences(TAG_FIRSTNAME, user.getFirstName());
        saveInSharedPreferences(TAG_LASTNAME, user.getLastName());
        saveInSharedPreferences(TAG_EMAIL, user.getEmail());
        //saveInSharedPreferences(TAG_MAX_ROL, user.getMaxAuthority());

    }

    public static String getUser() {
        return getFromSharedPreferences(TAG_USERNAME);
    }

    public static String getFirstName() {
        return getFromSharedPreferences(TAG_FIRSTNAME);
    }

    public static String getLastName() {
        return getFromSharedPreferences(TAG_LASTNAME);
    }

    public static String getEmail() {
        return getFromSharedPreferences(TAG_EMAIL);
    }

    public static UserProfile.Role getMaxAuthority() {
        return UserProfile.Role.valueOf(getFromSharedPreferences(TAG_MAX_ROL));
    }*/

    public static void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Return an instance of Shared Preference created only once
    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getActivity().
                    getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void goToActivity(Activity fromActivity, Class toActivity, int intentsFlags) {
        Intent intent = new Intent(getContext(), toActivity);
        getActivity().startActivity(intent.addFlags(intentsFlags));
        fromActivity.finish();
    }

    public static void goToActivitySlide(Activity fromActivity, Class toActivity, int intentsFlags) {
        Intent intent = new Intent(getContext(), toActivity);
        fromActivity.startActivity(intent.addFlags(intentsFlags));
        fromActivity.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
    }

    public static void goToActivitySlide(Activity fromActivity, Class toActivity) {
        Intent intent = new Intent(getContext(), toActivity);
        fromActivity.startActivity(intent);
        fromActivity.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
    }

    public static void goToActivitySlideBack(Activity fromActivity, Class toActivity, int intentsFlags) {
        Intent intent = new Intent(getContext(), toActivity);
        getActivity().startActivity(intent.addFlags(intentsFlags));
        fromActivity.finish();
        getActivity().overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }

    public static void goToActivityFade(Activity fromActivity, Class toActivity, int intentsFlags) {
        Intent intent = new Intent(getContext(), toActivity);
        getActivity().startActivity(intent.addFlags(intentsFlags));
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        fromActivity.finish();
    }

    public static void addFragment(FragmentManager frManager, Fragment fr, int container) {
        frManager
                .beginTransaction()
                .add(container, fr).commit();
    }

    public static void replaceFragment(FragmentManager frManager, Fragment fr, int container) {
        frManager
                .beginTransaction()
                .addToBackStack(fr.getClass().getSimpleName())
                .replace(container, fr).commit();
    }

    public static void showDialogMessage(String title, String msg) {
        try {
            if(progress == null) {
                progress = new ProgressDialog(getActivity(), R.style.MyProgressDialog);
            }
            progress.setTitle(title);
            progress.setMessage(msg);
            progress.show();
        }catch (Exception e){
            Log.e(TAG,"Progress is null");
            progress = null;
        }

    }

    public static void hideDialogMessage() {
        try {
            if(progress== null) {
                progress = new ProgressDialog(getActivity());
            }

            if (progress.isShowing()) {
                progress.dismiss();
            }        }catch (Exception e){
            Log.e(TAG,"Progress is null");
            progress = null;
        }
    }

    public static void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static void sendEmail(Exception e, String module) {
        Log.i("Send email", "");

        String[] TO = {"support@iter.com"};
        String[] CC = {"edwin@iterapp.co;pedro@iterapp.co;hugo@iterapp.co;rivera@iterapp.co"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ITER: Report BUG");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Activity: "+ getActivity().getClass().getSimpleName() + "\n\n" +
                "Excepcion: "+ e.toString() + "\n" +
                "Excepcion message: "+ e.getMessage() + " :: in "+  "\n\n" + module +  "\n\n" +
                "Exception cause: " + e.getCause() + "\n\n" +
                "Thanks for your feedback!! :) \n"+
                "Regards, \n"+
                "ITER Developer Team.");

        try {
            getActivity().startActivity(Intent.createChooser(emailIntent, "Sending mail..."));
            Log.i("Finished sending mail..", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Util.getActivity(),
                    "There is no email client installed, please install to send the exception to the developer team :)!", Toast.LENGTH_SHORT).show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void setContext(Context context) {
        Util.context = context;
    }

    public static <S> S string2Object(String jsonString,Class<S> objectClass) {
        Gson gson = new Gson();
        S obj = gson.fromJson(jsonString, objectClass);
        return obj;
    }

    public static String formatMoney(String s) {
        return s;
    }
}
