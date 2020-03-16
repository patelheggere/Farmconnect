package com.patelheggere.farmconnect.activity.registration;

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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.patelheggere.farmconnect.FarmConnectApplication;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmermain.MainActivity;
import com.patelheggere.farmconnect.activity.merchantmain.MerchantMainActivity;
import com.patelheggere.farmconnect.base.BaseActivity;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.AssemblyModel;
import com.patelheggere.farmconnect.model.DistrictModel;
import com.patelheggere.farmconnect.model.LoginModel;
import com.patelheggere.farmconnect.model.TalukModel;
import com.patelheggere.farmconnect.model.UserDetails;
import com.patelheggere.farmconnect.model.VillageModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.RetrofitInstance;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.patelheggere.farmconnect.utils.FilePath;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.AVATAR;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.COURSE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.EMAIL;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.FIRST_TIME;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LOGIN_TYPE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.MOBILE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.NAME;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;

public class RegistrationActivity extends BaseActivity {

    private static final String TAG = "RegistrationActivity";
    private ActionBar mActionBar;
    private Spinner spinner;
    private TextInputEditText mInputEditTextOTP, textInputEditTextName, textInputEditTextPhone,textInputEditTextPhoneLogin, textInputEditTextEmail, textInputEditTextPwd, textInputEditTextPwdLogin;
    private String course;
    private List<String> listInterest;
    private ArrayAdapter<String> adapter;
    private Button mRegisterSubmit, mButtonLoginSubmit, mButtonLogin, mButtonRegister;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private View registerView, loginView;
    private ProgressBar mProgressBar;
    private CircleImageView circleImageView;
    private Button btn_upload;
    private String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
    private Switch mSwitchLogin, mSwitchRegister;
    private boolean isFarmer = true;
    private Spinner spinnerState, spinnerDist, spinnerTaluk, spinnerHobli, mSpinnerVillage, mSpinnerAssembly;
    private List<DistrictModel> districtModelList;
    private List<TalukModel> talukModelList;
    private List<VillageModel> villageModelList;

    private List<String> mDistrictNamesList;
    private List<String> mAssemblyNamesList;
    private List<String> mTalukNames;
    private List<String> mTandaNames;
    private List<String> listState, listDistrict, listTaluks, listHobli, listVillage, listGP;
    private String dist, village, taluk, hobli, gp, state = "Karnataka";
    private String type="1";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId, phoneNumber, otp;
    private Button mButtonVerifyOTP, mButtonResendOTP;
    private ConstraintLayout mConstraintLayout;
    private LinearLayout mLinearLayoutOTP;
    private TextView mTextViewOTPSentMessage;


    public RegistrationActivity() {
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_registration;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.login));
        spinner = findViewById(R.id.sp_interest);
        textInputEditTextEmail = findViewById(R.id.et_email);
        textInputEditTextName = findViewById(R.id.et_name);
        textInputEditTextPhone = findViewById(R.id.et_phone);
        textInputEditTextPhoneLogin = findViewById(R.id.et_phone_login);
        circleImageView = findViewById(R.id.imageViewProfile);
        btn_upload = findViewById(R.id.btn_upload);
        mProgressBar = findViewById(R.id.progress_bar);

        textInputEditTextPwd = findViewById(R.id.et_pwd);
        textInputEditTextPwdLogin = findViewById(R.id.et_pwd_login);

        mRegisterSubmit = findViewById(R.id.btn_register_submit);
        mButtonRegister = findViewById(R.id.btn_register);
        mButtonLogin = findViewById(R.id.btn_login);
        mButtonLoginSubmit = findViewById(R.id.btn_login_submit);
        registerView = findViewById(R.id.reg_lyt);
        loginView = findViewById(R.id.log_lyt);
        mSwitchLogin = findViewById(R.id.switchLogin);
        mSwitchRegister = findViewById(R.id.switchRegistration);

        spinnerState = findViewById(R.id.sp_state);
        spinnerDist = findViewById(R.id.sp_district);
        spinnerTaluk = findViewById(R.id.sp_taluk);
        spinnerHobli = findViewById(R.id.sp_hobli);

        mConstraintLayout = findViewById(R.id.constraintlyt);
        mLinearLayoutOTP = findViewById(R.id.linearLayoutOTP);

        mButtonResendOTP = findViewById(R.id.btn_resend_otp);
        mButtonVerifyOTP = findViewById(R.id.btn_submit_otp);
        mInputEditTextOTP = findViewById(R.id.et_otp);

        mTextViewOTPSentMessage = findViewById(R.id.textViewOTPSent);


        setUpNetwork();

    }

    @Override
    protected void initData() {
       /* adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listInterest);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        listState = new ArrayList<>();
        listState.add("Select State");
        listState.add("Karnataka");

        ArrayAdapter aa = new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_item, listState);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(aa);
        getDistricts();
    }

    @Override
    protected void initListener() {

        mRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               submitDetails();
            }
        });

        spinnerDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  product = mDistrictNamesList.get(position);
                if(position!=0)
                {
                    dist = districtModelList.get(position-1).getDist_name();
                    getTaluks(districtModelList.get(position-1).getDist_id());
                   // getAssembly(districtModelList.get(position-1).getDist_id());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTaluk = findViewById(R.id.sp_taluk);
        spinnerTaluk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  product = mDistrictNamesList.get(position);
                if(position!=0)
                {
                    taluk = talukModelList.get(position-1).getName();
                    getVillage(talukModelList.get(position-1).getTlk_id());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerVillage = findViewById(R.id.sp_village);
        mSpinnerVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0)
                {
                    village = villageModelList.get(position-1).getTanda_name();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerView.setVisibility(View.GONE);
                loginView.setVisibility(View.VISIBLE);
                mActionBar.setTitle(getString(R.string.login));
                mSwitchLogin.setChecked(isFarmer);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerView.setVisibility(View.VISIBLE);
                loginView.setVisibility(View.GONE);
                mActionBar.setTitle(getString(R.string.registration));
                mSwitchRegister.setChecked(isFarmer);
            }
        });

        mButtonLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLoginDetails();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });

        mSwitchRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isFarmer = b;
                disableFarmerRegistration(b);
                if(b)
                {
                    mSwitchRegister.setText(R.string.farmer);
                    type="1";
                }
                else
                {
                    mSwitchRegister.setText(R.string.merchant);
                    type="2";
                }
            }
        });
        mSwitchLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isFarmer = b;
                disableFarmerRegistration(b);
                if(b)
                {
                    mSwitchLogin.setText(R.string.farmer);
                    type="1";
                }
                else
                {
                    mSwitchLogin.setText(R.string.merchant);
                    type="2";
                }
                isFarmer = b;
            }
        });

        mButtonVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInputEditTextOTP.getText().toString()!=null && mInputEditTextOTP.getText().toString().length()==6)
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mButtonVerifyOTP.setEnabled(false);
                    verifyPhoneNumberWithCode(mVerificationId, mInputEditTextOTP.getText().toString());
                }
                else
                {
                    AppUtils.showToast(getString(R.string.valid_otp));
                }
            }
        });

        mButtonResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonVerifyOTP.setEnabled(true);
                resendVerificationCode(textInputEditTextPhone.getText().toString(), mResendToken);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // inputEditTextPhone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                   /* Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();*/
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.e(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                mButtonVerifyOTP.setVisibility(View.VISIBLE);
                mLinearLayoutOTP.setVisibility(View.VISIBLE);
                mConstraintLayout.setVisibility(View.GONE);
                mButtonResendOTP.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mTextViewOTPSentMessage.setText(getString(R.string.otp_sent)+" "+textInputEditTextPhone.getText().toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mButtonResendOTP.setVisibility(View.VISIBLE);
                    }
                }, 60000);

            }
        };


    }

    private void disableFarmerRegistration(boolean b) {
        if(b){
            spinnerState.setVisibility(View.VISIBLE);
            spinnerDist.setVisibility(View.VISIBLE);
            spinnerTaluk.setVisibility(View.VISIBLE);
            mSpinnerVillage.setVisibility(View.VISIBLE);
        }
        else{
            spinnerState.setVisibility(View.GONE);
            spinnerDist.setVisibility(View.GONE);
            spinnerTaluk.setVisibility(View.GONE);
            mSpinnerVillage.setVisibility(View.GONE);
        }
    }

    private void submitLoginDetails() {
        if(textInputEditTextPhoneLogin.getText()==null || textInputEditTextPhoneLogin.getText().length()!=10)
        {
            textInputEditTextPhoneLogin.setError(getString(R.string.phone_correct));
            return;
        }
        if(textInputEditTextPwdLogin.getText()==null || textInputEditTextPwdLogin.getText().length()<3)
        {
            textInputEditTextPwdLogin.setError(getString(R.string.enter_pwd));
            return;
        }

        mButtonLoginSubmit.setEnabled(false);
        mButtonLoginSubmit.setClickable(false);
        mProgressBar.setVisibility(View.VISIBLE);
        LoginModel loginModel = new LoginModel();
        loginModel.setPhone(textInputEditTextPhoneLogin.getText().toString());
        loginModel.setPwd(textInputEditTextPwdLogin.getText().toString());
        Call<APIResponseModel> userVerifyCall = apiInterface.verifyUser(loginModel.getPhone(), loginModel.getPwd(), type);
        userVerifyCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().isStatus())
                {
                    SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                    SharedPrefsHelper.getInstance().save(NAME,response.body().getName());
                    SharedPrefsHelper.getInstance().save(MOBILE, response.body().getPhone());
                    SharedPrefsHelper.getInstance().save(EMAIL, response.body().getEmail());
                    SharedPrefsHelper.getInstance().save(AVATAR, response.body().getImageURL());
                    SharedPrefsHelper.getInstance().save(USER_ID, response.body().getId());
                    startMainActivity();
                }else{
                    mButtonLoginSubmit.setEnabled(true);
                    mButtonLoginSubmit.setClickable(true);
                    showToast(getString(R.string.invalid));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mButtonLoginSubmit.setEnabled(true);
                mButtonLoginSubmit.setClickable(true);
                showToast(getString(R.string.something_wrong));
            }
        });
    }

    private void showToast(String string) {
        Toast.makeText(RegistrationActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private void submitDetails() {
        try {
            if(textInputEditTextName.getText()==null || textInputEditTextName.getText().toString().trim().length()<3)
            {
                textInputEditTextName.setError(getString(R.string.name_required));
                return;
            }
            if(textInputEditTextPhone.getText()==null || textInputEditTextPhone.getText().toString().trim().length()!=10)
            {
                textInputEditTextPhone.setError(getString(R.string.phone_correct));
                return;
            }
            if(textInputEditTextPwd.getText()==null || textInputEditTextPwd.getText().toString().trim().length()<3)
            {
                textInputEditTextPwd.setError(getString(R.string.enter_pwd));
                return;
            }
            /*if(course.contains("Select one"))
            {
                AppUtils.showSnackBar(activity, getString(R.string.please_select_area));
                return;
            }*/

            if(type.equalsIgnoreCase("1")) {
                if (dist == null || dist.trim().isEmpty()) {
                    AppUtils.showToast(getString(R.string.dist_cnt_emty));
                    return;
                }
                if (taluk == null || taluk.trim().isEmpty()) {
                    AppUtils.showToast(getString(R.string.taluk_cnt_emty));
                    return;
                }
            /*
            if(hobli==null || hobli.trim().isEmpty()){
                AppUtils.showToast(getString(R.string.hobli_cnt_emty));
                return;
            }
            if(gp==null || gp.trim().isEmpty()){
                AppUtils.showToast(getString(R.string.gp_cnt_emty));
                return;
            }*/
                if (village == null || village.trim().isEmpty()) {
                    AppUtils.showToast(getString(R.string.village_cnt_emty));
                    return;
                }
            }

            if(textInputEditTextEmail.getText()!=null) {
                if (!(Patterns.EMAIL_ADDRESS.matcher(textInputEditTextEmail.getText()).matches())) {
                    textInputEditTextEmail.setError(getString(R.string.email_correct));
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if(AppUtils.checkInternetStatus())
        {
            mProgressBar.setVisibility(View.VISIBLE);
            mRegisterSubmit.setClickable(false);
            mRegisterSubmit.setEnabled(false);
            databaseReference = FarmConnectApplication.getFireBaseRef();
            startPhoneNumberVerification("+91" + textInputEditTextPhone.getText().toString());
        }
        else{
            AppUtils.showToast(getString(R.string.no_inetrnet));
        }
    }
    private ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
    private void addPhoto() {
        if (!AppUtils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }

    private void addVideo() {
        if (!AppUtils.hasPermissions(this, PERMISSIONS)) {
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
                boolean gallery = AppUtils.checkPermission(RegistrationActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(RegistrationActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(RegistrationActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(RegistrationActivity.this);
                if (items[item].equals("Capture Video")) {
                    userChoosenTask = "Capture Video";
                    if (hasCamera()) {
                        videoCaptureIntent();
                    } else {
                        AppUtils.showToast("No Camera Found");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(RegistrationActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(RegistrationActivity.this);
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void videoFromGalleryIntent() {
        if (!AppUtils.checkInternetStatus()) {
            AppUtils.showToast("No Internet connected");
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
            AppUtils.showToast("No Internet connected");
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
            AppUtils.showToast("No Internet connected");
            //  return;
        }
        //Intent intent = new Intent(mActivity, AudioRecordActivity.class);
        //startActivityForResult(intent, RQS_RECORDING);
    }
    private void AudioLocalIntent()
    {
        if (!AppUtils.checkInternetStatus()) {
            AppUtils.showToast("No Internet connected");
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
                String filePath = FilePath.getPathFromInputStreamUri(RegistrationActivity.this, path);

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
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.d(TAG,"from SDK more than Kitkat");
                String filePath = getRealPathFromUri(RegistrationActivity.this, path);
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
                    AppUtils.showToast("unable to get file");
                }
            } else {

                String[] proj = {MediaStore.Images.Media.DATA};
                String result = null;

                CursorLoader cursorLoader = new CursorLoader(
                        RegistrationActivity.this,
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
    String imageURL;
    private void uploadImage(Uri filePath) {
        if(textInputEditTextPhone.getText().toString().trim()!=null && textInputEditTextPhone.getText().toString().trim().length()==10) {
            storage = FirebaseStorage.getInstance();
            circleImageView.setImageURI(filePath);
            circleImageView.setVisibility(View.VISIBLE);
            storageReference = storage.getReference();
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                final StorageReference ref = storageReference.child("images").child("profile").child(textInputEditTextPhone.getText().toString().trim()).child(UUID.randomUUID().toString());
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
                                            SharedPrefsHelper.getInstance().save(AVATAR, imageURL);


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

                                Toast.makeText(RegistrationActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

            }
        }
        else
        {
            Toast.makeText(RegistrationActivity.this, "Enter Phone Nuumber", Toast.LENGTH_LONG).show();
        }
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

    private void startMainActivity()
    {
        SharedPrefsHelper.getInstance().save(LOGIN_TYPE, isFarmer);
        if(isFarmer) {
            startActivity(new Intent(activity, MainActivity.class));
        }
        else{
            startActivity(new Intent(activity, MerchantMainActivity.class));
        }
        finish();

    }
    private void getTaluks(String dist_id) {
        Call<List<TalukModel>> listCall = apiInterface.getTaluks(dist_id);
        listCall.enqueue(new Callback<List<TalukModel>>() {
            @Override
            public void onResponse(Call<List<TalukModel>> call, Response<List<TalukModel>> response) {
                if(response.isSuccessful()){
                    talukModelList = response.body();
                    mTalukNames = new ArrayList<>();
                    mTalukNames.add(getString(R.string.Select_Taluk));
                    if(talukModelList!=null && talukModelList.size()>0) {
                        for (int i = 0; i < talukModelList.size(); i++) {
                            mTalukNames.add(talukModelList.get(i).getName());
                        }
                        ArrayAdapter aa = new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_item, mTalukNames);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTaluk.setAdapter(aa);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TalukModel>> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getVillage(String tlkdId) {
        Call<List<VillageModel>> listCall = apiInterface.getTandasByTaluk(tlkdId);
        listCall.enqueue(new Callback<List<VillageModel>>() {
            @Override
            public void onResponse(Call<List<VillageModel>> call, Response<List<VillageModel>> response) {
                if(response.isSuccessful()){
                    villageModelList = response.body();
                    mTandaNames = new ArrayList<>();
                    mTandaNames.add(getString(R.string.Select_Tanda));
                    if(villageModelList!=null && villageModelList.size()>0) {
                        for (int i = 0; i < villageModelList.size(); i++) {
                            mTandaNames.add(villageModelList.get(i).getTanda_name());
                        }
                        ArrayAdapter aa = new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_item, mTandaNames);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerVillage.setAdapter(aa);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<VillageModel>> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();

            }
        });
    }


/*    private void getAssembly(String dist_id) {
        Call<List<AssemblyModel>> listCall = apiInterface.getAssembly(dist_id);
        listCall.enqueue(new Callback<List<AssemblyModel>>() {
            @Override
            public void onResponse(Call<List<AssemblyModel>> call, Response<List<AssemblyModel>> response) {
                if(response.isSuccessful()){
                    assemblyModelList = response.body();
                    mAssemblyNamesList = new ArrayList<>();
                    mAssemblyNamesList.add("Select Assembly");
                    if(assemblyModelList!=null && assemblyModelList.size()>0) {
                        for (int i = 0; i < assemblyModelList.size(); i++) {
                            mAssemblyNamesList.add(assemblyModelList.get(i).getName());
                        }
                        ArrayAdapter aa = new ArrayAdapter(RegistrationActivity.this, android.R.layout.simple_spinner_item, mAssemblyNamesList);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerAssembly.setAdapter(aa);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AssemblyModel>> call, Throwable t) {

            }
        });
    }*/

    private void getDistricts() {
        Call<List<DistrictModel>> listCall = apiInterface.getAllDistricts();
        listCall.enqueue(new Callback<List<DistrictModel>>() {
            @Override
            public void onResponse(Call<List<DistrictModel>> call, Response<List<DistrictModel>> response) {
                if(response.isSuccessful()){
                    districtModelList = response.body();
                    mDistrictNamesList = new ArrayList<>();
                    mDistrictNamesList.add(getString(R.string.Select_District));
                    for (int i = 0; i<districtModelList.size(); i++){
                        mDistrictNamesList.add(districtModelList.get(i).getDist_name());
                    }
                    ArrayAdapter aa = new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_item, mDistrictNamesList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDist.setAdapter(aa);
                }
            }

            @Override
            public void onFailure(Call<List<DistrictModel>> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser currentUser = task.getResult().getUser();
                            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(textInputEditTextName.getText().toString()).build();
                            currentUser.updateProfile(update);
                            UserDetails userDetails = new UserDetails();
                            userDetails.setPhone(textInputEditTextPhone.getText().toString());
                            userDetails.setPwd(textInputEditTextPwd.getText().toString());
                            userDetails.setUserid(currentUser.getUid());
                            userDetails.setName(textInputEditTextName.getText().toString());
                            userDetails.setEmail(textInputEditTextEmail.getText().toString());
                            callNetworkAPI(userDetails);

                            //registeredSuccess(userDetails);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegistrationActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();
                                // mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    private void callNetworkAPI(final UserDetails userDetails) {
        userDetails.setEmail(textInputEditTextEmail.getText().toString().trim());
        userDetails.setMobile(textInputEditTextPhone.getText().toString().trim());
        userDetails.setName(textInputEditTextName.getText().toString().trim());
        userDetails.setPwd(textInputEditTextPwd.getText().toString().trim());
        Log.d(TAG, "callNetworkAPI: "+SharedPrefsHelper.getInstance().get(AVATAR));
        if(SharedPrefsHelper.getInstance().get(AVATAR)!=null )
        {
            userDetails.setAvatar(SharedPrefsHelper.getInstance().get(AVATAR).toString());
        }
        userDetails.setDist(dist);
        userDetails.setTaluk(taluk);
        userDetails.setHobli(hobli);
        userDetails.setVillage(village);
        userDetails.setGp(gp);
        userDetails.setType(type);
        userDetails.setState(state);
        userDetails.setFcm_token((String)SharedPrefsHelper.getInstance().get("FCM"));
        databaseReference.child("profile").child(textInputEditTextPhone.getText().toString()).setValue(userDetails);
        Call<APIResponseModel> userDetailsCall = apiInterface.updateUserTable(userDetails);
        userDetailsCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().isStatus()) {
                    SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                    SharedPrefsHelper.getInstance().save(COURSE, course);
                    SharedPrefsHelper.getInstance().save(NAME,textInputEditTextName.getText().toString());
                    SharedPrefsHelper.getInstance().save(EMAIL, textInputEditTextEmail.getText().toString());
                    SharedPrefsHelper.getInstance().save(MOBILE, textInputEditTextPhone.getText().toString());
                    SharedPrefsHelper.getInstance().save(AVATAR, imageURL);
                    SharedPrefsHelper.getInstance().save(USER_ID, userDetails.getUserid());
                    startMainActivity();
                }
                else {
                    mRegisterSubmit.setClickable(true);
                    mRegisterSubmit.setEnabled(true);
                    if(response.body().getMsg().contains("Duplicate")) {
                        String text = getString(R.string.duplicate_mobile);
                        Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mRegisterSubmit.setClickable(true);
                mRegisterSubmit.setEnabled(true);
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                showToast(getString(R.string.something_wrong));
            }
        });
    }

    private void registeredSuccess(APIResponseModel userDetails) {
        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
        SharedPrefsHelper.getInstance().save(NAME, userDetails.getName());
        SharedPrefsHelper.getInstance().save(EMAIL, userDetails.getEmail());
        SharedPrefsHelper.getInstance().save(MOBILE, userDetails.getPhone());
        SharedPrefsHelper.getInstance().save(AVATAR, userDetails.getImageURL());
        SharedPrefsHelper.getInstance().save(USER_ID, userDetails.getId());
        startMainActivity();
    }
}
