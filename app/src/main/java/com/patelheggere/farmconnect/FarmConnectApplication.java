package com.patelheggere.farmconnect;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FarmConnectApplication extends Application {
    private static FarmConnectApplication mInstance;
    private static DatabaseReference databaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT > 23) {
            builder.detectFileUriExposure();
        }
        FirebaseApp.initializeApp(this);
        if(FirebaseApp.getInstance()!=null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        // ApiClient.intialise();
       /* if(isDeve()) {
            ApiClient.setBaseUrl(AppConstants.appBaseUrlDev);
        }
        else
        {
            ApiClient.setBaseUrl(AppConstants.appBaseUrl);

        }*/

    }
    public static synchronized DatabaseReference getFireBaseRef()
    {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if(BuildConfig.DEBUG) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FARM_CONNECT").child("test");
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FARM_CONNECT").child("prod");
        }
        return databaseReference;
    }

    public static synchronized FarmConnectApplication getInstance() {
        return mInstance;
    }

}