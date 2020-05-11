package com.patelheggere.farmconnect.activity.farmermain.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DatabaseReference;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmermain.ui.home.adapter.FarmerCropAdapter;
import com.patelheggere.farmconnect.base.BaseFragment;
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

public class HomeFragment extends BaseFragment {
    private static final String TAG = "MerchantNotificationsFragment";

    private HomeViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private FarmerCropAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<FarmerCropModel> cropModelsList;
    private ProgressBar mProgressBar;
    private TextView no_data;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home_farmer, container, false);
      /*  homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });*/
        mSwipeRefreshLayout = root.findViewById(R.id.swipeLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mProgressBar = root.findViewById(R.id.progress_bar);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        no_data = root.findViewById(R.id.no_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
        return root;
    }

    private void getData() {
        cropModelsList = new ArrayList<>();
        setUpNetwork();
        if(SharedPrefsHelper.getInstance().get(USER_ID)!=null) {
          //  mProgressBar.setVisibility(View.VISIBLE);
            Call<List<FarmerCropModel>> cropData = apiInterface.getAllLiveUserId(System.currentTimeMillis(), SharedPrefsHelper.getInstance().get(USER_ID).toString());
            cropData.enqueue(new Callback<List<FarmerCropModel>>() {
                @Override
                public void onResponse(Call<List<FarmerCropModel>> call, Response<List<FarmerCropModel>> response) {
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful() && response.body().size() > 0) {
                        cropModelsList = (ArrayList<FarmerCropModel>) response.body();
                        Collections.reverse(cropModelsList);
                        adapter = new FarmerCropAdapter(getContext(), cropModelsList);
                        mRecyclerView.setAdapter(adapter);
                        mProgressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                        AppUtils.showToast(getString(R.string.no_data));

                    }
                }

                @Override
                public void onFailure(Call<List<FarmerCropModel>> call, Throwable t) {
                    // AppUtils.showToast(getString(R.string.something_wrong));
                    Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);

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

}