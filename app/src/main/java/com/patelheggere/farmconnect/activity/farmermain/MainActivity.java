package com.patelheggere.farmconnect.activity.farmermain;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmeraddcrop.AddCropDataActivity;
import com.patelheggere.farmconnect.activity.languegae.LanguageActivity;
import com.patelheggere.farmconnect.activity.farmermain.ui.dashboard.DashboardFragment;
import com.patelheggere.farmconnect.activity.farmermain.ui.home.HomeFragment;
import com.patelheggere.farmconnect.activity.farmermain.ui.notifications.NotificationsFragment;
import com.patelheggere.farmconnect.activity.merchantmain.MerchantMainActivity;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.LoginModel;
import com.patelheggere.farmconnect.model.notify.Data;
import com.patelheggere.farmconnect.model.notify.Notification;
import com.patelheggere.farmconnect.model.notify.SendNotificationModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.RetrofitInstance;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.patelheggere.farmconnect.utils.FilePath;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.AVATAR;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.EMAIL;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.FIRST_TIME;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LANGUAGE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LOGIN_TYPE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.MOBILE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.NAME;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.PASSWORD;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;
import static com.patelheggere.farmconnect.utils.AppUtils.hasPermissions;
import static com.patelheggere.farmconnect.utils.AppUtils.setLocale;
import static com.patelheggere.farmconnect.utils.AppUtils.showToast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private AlertDialog alertDialog;
    private String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            /* android.Manifest.permission.RECORD_AUDIO,*/
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    int PERMISSION_ALL = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private static final int SELECT_FILE = 1100;
    private static final int REQUEST_CAMERA = 1200;
    private static final int SELECT_VIDEO = 1300;
    private static final int REQUEST_CODE_DOC = 1400;
    private static final int RQS_RECORDING = 1500;
    private static final int VIDEO_CAPTURE = 1600;
    private static final int AUDIO_LOCAL = 1700;
    private TextView mTextViewName, mTextViewEmail;
    private String mEmail, mUserName;
    private CircleImageView mImageViewAvatar;
    private String phone;
    private TextView profile;
    private Toolbar toolbar;
    private FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar(getString(R.string.title_home));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        mTextViewName = headerView.findViewById(R.id.textViewName);
        mTextViewEmail = headerView.findViewById(R.id.textViewEmail);
        mImageViewAvatar = headerView.findViewById(R.id.imageViewProfile);
        profile = headerView.findViewById(R.id.imageViewProfileText);
        mUserName = SharedPrefsHelper.getInstance().get(NAME);
        mEmail = SharedPrefsHelper.getInstance().get(EMAIL);
        mTextViewName.setText(mUserName);
        mTextViewEmail.setText(mEmail);
        mFloatingActionButton = findViewById(R.id.fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddCropDataActivity.class));
            }
        });


        String url =  SharedPrefsHelper.getInstance().get(AVATAR);
        if(url!=null)
        {
            Glide.with(MainActivity.this).load(url).into(mImageViewAvatar);
            mImageViewAvatar.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
        }
        else {
            if(mUserName!=null) {
                profile.setText(mUserName.substring(0, 2).toUpperCase());
                profile.setVisibility(View.VISIBLE);
                mImageViewAvatar.setVisibility(View.GONE);
            }

        }
        phone = SharedPrefsHelper.getInstance().get(MOBILE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view_bottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        final String token = task.getResult().getToken();
                        if(SharedPrefsHelper.getInstance().get("TOKEN")==null || !token.equalsIgnoreCase(SharedPrefsHelper.getInstance().get("TOKEN").toString())) {
                            Log.d(TAG, "onComplete: " + token);
                            setUpNetwork();
                            String phone = SharedPrefsHelper.getInstance().get(MOBILE);
                            Call<APIResponseModel> apiResponseModelCall = apiInterface.updateFcm(phone, token);
                            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                @Override
                                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getPhone() != null) {
                                        SharedPrefsHelper.getInstance().save("TOKEN", token);
                                        Log.d(TAG, "onResponse updated token: " + token);
                                        SendNotificationModel sendNotificationModel = new SendNotificationModel();
                                        Notification notification = new Notification();
                                        notification.setTitle("Notification Title ");
                                        notification.setBody("Notification Body");
                                        Data data = new Data();
                                        data.setTitle("Data Title");
                                        data.setBody("Data Body");
                                        sendNotificationModel.setData(data);
                                        sendNotificationModel.setNotification(notification);
                                        sendNotificationModel.setCollapse_key("type_a");
                                        String[] ids = new String[]{token};
                                        sendNotificationModel.setRegistration_ids(ids);
                                        Gson gson = new Gson();
                                        Log.d(TAG, "submitData json: " + gson.toJson(sendNotificationModel));
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponseModel> call, Throwable t) {

                                }
                            });
                        }

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //  Log.d(TAG, msg);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        if(!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
    }
    private void setupToolBar(String text) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(text);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private String imageURL;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            Fragment curFrag = fragmentManager.getPrimaryNavigationFragment();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d(TAG, "onNavigationItemSelected: ");
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    Fragment fragment = fragmentManager.findFragmentByTag("NEWS");
                    if (fragment == null) {
                        fragment = new HomeFragment();
                        fragmentTransaction.add(R.id.nav_host_fragment_bottom, fragment, "NEWS");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commitNowAllowingStateLoss();
                    setupToolBar(getString(R.string.title_home));


                    return true;

                case R.id.navigation_dashboard:
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }

                    fragment = fragmentManager.findFragmentByTag("SOFT_SKILL");
                    if (fragment == null) {
                        fragment = new DashboardFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment_bottom, fragment, "SOFT_SKILL");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commitNowAllowingStateLoss();
                    setupToolBar(getString(R.string.title_dashboard));

                    // mTextMessage.setText(R.string.title_dashboard);
                    return true;

               /* case R.id.navigation_events:
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("EVENTS");
                    if (fragment == null) {
                        fragment = new SoftSkillFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment, "EVENTS");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commitNowAllowingStateLoss();
                    // mTextMessage.setText(R.string.title_dashboard);
                    return true;*/

                case R.id.navigation_notifications:
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("JOBS");
                    if (fragment == null) {
                        fragment = new NotificationsFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment_bottom, fragment, "JOBS");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commitNowAllowingStateLoss();
                    setupToolBar(getString(R.string.title_notifications));

                    //mTextMessage.setText(R.string.title_notifications);
                    return true;

            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {

         /*   FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            Fragment curFrag = fragmentManager.getPrimaryNavigationFragment();
            if (curFrag != null) {
                fragmentTransaction.detach(curFrag);
            }
            Fragment fragment = fragmentManager.findFragmentByTag("NEWS");
            if (fragment == null) {
                fragment = new NewsFragment();
                fragmentTransaction.add(R.id.nav_host_fragment, fragment, "NEWS");
            } else {
                fragmentTransaction.attach(fragment);
            }
            fragmentTransaction.setPrimaryNavigationFragment(fragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.commitNowAllowingStateLoss();*/
        }
        else if (id == R.id.nav_share) {
            shareApp();
        }
        else if (id == R.id.nav_contact) {
            contactUS();
        }
        else if(id==R.id.nav_tools)
        {
            showAlert();
        }
       /* else if(id==R.id.nav_slideshow){
            startActivity(new Intent(this, AdminActivity.class));
        }*/
        else if(id==R.id.nav_logout){
            SharedPrefsHelper.getInstance().clearAllData();
            startActivity(new Intent(MainActivity.this, LanguageActivity.class));
            finish();
        }
        else if(id==R.id.nav_mode)
        {
            setUpNetwork();
            submitLoginDetails();
        }
        /*
        else if (id == R.id.nav_events) {
            Intent intent = new Intent(context, EventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(context, GalleryActivity.class);
            startActivity(intent);
            //overridePendingTransition();
        } else if (id == R.id.nav_feedback) {
            sendEmail();
        }  else if (id == R.id.nav_contact_us) {
            contactUS();
        } else if (id == R.id.nav_language) {
            showAlert();
        }
        else if(id==R.id.nav_exam)
        {
            showExamScreen();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void shareApp() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
        //shareIntent.putExtra(Intent.EXTRA_STREAM, R.drawable.finaled);
        shareIntent.setType("text/plain");
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_us_via)));
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(MainActivity.this, getString(R.string.no_app_to_share), Toast.LENGTH_LONG).show();
        }
    }
    private void contactUS() {
       /* final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_us, null);
        builder.setView(view);

        TextView textViewTitle = view.findViewById(R.id.tv_cant_cashout);
        Button btn = view.findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        phone = view.findViewById(R.id.phone_number);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(isCallPermissionRPermitted()) {
                    callPhoneNumber(phone.getText().toString());
                }
                else {
                    requestCallPermission();
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }*/
    }
    private void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_layout, null);
        builder.setView(view);

        TextView textViewTitle = view.findViewById(R.id.tv_cant_cashout);
        // textViewTitle.setText(title);
        RadioGroup radioGroup;
        RadioButton radioButtonKannada, radioButtonEnglish;

        radioGroup = view.findViewById(R.id.rg_language);
        RadioButton radioKan = view.findViewById(R.id.rb_kannada);
        RadioButton radioEng = view.findViewById(R.id.rb_english);
        String lan = SharedPrefsHelper.getInstance().get(LANGUAGE);
        if(lan.equalsIgnoreCase("ka"))
        {
            radioKan.setChecked(true);
        }
        else if(lan.equalsIgnoreCase("en"))
        {
            radioEng.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_kannada) {
                    alertDialog.dismiss();
                    showConfirmationDialog("ka");

                } else if (i == R.id.rb_english) {
                    alertDialog.dismiss();
                    showConfirmationDialog("en");
                }

            }

        });

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
    private void showConfirmationDialog(final String language) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        alertBuilder.setTitle(R.string.confirmation);
        alertBuilder.setMessage(R.string.conf_message);
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String lan = SharedPrefsHelper.getInstance().get(LANGUAGE);
                if(!language.equalsIgnoreCase(lan)) {
                    SharedPrefsHelper.getInstance().save(LANGUAGE, language);
                    //restartActivity();
                    //recreate();
                    startActivity(getIntent());
                    setLocale(MainActivity.this);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.no), null);
        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
    private void addPhoto() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }

    private void addVideo() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectVideo();
    }
    private String userChoosenTask;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(MainActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (camera)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (gallery)
                    {
                        chooseImage();
                        //galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_2);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST_2);
    }

    private void selectVideo() {
        final CharSequence[] items = {"Capture Video", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(MainActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(MainActivity.this);
                if (items[item].equals("Capture Video")) {
                    userChoosenTask = "Capture Video";
                    if (hasCamera()) {
                        videoCaptureIntent();
                    } else {
                        showToast("No Camera Found");
                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (gallery)
                    {
                        chooseVideo();
                        //videoFromGalleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectAudio() {
        final CharSequence[] items = {"Record Audio", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(MainActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(MainActivity.this);
                if (items[item].equals("Record Audio")) {
                    userChoosenTask = "Record Audio";
                    audioIntent();
                    //recordingAlert();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    AudioLocalIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void videoFromGalleryIntent() {
        if (!AppUtils.checkInternetStatus()) {
            showToast("No Internet connected");
            //return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }
    private File mediaFile;

    private void videoCaptureIntent() {
        if (!AppUtils.checkInternetStatus()) {
            showToast("No Internet connected");
            // return;
        }
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(mediaFile);
        /*if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", mediaFile);
        }*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        //intent.putExtra(EXTRA_VIDEO_QUALITY, 0);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, VIDEO_CAPTURE);
        }
    }

    private void audioIntent() {
        if (!AppUtils.checkInternetStatus()) {
            showToast("No Internet connected");
            //  return;
        }
        //Intent intent = new Intent(mActivity, AudioRecordActivity.class);
        //startActivityForResult(intent, RQS_RECORDING);
    }
    private void AudioLocalIntent()
    {
        if (!AppUtils.checkInternetStatus()) {
            showToast("No Internet connected");
            // return;
        }
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,AUDIO_LOCAL);
    }

    private void onSelectFromGalleryResult(Intent data, String mediaType) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Uri path = data.getData();
        File original;
        if (path != null) {
            if (path.toString().contains("com.google.android.apps.photos")) {
                Log.d(TAG,"From android photos ");
                String filePath = FilePath.getPathFromInputStreamUri(MainActivity.this, path);

                original = new File(filePath);
                String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                    crop_ImageAndUpload(original,extension_file,mediaType);
                }else {
                    //uploadImageToAWS(new File(filePath), mediaType);
                    uploadImage(Uri.fromFile(original));
                }
                //OustSdkTools.showToast("can't select attachment from google photos app");
                //return;
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Log.d(TAG,"from SDK more than Kitkat");
                String filePath = getRealPathFromUri(MainActivity.this, path);
                if (filePath != null) {
                    original = new File(filePath);
                    uploadImage(Uri.parse(filePath));
                  /*  String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                    if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                        crop_ImageAndUpload(original,extension_file,mediaType);
                    }else {
                        uploadImage(Uri.parse(filePath));
                        //uploadImageToAWS(new File(filePath), mediaType);
                    }*/
                } else {
                    showToast("unable to get file");
                }
            } else {

                String[] proj = {MediaStore.Images.Media.DATA};
                String result = null;

                CursorLoader cursorLoader = new CursorLoader(
                        MainActivity.this,
                        path, proj, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor != null) {
                    int column_index =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    result = cursor.getString(column_index);
                    if (result != null) {
                        uploadImage(Uri.parse(result));
                        //uploadImageToAWS(new File(result), mediaType);
                    }
                }
            }
        }
    }

    private void storeImage(Bitmap image, File pictureFile) {
        //File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public void crop_ImageAndUpload(File original, String extension_file, String mediaType){
        try {
            //change the filepath
            Bitmap d = new BitmapDrawable(getResources(), original.getPath()).getBitmap();
            int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
            Bitmap bitmap_new = Bitmap.createScaledBitmap(d, 512, nh, true);
            Log.d(TAG, "original:" + d.getByteCount() + " -- duplicate:" + bitmap_new.getByteCount());
            //Log.d(TAG, "Bitmap width:" + bitmap_new.getWidth() + " -- height:" + bitmap_new.getHeight());

            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "" + extension_file);
            // storeImage(bitmap_new, destination);

            Log.d(TAG, "file size  duplicate:" + destination.length() + " -- Original:" + original.length());
            // uploadImageToAWS(destination, mediaType);
            uploadImage(Uri.fromFile(original));
        }catch (Exception e){
            e.printStackTrace();
            uploadImage(Uri.fromFile(original));
            //  uploadImageToAWS(original, mediaType);
            //Toast.makeText(this,"Couldn't able to load the image. Please try again.",Toast.LENGTH_LONG).show();
        }
    }

    public static String getRealPathFromUri(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (destination != null) {
            String extension_file = destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("."));
            if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                crop_ImageAndUpload(destination,extension_file,"IMAGE");
            }else {
                uploadImage(Uri.fromFile(destination));
                // uploadImageToAWS(destination, "IMAGE");
            }

        }
    }

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
            filePath = data.getData();
        // imageViewUploaded.setImageURI(filePath);
        // imageViewUploaded.setVisibility(View.VISIBLE);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            // imageViewUploaded.setImageURI(filePath);
            // imageViewUploaded.setVisibility(View.VISIBLE);
            //   if(filePath!=null)
            //  uploadImage();
        }
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode==PICK_IMAGE_REQUEST_2){
                uploadImage(filePath);
            }
            else if(requestCode==PICK_VIDEO_REQUEST_2) {
                uploadImage(filePath);
            }
            else if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data, "IMAGE");
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == SELECT_VIDEO)
                onSelectFromGalleryResult(data, "VIDEO");
            else if (requestCode == REQUEST_CODE_DOC) {
                onSelectFromGalleryResult(data, "ALL");
            }else if(requestCode==AUDIO_LOCAL){
                onSelectFromGalleryResult(data, "AUDIO");
            }
            else if (requestCode == RQS_RECORDING) {
                String result = data.getStringExtra("result");
                if(result!=null){
                    // uploadImageToAWS(new File(result), "AUDIO");
                }
                else {

                }
            } else if (requestCode == VIDEO_CAPTURE) {
                Log.d(TAG, "onActivityResult: ");
                //onSelectFromGalleryResult(data, "VIDEO");
                //  isVideoCaptured = true;
                uploadImage(Uri.fromFile(mediaFile));
                // uploadImageToAWS(mediaFile, "VIDEO");
            }
        }
    }
    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        mImageViewAvatar.setImageURI(filePath);
        storageReference = storage.getReference();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images").child("profile").child(phone);
            //UploadTask uploadTask =
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    {
                                        imageURL = downloadUrl.toString();
                                        SharedPrefsHelper.getInstance().save("PICK_URL", imageURL);
                                        if(imageURL!=null)
                                        {
                                            // Glide.with(MainActivity.this).load(imageURL).into(mImageViewAvatar);
                                            profile.setVisibility(View.GONE);
                                            mImageViewAvatar.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            profile.setVisibility(View.VISIBLE);
                                            mImageViewAvatar.setVisibility(View.GONE);
                                        }
                                        setUpNetwork();
                                        updateAvatr();

                                        /*if(url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png"))
                                        {
                                            mImageURL = url;
                                        }
                                        else if(url.contains(".mp4") ) {
                                            mVideoURL = url;
                                        }*/

                                        //  textViewURL.setText(downloadUrl.toString());
                                    }
                                    //imageViewUploaded.setVisibility(View.VISIBLE);
                                }
                            });

                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }
    }

    private void updateAvatr() {
        Call<APIResponseModel> call = apiInterface.updateAvatar(phone, imageURL);
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                if(response.isSuccessful())
                {
                    mImageViewAvatar.setVisibility(View.VISIBLE);
                    profile.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {

            }
        });
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void submitLoginDetails() {

        LoginModel loginModel = new LoginModel();
        loginModel.setPhone(SharedPrefsHelper.getInstance().get(MOBILE).toString());
        if(SharedPrefsHelper.getInstance().get(PASSWORD)!=null)
        {
            loginModel.setPwd(SharedPrefsHelper.getInstance().get(PASSWORD).toString());
        }
        Call<APIResponseModel> userVerifyCall = apiInterface.verifyUser(loginModel.getPhone(), loginModel.getPwd(), ""+2);
        userVerifyCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
               // mProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().isStatus())
                {
                    SharedPrefsHelper.getInstance().save(LOGIN_TYPE, false);
                    SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                    SharedPrefsHelper.getInstance().save(NAME,response.body().getName());
                    SharedPrefsHelper.getInstance().save(MOBILE, response.body().getPhone());
                    SharedPrefsHelper.getInstance().save(EMAIL, response.body().getEmail());
                    SharedPrefsHelper.getInstance().save(AVATAR, response.body().getImageURL());
                    SharedPrefsHelper.getInstance().save(USER_ID, response.body().getId());
                    startActivity(new Intent(MainActivity.this, MerchantMainActivity.class));
                    finish();
                    //startMainActivity();
                }else{
                   // mButtonLoginSubmit.setEnabled(true);
                   // mButtonLoginSubmit.setClickable(true);
                    showToast(getString(R.string.invalid));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
               // mProgressBar.setVisibility(View.GONE);

                showToast(getString(R.string.something_wrong));
            }
        });
    }

}
