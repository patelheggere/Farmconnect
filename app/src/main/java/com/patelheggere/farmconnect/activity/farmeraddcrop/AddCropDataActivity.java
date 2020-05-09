package com.patelheggere.farmconnect.activity.farmeraddcrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.patelheggere.farmconnect.FarmConnectApplication;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.registration.RegistrationActivity;
import com.patelheggere.farmconnect.base.BaseActivity;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.CropNameModel;
import com.patelheggere.farmconnect.model.DistrictModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.model.TalukModel;
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
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LANGUAGE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LANGUAGE_SELECTED;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;

public class AddCropDataActivity extends BaseActivity {
    private static final String TAG = "AddCropDataActivity";
    private Button submit, cancel, upload, takePhoto;

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
    private ImageView circleImageView;
    private String mMoobile;
    private TextInputEditText inputEditTextCropName, inputEditText, inputEditTextHarvestTime, inputEditTextQuantity, inputEditTextMinimumBid, inputEditTextMinimumOrder;
    private TextInputEditText inputEditTextCropLocation;
    private TextInputLayout mTextInputLayoutMinimumOrder;
    private int year, month, day;
    private Calendar calendar;
    private long harvestTimeInLong;
    private Spinner spinnerState, spinnerDist, spinnerTaluk, spinnerHobli, mSpinnerVillage, mSpinnerCropName, mSpinnerAssembly;
    private List<DistrictModel> districtModelList;
    private List<TalukModel> talukModelList;
    private List<VillageModel> villageModelList;

    private List<String> mDistrictNamesList;
    private List<String> mAssemblyNamesList;
    private List<String> mTalukNames;
    private List<String> mTandaNames;
    private List<String> listState, listDistrict, listTaluks, listHobli, listVillage, listGP;
    private String dist, distId, village, villageId, taluk, talukId, hobli, gp, state = "Karnataka", statId;
    private List<String> cropNamesList;
    private String cropName;
    private DatabaseReference databaseReferenceCropNames;
    private List<CropNameModel> cropNameWithIconList;
    private String cropIconImage;
    private RadioGroup mRadioGroupMinimumOrder;
    protected RadioButton mRadioButtonMinimumYes, mRadioButtonMinimumNo;
    private int isMinimumOrder=0;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_crop_details;
    }

    @Override
    protected void initView() {
        upload = findViewById(R.id.btnUploadPhoto);
        takePhoto = findViewById(R.id.btnPhoto);
        circleImageView = findViewById(R.id.photo);
        submit = findViewById(R.id.btnSubmit);
        inputEditTextHarvestTime = findViewById(R.id.et_harvesting_date);
        inputEditTextQuantity = findViewById(R.id.et_quantity);
        inputEditTextMinimumBid = findViewById(R.id.et_minbid);
        inputEditTextCropLocation = findViewById(R.id.et_location);
        inputEditTextMinimumOrder = findViewById(R.id.et_min_quantity);
        mTextInputLayoutMinimumOrder = findViewById(R.id.til_minimum_quantity);
        mRadioGroupMinimumOrder = findViewById(R.id.radiogroup_min_order);
        mRadioButtonMinimumYes = findViewById(R.id.radioMinimumYes);
        mRadioButtonMinimumNo = findViewById(R.id.radioMinimumNo);

        spinnerState = findViewById(R.id.sp_state);
        spinnerDist = findViewById(R.id.sp_district);
        spinnerTaluk = findViewById(R.id.sp_taluk);
        spinnerHobli = findViewById(R.id.sp_hobli);
        mSpinnerCropName = findViewById(R.id.sp_crop_name);
        mTextInputLayoutMinimumOrder.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void initListener() {
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(filePath);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetails();
            }
        });

        inputEditTextHarvestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCropDataActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String dd = "", mm = "";
                        if (i2 < 10) {
                            dd = "0" + i2;
                        } else {
                            dd = "" + i2;
                        }
                        if (i1 < 9) {
                            mm = "0" + (i1 + 1);
                        } else {
                            mm = "" + (i1 + 1);
                        }
                        inputEditTextHarvestTime.setText(dd + "/" + mm + "/" + i);

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        cal.set(Calendar.MONTH, datePicker.getMonth());
                        cal.set(Calendar.YEAR, datePicker.getYear());
                        harvestTimeInLong = cal.getTimeInMillis();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        mRadioGroupMinimumOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radioMinimumYes)
                {
                    isMinimumOrder = 1;
                    mTextInputLayoutMinimumOrder.setVisibility(View.VISIBLE);
                }
                else {
                    isMinimumOrder = 0;
                    mTextInputLayoutMinimumOrder.setVisibility(View.GONE);
                }
            }
        });

        listState = new ArrayList<>();
        listState.add("Select State");
        listState.add("Karnataka");


        setUpNetwork();
        getCropNames();

        ArrayAdapter aa = new ArrayAdapter(AddCropDataActivity.this, R.layout.spinner_item, listState);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(aa);
        getDistricts();

        mSpinnerCropName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0)
                {
                    cropName = cropNamesList.get(position);
                    cropIconImage = cropNameWithIconList.get(position).getCropIcon();
                  //  getTaluks(districtModelList.get(position-1).getDist_id());
                    // getAssembly(districtModelList.get(position-1).getDist_id());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  product = mDistrictNamesList.get(position);
                if(position!=0)
                {
                    dist = districtModelList.get(position-1).getDist_name();
                    distId = districtModelList.get(position-1).getDist_id();

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
                    talukId = talukModelList.get(position-1).getTlk_id();
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
                    villageId = villageModelList.get(position-1).getTanda_name();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                        ArrayAdapter aa = new ArrayAdapter(AddCropDataActivity.this, R.layout.spinner_item, mTalukNames);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTaluk.setAdapter(aa);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TalukModel>> call, Throwable t) {
                Toast.makeText(AddCropDataActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
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
                        ArrayAdapter aa = new ArrayAdapter(AddCropDataActivity.this, R.layout.spinner_item, mTandaNames);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerVillage.setAdapter(aa);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<VillageModel>> call, Throwable t) {
                Toast.makeText(AddCropDataActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getCropNames(){

        databaseReferenceCropNames = FarmConnectApplication.getFireBaseRef();
        databaseReferenceCropNames = databaseReferenceCropNames.child(AppUtils.Constants.CROP_LIST);
        databaseReferenceCropNames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cropNameWithIconList = new ArrayList<>();
                cropNameWithIconList.add(new CropNameModel());
                cropNamesList = new ArrayList<>();
                cropNamesList.add("Select Crop");

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    cropNameWithIconList.add(snapshot.getValue(CropNameModel.class));
                    if(SharedPrefsHelper.getInstance().get(LANGUAGE).toString().equalsIgnoreCase("ka")){
                        cropNamesList.add(snapshot.getValue(CropNameModel.class).getNameKn());
                    }
                    else {
                        cropNamesList.add(snapshot.getValue(CropNameModel.class).getName());
                    }
                }
                ArrayAdapter cropAdapter = new ArrayAdapter(AddCropDataActivity.this, R.layout.spinner_item, cropNamesList);
                cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerCropName.setAdapter(cropAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    FarmerCropModel farmerCropModel = new FarmerCropModel();
    private void submitDetails() {

        farmerCropModel.setBidSuccess(0);
        farmerCropModel.setCropImage(imageURL);
        farmerCropModel.setCropName(cropName);
        farmerCropModel.setNoOfBids(0);
        farmerCropModel.setCropImageIcon(cropIconImage);
        farmerCropModel.setStatus("active");
        farmerCropModel.setHarvestingTime(harvestTimeInLong);
        farmerCropModel.setUploadedTime(System.currentTimeMillis());
        farmerCropModel.setEndTime(farmerCropModel.getUploadedTime()+86400000);
        farmerCropModel.setBidderPrice(0+"");
        farmerCropModel.setCropLocation(inputEditTextCropLocation.getText().toString());
        farmerCropModel.setFarmerPrice(inputEditTextMinimumBid.getText().toString());
        farmerCropModel.setQty(inputEditTextQuantity.getText().toString());
        farmerCropModel.setUserid(SharedPrefsHelper.getInstance().get(USER_ID).toString());
        farmerCropModel.setState(state);
        farmerCropModel.setDistrict(dist);
        farmerCropModel.setTaluk(taluk);
        farmerCropModel.setVillage(village);
        farmerCropModel.setIsMinimumOrder(isMinimumOrder);
        farmerCropModel.setStateId(statId);
        farmerCropModel.setDistrictId(distId);
        farmerCropModel.setTalukId(talukId);
        farmerCropModel.setVillageId(villageId);
        farmerCropModel.setMinimumOrder(Long.parseLong(inputEditTextMinimumOrder.getText().toString()));


        Call<APIResponseModel> cropModelCall = apiInterface.addCropDetails(farmerCropModel);
        cropModelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                if(response.isSuccessful() && response.body().isStatus()){
                    AppUtils.showToast(getString(R.string.added_crop));
                    AppUtils.sendPush(farmerCropModel.getCropName(), "Crop Added");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                AppUtils.showToast(getString(R.string.something_wrong));
            }
        });

    }
    protected ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

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
                    ArrayAdapter aa = new ArrayAdapter(AddCropDataActivity.this, R.layout.spinner_item, mDistrictNamesList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDist.setAdapter(aa);
                }
            }

            @Override
            public void onFailure(Call<List<DistrictModel>> call, Throwable t) {
                Toast.makeText(AddCropDataActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
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
                boolean gallery = AppUtils.checkPermission(AddCropDataActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(AddCropDataActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCropDataActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(AddCropDataActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(AddCropDataActivity.this);
                if (items[item].equals("Capture Video")) {
                    userChoosenTask = "Capture Video";
                    if (hasCamera()) {
                        videoCaptureIntent();
                    } else {
                        Toast.makeText(AddCropDataActivity.this,"No Camera Found", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCropDataActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = AppUtils.checkPermission(AddCropDataActivity.this);
                boolean camera = AppUtils.checkPermissionCamera(AddCropDataActivity.this);
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
            // AppUtils.showToast("No Internet connected");
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
            //AppUtils.showToast("No Internet connected");
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
            //  AppUtils.showToast("No Internet connected");
            //  return;
        }
        //Intent intent = new Intent(mActivity, AudioRecordActivity.class);
        //startActivityForResult(intent, RQS_RECORDING);
    }
    private void AudioLocalIntent()
    {
        if (!AppUtils.checkInternetStatus()) {
            // AppUtils.showToast("No Internet connected");
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
                String filePath = FilePath.getPathFromInputStreamUri(AddCropDataActivity.this, path);

                original = new File(filePath);
                String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                    crop_ImageAndUpload(original,extension_file,mediaType);
                }else {
                    circleImageView.setImageURI(Uri.parse(filePath));
                    upload.setVisibility(View.VISIBLE);
                    circleImageView.setVisibility(View.VISIBLE);

                    //uploadImageToAWS(new File(filePath), mediaType);
                    // uploadImage(Uri.fromFile(original));
                }
                //OustSdkTools.showToast("can't select attachment from google photos app");
                //return;
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Log.d(TAG,"from SDK more than Kitkat");
                String filePath = getRealPathFromUri(AddCropDataActivity.this, path);
                if (filePath != null) {
                    original = new File(filePath);
                    circleImageView.setImageURI(Uri.parse(filePath));
                    upload.setVisibility(View.VISIBLE);
                    circleImageView.setVisibility(View.VISIBLE);
                    //  uploadImage(Uri.parse(filePath));
                  /*  String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                    if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                        crop_ImageAndUpload(original,extension_file,mediaType);
                    }else {
                        uploadImage(Uri.parse(filePath));
                        //uploadImageToAWS(new File(filePath), mediaType);
                    }*/
                } else {
                    //  AppUtils.showToast("unable to get file");
                }
            } else {

                String[] proj = {MediaStore.Images.Media.DATA};
                String result = null;

                CursorLoader cursorLoader = new CursorLoader(
                        AddCropDataActivity.this,
                        path, proj, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor != null) {
                    int column_index =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    result = cursor.getString(column_index);
                    if (result != null) {
                        circleImageView.setImageURI(Uri.parse(result));
                        upload.setVisibility(View.VISIBLE);
                        circleImageView.setVisibility(View.VISIBLE);
                        // uploadImage(Uri.parse(result));
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
            filePath = Uri.fromFile(original);
            circleImageView.setImageURI(Uri.fromFile(original));
            upload.setVisibility(View.VISIBLE);
            circleImageView.setVisibility(View.VISIBLE);
            // uploadImage(Uri.fromFile(original));
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
        long time = System.currentTimeMillis();

        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), time + ".jpg");
        if(!destination.getParentFile().exists()){
            destination.getParentFile().mkdirs();
        }

        FileOutputStream fo;
        try {
            if(!destination.exists())
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
                circleImageView.setImageURI(Uri.fromFile(destination));
                upload.setVisibility(View.VISIBLE);
                //uploadImage(Uri.fromFile(destination));
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
                circleImageView.setImageURI(filePath);
                upload.setVisibility(View.VISIBLE);
                circleImageView.setVisibility(View.VISIBLE);
                //  uploadImage(filePath);
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
        if(SharedPrefsHelper.getInstance().get(USER_ID).toString().trim()!=null) {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(AddCropDataActivity.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                final StorageReference ref = storageReference.child("images").child("crop").child(SharedPrefsHelper.getInstance().get(USER_ID).toString().trim()).child(UUID.randomUUID().toString());
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
                                            // SharedPrefsHelper.getInstance().save("PICK_URL", imageURL);


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

                                Toast.makeText(AddCropDataActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddCropDataActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AddCropDataActivity.this, "Enter Phone Nuumber", Toast.LENGTH_LONG).show();
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
}
