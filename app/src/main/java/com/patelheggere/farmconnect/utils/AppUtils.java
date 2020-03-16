package com.patelheggere.farmconnect.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.patelheggere.farmconnect.FarmConnectApplication;
import com.patelheggere.farmconnect.model.notify.PushNotificationModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.NetworkUtil;
import com.patelheggere.farmconnect.network.RetrofitInstance;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppUtils {
    public static final  int CAMERA_PERMISSION = 151;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public static boolean checkPermissionCamera(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkPermissionCamera(final Context context, final int CAMERA_PERMISSION)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public static void showSnackBar(Activity context, String message)
    {
        Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
    public static boolean checkInternetStatus() {
        String status = NetworkUtil.getConnectivityStatusString(FarmConnectApplication.getInstance().getApplicationContext());
        boolean isNetworkAvailable = false;
        try {
            switch (status) {
                case "Connected to Internet with Mobile Data":
                    isNetworkAvailable = true;
                    break;
                case "Connected to Internet with WIFI":
                    isNetworkAvailable = true;
                    break;
                default:
                    isNetworkAvailable = false;
                    break;
            }
            //  showSnackBarBasedonStatus(isNetworkAvailable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNetworkAvailable;
    }
    public static void setLocale(Context context) {
        Locale myLocale = new Locale(SharedPrefsHelper.getInstance().get(Constants.LANGUAGE_SELECTED, "en"));
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        Resources res = context.getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
    public static void showToast(String message) {
        try {
            if ((message != null) && (!message.isEmpty())) {
                final Context context = FarmConnectApplication.getInstance().getBaseContext();
                final CharSequence text = message;
                final int duration = Toast.LENGTH_SHORT;
                Handler mainHandler = new Handler(context.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, "  " + text + "  ", duration);
                        toast.show();
                    }
                };
                mainHandler.post(myRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class Constants{
        public static final String LANGUAGE = "language";
        public static final String LANGUAGE_SELECTED = "language";
        public static final String JOB = "job";
        public static final long ONE_SECOND = 1000;
        public static final long TWO_SECOND = 2000;
        public static final long THREE_SECOND = 3000;
        public static final long FOUR_SECOND = 4000;
        public static final long TEN_SECOND = 10000;
        public static final long EIGHT_SECOND = 8000;


        public static final String PLACES = "PLACES";
        public static final String ADDS = "ADDS";
        public static final String LAT = "LAT";
        public static final String LON = "LON";
        public static final String RESTAURANT = "restaurant";
        public static final String HOTEL = "lodging";
        public static final String HOSPITAL = "hospital";

        public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
        public static final String ZERO_RESULTS = "ZERO_RESULTS";
        public static final String OK = "OK";
        public static final String GALLERY = "gallery";
        public static final String EVENTS = "events";
        public static final String NEWS = "news";


        public static final String FIRST_TIME = "first";
        public static final String NAME ="name";
        public static final String EMAIL = "email";
        public static final String USER_ID = "userid";
        public static final String AVATAR = "avatar";
        public static final String MOBILE = "mobile";
        public static final String COURSE = "course";
        public static final String CHANNEL_ID = "my_channel_01";
        public static final String CHANNEL_NAME = "Simplified Coding Notification";
        public static final String CHANNEL_DESCRIPTION = "www.simplifiedcoding.net";
        public static final String QUALIFICATIONS = "qualification";
        public static final String DESIRED_JOB = "desiredJob";
        public static final String LOGIN_TYPE = "logintype";
        public static final String CROP_LIST = "cropList";

        public static final String API_KEY_ID ="rzp_test_t6eDUw8dTo8SOL";
        public static final String API_SECRET_KEY ="eIK7z1Csr3Va6OPXQizSrzhv";

    }
    public static void sendPush(String msg, String title){
        setUpNetwork();
        PushNotificationModel pushNotificationModel = new PushNotificationModel();
        pushNotificationModel.setMsg(msg);
        pushNotificationModel.setTitle(title);
        Call<PushNotificationModel> pushNotificationModelCall = apiInterface.sendPushMessage(pushNotificationModel);
        pushNotificationModelCall.enqueue(new Callback<PushNotificationModel>() {
            @Override
            public void onResponse(Call<PushNotificationModel> call, Response<PushNotificationModel> response) {
            }

            @Override
            public void onFailure(Call<PushNotificationModel> call, Throwable t) {

            }
        });

    }
    public static ApiInterface apiInterface;

    private static void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

}
