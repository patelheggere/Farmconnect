package com.patelheggere.farmconnect.activity.merchantmain.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.RazorPGActivity;
import com.patelheggere.farmconnect.activity.merchantmain.ui.notifications.adapter.FarmerCropNotificationAdapter;
import com.patelheggere.farmconnect.activity.merchantmain.ui.notifications.adapter.MerchantCropAdapter;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.FarmerAndCropDetails;
import com.patelheggere.farmconnect.model.FarmerBidNotificationModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.model.RazorPayOrderModel;
import com.patelheggere.farmconnect.model.RazorPayOrderRequestModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.RetrofitInstance;
import com.patelheggere.farmconnect.network.auth.AuthAPIService;
import com.patelheggere.farmconnect.network.auth.ServiceGenerator;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;


public class MerchantNotificationsFragment extends Fragment {
    private static final String TAG = "MerchantFragment";

    private NotificationsViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private FarmerCropNotificationAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<FarmerBidNotificationModel> farmerBidNotificationModelList;
    private ProgressBar mProgressBar;
    private TextView no_data;
    private boolean isPaymentMandatory=true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications_farmer, container, false);
       /* homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });*/
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mProgressBar = root.findViewById(R.id.progress_bar);
        mProgressBar.bringToFront();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        no_data = root.findViewById(R.id.no_data);
        setUpNetwork();
        getData();
        return root;
    }

    private void getData() {
        farmerBidNotificationModelList = new ArrayList<>();
        if(SharedPrefsHelper.getInstance().get(USER_ID)!=null) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<List<FarmerBidNotificationModel>> cropData = apiInterface.getAllMerchantNotifications(SharedPrefsHelper.getInstance().get(USER_ID).toString());
            cropData.enqueue(new Callback<List<FarmerBidNotificationModel>>() {
                @Override
                public void onResponse(Call<List<FarmerBidNotificationModel>> call, Response<List<FarmerBidNotificationModel>> response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body().size() > 0) {
                        farmerBidNotificationModelList = (ArrayList<FarmerBidNotificationModel>) response.body();
                        Collections.reverse(farmerBidNotificationModelList);
                        adapter = new FarmerCropNotificationAdapter(getContext(), farmerBidNotificationModelList, selectItem2);
                        mRecyclerView.setAdapter(adapter);
                        mProgressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                    } else {
                        AppUtils.showToast(getString(R.string.no_data));
                    }
                }

                @Override
                public void onFailure(Call<List<FarmerBidNotificationModel>> call, Throwable t) {
                    // AppUtils.showToast(getString(R.string.something_wrong));
                    Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }


    }
    protected ApiInterface apiInterface;

    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    public FarmerCropNotificationAdapter.SelectItem selectItem2 = new FarmerCropNotificationAdapter.SelectItem() {
        @Override
        public void selectedPosition(FarmerBidNotificationModel farmerBidNotificationModel) {
            if(farmerBidNotificationModel.getIsPaymentMade()==0 && isPaymentMandatory)
            {
                farmerBidNotificationModel2 = farmerBidNotificationModel;
                Intent intent=new Intent(getContext(),RazorPGActivity.class);
                startActivityForResult(intent, PAYMENT_REQUEST);
            }
            else
            {
                fetchFarmerAndCropDetails(farmerBidNotificationModel);
            }
        }
    };

    private FarmerBidNotificationModel farmerBidNotificationModel2;
    private void proceedForPayment(FarmerBidNotificationModel farmerBidNotificationModel) {

    }

    private void fetchFarmerAndCropDetails(FarmerBidNotificationModel farmerBidNotificationModel) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<FarmerAndCropDetails> farmerAndCropDetailsCall = apiInterface.GetFarmerAndCropDetails(farmerBidNotificationModel.getFarmer_id(), farmerBidNotificationModel.getCrop_id());
        farmerAndCropDetailsCall.enqueue(new Callback<FarmerAndCropDetails>() {
            @Override
            public void onResponse(Call<FarmerAndCropDetails> call, Response<FarmerAndCropDetails> response) {
              //  Log.d(TAG, "onResponse: "+response.body().toString());
                mProgressBar.setVisibility(View.GONE);
                showFarmerDetails(response.body());

            }

            @Override
            public void onFailure(Call<FarmerAndCropDetails> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private View dialogView;
    private AlertDialog alertDialogFarmerDetails;
    private void showFarmerDetails(FarmerAndCropDetails farmerAndCropDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.adds_alert_view, null);
        TextView textViewCName, textViewCLoc, textViewDist, textViewFName, textViewFPhone, textViewFDist;
        Button buttonDirection, buttonCancel;
        textViewCName = dialogView.findViewById(R.id.textViewCropValue);
        textViewCLoc = dialogView.findViewById(R.id.textViewCropLocationValue);
        textViewDist = dialogView.findViewById(R.id.textViewCropDistValue);

        textViewFName = dialogView.findViewById(R.id.textViewFarmerNameValue);
        textViewFPhone = dialogView.findViewById(R.id.textViewFarmerPhoneValue);
        textViewFDist = dialogView.findViewById(R.id.textViewFarmerDistValue);

        textViewCName.setText(farmerAndCropDetails.getCropName());
        textViewCLoc.setText(farmerAndCropDetails.getCropVillageId());
        textViewDist.setText(farmerAndCropDetails.getCropDistrict());

        textViewFName.setText(farmerAndCropDetails.getFarmersName());
        textViewFPhone.setText(farmerAndCropDetails.getFarmerPhone());
        textViewFDist.setText(farmerAndCropDetails.getFarmerDist());

        buttonDirection = dialogView.findViewById(R.id.buttonDirection);
        buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        buttonDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFarmerDetails.dismiss();
            }
        });

        builder.setView(dialogView);
        alertDialogFarmerDetails = builder.create();
        alertDialogFarmerDetails.show();
    }

    private void updatePayment(FarmerBidNotificationModel farmerBidNotificationModel){
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.setElevation(10);
        }
        farmerBidNotificationModel.setIsPaymentMade(1);
        Call<APIResponseModel> call = apiInterface.updateMerchantPayment(farmerBidNotificationModel);
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                //Log.d(TAG, "onResponse: "+response.body().toString());
                mProgressBar.setVisibility(View.GONE);
               // farmerBidNotificationModel.setIsPaymentMade(1);
                Toast.makeText(getContext(), " Payment success" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private int PAYMENT_REQUEST = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==PAYMENT_REQUEST && data!=null && data.getStringExtra("SUCCESS").equalsIgnoreCase("success"))
        {
           updatePayment(farmerBidNotificationModel2);
        }
        else{
            AppUtils.showToast("Payment fail");
        }
    }

}