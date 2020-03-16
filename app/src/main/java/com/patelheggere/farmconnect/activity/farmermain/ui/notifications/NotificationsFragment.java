package com.patelheggere.farmconnect.activity.farmermain.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmermain.ui.home.adapter.FarmerCropAdapter;
import com.patelheggere.farmconnect.activity.farmermain.ui.notifications.adapter.FarmerCropNotificationAdapter;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.FarmerBidNotificationModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.RetrofitInstance;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;

public class NotificationsFragment extends Fragment implements FarmerCropNotificationAdapter.SelectItem{
    private static final String TAG = "FarmerNotificationsFragment";
    private NotificationsViewModel notificationsViewModel;
    private RecyclerView mRecyclerView;
    private FarmerCropNotificationAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<FarmerBidNotificationModel> farmerBidNotificationModelList;
    private ProgressBar mProgressBar;
    private TextView no_data;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications_farmer, container, false);
       // final TextView textView = root.findViewById(R.id.text_notifications);
        /*notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mProgressBar = root.findViewById(R.id.progress_bar);
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
            Call<List<FarmerBidNotificationModel>> cropData = apiInterface.getAllFarmerNotifications(SharedPrefsHelper.getInstance().get(USER_ID).toString());
            cropData.enqueue(new Callback<List<FarmerBidNotificationModel>>() {
                @Override
                public void onResponse(Call<List<FarmerBidNotificationModel>> call, Response<List<FarmerBidNotificationModel>> response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body().size() > 0) {
                        farmerBidNotificationModelList = (ArrayList<FarmerBidNotificationModel>) response.body();
                        Collections.reverse(farmerBidNotificationModelList);
                        adapter = new FarmerCropNotificationAdapter(getContext(), farmerBidNotificationModelList, selectItem);
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

    private FarmerCropNotificationAdapter.SelectItem selectItem = new FarmerCropNotificationAdapter.SelectItem() {
        @Override
        public void selectedPosition(final FarmerBidNotificationModel farmerBidNotificationModel) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<APIResponseModel> apiResponseModelCall = apiInterface.NotifyMerchant(farmerBidNotificationModel);
            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                @Override
                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                    for (int i=0;i<farmerBidNotificationModelList.size(); i++)
                    {
                        if(farmerBidNotificationModelList.get(i).getSl_no()==farmerBidNotificationModel.getSl_no())
                        {
                            farmerBidNotificationModelList.get(i).setIsBidAccepted(farmerBidNotificationModel.getIsBidAccepted());
                            adapter.notifyDataSetChanged();
                        }
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<APIResponseModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    };

    @Override
    public void selectedPosition(final FarmerBidNotificationModel farmerBidNotificationModel) {

    }
}